package br.com.eskaryos.rankup.data;

import br.com.eskaryos.rankup.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Objects;

public class Lang {

    public static String last_rank = "§cYou have already reached the last rank!";
    public static String evolvedMsg = "§aYou evolved to rank <rank>\"";
    public static String evolvedGlobal = "<player> evolved to rank <rank>";

    public static String SQLiteSuccess = "§aSQLite connection completed.";
    public static String SQLiteError = "§cCould not start SQLite.";
    public static String TableCreated = "§aSQLite table created.";
    public static String TableNoCreated = "§cSQLite table not created.";
    public static String PlayerNoLoaded = "§cThe player §c<player> was not loaded.";

    public static String events_sucess = "§aAll events were successfully loaded.";
    public static String events_error = "§cEvents were not loaded.";

    public static String commands_sucess = "§aAll commands were successfully loaded.";
    public static String commands_error = "§cCommands were not loaded.";



    public static void LoadLang(){
        File file = new File(Main.plugin.getDataFolder(),"lang.yml");
        if(!file.exists()){Main.plugin.saveResource("lang.yml",true);}
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        last_rank = convert(Objects.requireNonNull(config.getString("last_rank")));
        evolvedMsg = convert(Objects.requireNonNull(config.getString("evolved-msg")));
        evolvedGlobal = convert(Objects.requireNonNull(config.getString("evolved-global")));

        SQLiteSuccess = convert(Objects.requireNonNull(config.getString("sqlite-sucess")));
        SQLiteError = convert(Objects.requireNonNull(config.getString("sqlite-error")));
        TableCreated = convert(Objects.requireNonNull(config.getString("table-created")));
        TableNoCreated = convert(Objects.requireNonNull(config.getString("table-error")));
        PlayerNoLoaded = convert(Objects.requireNonNull(config.getString("player-noloaded")));
        events_sucess = convert(Objects.requireNonNull(config.getString("events_sucess")));
        events_error = convert(Objects.requireNonNull(config.getString("events_error")));
        commands_sucess = convert(Objects.requireNonNull(config.getString("commands_sucess")));
        commands_sucess = convert(Objects.requireNonNull(config.getString("commands_error")));

    }

    public static String convert(String m){
        return m.replace("&","§");
    }

}
