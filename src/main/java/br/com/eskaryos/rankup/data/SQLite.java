package br.com.eskaryos.rankup.data;

import br.com.eskaryos.rankup.Main;
import br.com.eskaryos.rankup.ranks.Rank;
import br.com.eskaryos.rankup.ranks.RankMain;
import br.com.eskaryos.rankup.utils.bukkit.Logger;
import br.com.eskaryos.rankup.utils.api.placeholder.RankHolder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.sql.*;
import java.util.Objects;
import java.util.UUID;

public class SQLite {

    public static Connection con = null;

    public static void openSQL(){
        File file = new File(Main.plugin.getDataFolder(),"database.db");

        String url = "jdbc:sqlite:"+file;

        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection(url);
            createTable("rankup");
            Logger.log(Logger.LogLevel.INFO,Lang.SQLiteSuccess);
        } catch (Exception e) {
            Logger.log(Logger.LogLevel.ERROR,Lang.SQLiteError);
            Main.plugin.getPluginLoader().disablePlugin(Main.plugin);
        }
    }
    public static void createTable(String table){
        PreparedStatement stm;
        try {
            stm = con.prepareStatement("CREATE TABLE IF NOT EXISTS `"+table+"` (`uuid` TEXT, `rank` TEXT, `requirements` TEXT)");
            stm.execute();
            stm.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Logger.log(Logger.LogLevel.ERROR,Lang.TableNoCreated);
            Main.plugin.getPluginLoader().disablePlugin(Main.plugin);
        }
    }

    public static void close(){
        if(con !=null){
            try {
                con.close();
                con = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /***
     * Database system to load player data
     * @param uuid Player uuid
     */
    public static void loadPlayer(UUID uuid){
        createPlayer(uuid,"rankup");
        try {
            Rank rank = RankMain.clone(RankMain.getRankByName(getRank(uuid)));
            DataMain.getProfileList().put(uuid,new Profile(uuid,rank));

            Profile profile = DataMain.getProfile(uuid);

            if(!RankMain.isLastRank(Objects.requireNonNull(Bukkit.getPlayer(uuid)))){
                profile.setNext(RankMain.clone(Objects.requireNonNull(RankMain.getRankById(rank.getOrder() + 1))));
                DataMain.deserializeRequirement(Objects.requireNonNull(getRequirements(uuid)),profile.getNext());
            }
        } catch (Exception e) {
            DataMain.getProfileList().put(uuid,new Profile(uuid,RankMain.clone(RankMain.getDefaultRank())));
        }
    }
    public static String getRequirements(UUID uuid){
        try{
            PreparedStatement statement = con.prepareStatement(
                    "SELECT * FROM rankup WHERE UUID=?");
            statement.setString(1,uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();
            String requirements = results.getString("requirements");
            results.close();
            statement.close();
            return requirements;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    public static String getRank(UUID uuid) {
        try {
            PreparedStatement statement = con
                    .prepareStatement("SELECT * FROM rankup WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();
            String rank = results.getString("rank");
            results.close();
            statement.close();
            return rank;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void createPlayer(UUID uuid,String table){
        PreparedStatement stm;
        if(!playerExists(uuid)){
            try {
                stm = con.prepareStatement("INSERT INTO "+table+" (uuid,rank,requirements) VALUES (?,?,?)");
                if(Lang.first_join){
                    Player p = Bukkit.getPlayer(uuid);
                    assert p != null;
                    for(String s : Lang.joinMessage){
                        p.sendMessage(RankHolder.hook(p,s));
                    }
                }
                stm.setString(1,uuid.toString());
                stm.setString(2, RankMain.getDefaultRank().clone().getName());
                String r = DataMain.serializeRequirements(RankMain.getDefaultRank());
                stm.setString(3,r);
                stm.executeUpdate();
                stm.close();

            } catch (SQLException e) {
                Logger.log(Logger.LogLevel.INFO,RankHolder.hook(Bukkit.getPlayer(uuid),Lang.PlayerNoLoaded));
                e.printStackTrace();
            }
        }

    }

    public static void setRank(UUID uuid, String rank) {
        try {
            PreparedStatement statement = con
                    .prepareStatement("UPDATE rankup SET rank=? WHERE UUID=?");
            statement.setString(1, rank);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void setRequirement(UUID uuid, Rank rank) {
        try {
            PreparedStatement statement = con
                    .prepareStatement("UPDATE rankup SET requirements=? WHERE UUID=?");
            statement.setString(1, DataMain.serializeRequirements(rank));
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean playerExists(UUID uuid) {
        try {
            PreparedStatement statement = con
                    .prepareStatement("SELECT * FROM rankup WHERE uuid=?");
            statement.setString(1, uuid.toString());

            ResultSet results = statement.executeQuery();
            if (results.next()) {
                return true;
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
