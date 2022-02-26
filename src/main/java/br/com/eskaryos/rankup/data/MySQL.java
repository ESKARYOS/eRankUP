package br.com.eskaryos.rankup.data;

import br.com.eskaryos.rankup.Main;
import br.com.eskaryos.rankup.ranks.Rank;
import br.com.eskaryos.rankup.ranks.RankMain;
import br.com.eskaryos.rankup.utils.api.placeholder.RankHolder;
import br.com.eskaryos.rankup.utils.bukkit.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class MySQL {

    private String host, database, username, password, port, table;
    private Connection connection;

    public MySQL(String host, String database, String username, String password, String port, String table) {
        this.host = host;
        this.database = database;
        this.username = username;
        this.password = password;
        this.port = port;
        setTable(table);
        try {
            synchronized (this) {
                if (getConnection() != null && !getConnection().isClosed()) {
                    return;
                }
                Class.forName("com.mysql.jdbc.Driver");
                setConnection(DriverManager.getConnection("jdbc:mysql://" + this.host + ":"
                        + this.port + "/" + this.database, this.username, this.password));
                Logger.log(Logger.LogLevel.INFO,
                        "§a§l[" + Main.plugin.getName() + "] MySQL Conectado com sucesso");
                queryUpdate("CREATE TABLE IF NOT EXISTS `"+table+"` (`uuid` varchar(200), `rank` varchar(100), `requirements` TEXT)");
            }
        } catch (SQLException | ClassNotFoundException e) {
            Logger.log(Logger.LogLevel.ERROR,
                    "§c§l[" + Main.plugin.getName() + "] Não foi possivél conectar ao banco de dados");
            Bukkit.getPluginManager().disablePlugin(Main.plugin);
        }
    }

    public static void closeResources(ResultSet rs, PreparedStatement st) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {

            }
        }
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {

            }
        }
    }

    public void queryUpdate(String query) {
        PreparedStatement stat = null;
        try {
            stat = getConnection().prepareStatement(query);
            stat.executeUpdate();
        } catch (SQLException e) {

        } finally {
            MySQL.closeResources(null, stat);
        }

    }

    public void close() {
        try {
            getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /***
     * Database system to load player data
     * @param uuid Player uuid
     */
    public void loadPlayer(UUID uuid){
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
    public String getRequirements(UUID uuid){
        try{
            PreparedStatement statement = connection.prepareStatement(
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
    public String getRank(UUID uuid) {
        try {
            PreparedStatement statement = connection
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

    public void createPlayer(UUID uuid,String table){
        PreparedStatement stm;
        if(!playerExists(uuid)){
            try {
                stm = connection.prepareStatement("INSERT INTO "+table+" (uuid,rank,requirements) VALUES (?,?,?)");
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

    public void setRank(UUID uuid, String rank) {
        try {
            PreparedStatement statement = connection
                    .prepareStatement("UPDATE rankup SET rank=? WHERE UUID=?");
            statement.setString(1, rank);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void setRequirement(UUID uuid, Rank rank) {
        try {
            PreparedStatement statement = connection
                    .prepareStatement("UPDATE rankup SET requirements=? WHERE UUID=?");
            statement.setString(1, DataMain.serializeRequirements(rank));
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean playerExists(UUID uuid) {
        try {
            PreparedStatement statement = connection
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
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }
}