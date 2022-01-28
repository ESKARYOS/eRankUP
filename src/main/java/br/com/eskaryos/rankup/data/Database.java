package br.com.eskaryos.rankup.data;

import br.com.eskaryos.rankup.Main;
import br.com.eskaryos.rankup.ranks.Rank;
import br.com.eskaryos.rankup.ranks.RankMain;
import br.com.eskaryos.rankup.utils.Logger;
import org.bukkit.Bukkit;

import java.io.File;
import java.sql.*;
import java.util.UUID;

public class Database {

    public static Connection con = null;

    public static void openSQL(){
        File file = new File(Main.plugin.getDataFolder(),"database.db");

        String url = "jdbc:sqlite:"+file;

        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection(url);
            createTable();
            Logger.log(Logger.LogLevel.INFO,"§aConexão SQLite concluida.");
        } catch (Exception e) {
            Logger.log(Logger.LogLevel.ERROR,"§cNão foi possivél iniciar o SQLite.");
            Main.plugin.getPluginLoader().disablePlugin(Main.plugin);
        }
    }

    public static void createTable(){
        PreparedStatement stm = null;
        try {

            stm = con.prepareStatement("CREATE TABLE IF NOT EXISTS `rankup` (`uuid` TEXT, `rank` TEXT)");
            Logger.log(Logger.LogLevel.INFO,"§aTabela do SQLite criada.");
            stm.execute();
            stm.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Logger.log(Logger.LogLevel.ERROR,"§cNão foi possivél criar tabela no SQLite.");
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

    public static void loadPlayer(UUID uuid){
        createPlayer(uuid);

        try {
            Rank rank = RankMain.clone(RankMain.getRankByName(getRank(uuid)));
            DataMain.getProfileList().put(uuid,new Profile(uuid,rank));
            Logger.log(Logger.LogLevel.INFO,"§aJogador §f" + Bukkit.getPlayer(uuid).getName() + " §a carregado");
        } catch (Exception e) {
            DataMain.getProfileList().put(uuid,new Profile(uuid,RankMain.clone(RankMain.getDefaultRank())));
            Logger.log(Logger.LogLevel.INFO,"§cJogador §f" + Bukkit.getPlayer(uuid).getName() + " §cnão carregado");
        }
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

    public static void createPlayer(UUID uuid){
        PreparedStatement stm = null;
        if(!playerExists(uuid)){
            try {
                Logger.log(Logger.LogLevel.INFO,"§aJogador §f" + Bukkit.getPlayer(uuid).getName() + "§a criado");
                stm = con.prepareStatement("INSERT INTO rankup (uuid,rank) VALUES (?,?)");

                stm.setString(1,uuid.toString());
                stm.setString(2, RankMain.getDefaultRank().getName());
                stm.executeUpdate();
                stm.close();
            } catch (SQLException e) {
                Logger.log(Logger.LogLevel.INFO,"§cJogador §f" + Bukkit.getPlayer(uuid).getName() + " §cnão criado");
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