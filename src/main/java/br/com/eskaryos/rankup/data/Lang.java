package br.com.eskaryos.rankup.data;

import br.com.eskaryos.rankup.Main;
import br.com.eskaryos.rankup.utils.StringUtils;
import br.com.eskaryos.rankup.utils.api.RankHolder;
import br.com.eskaryos.rankup.utils.api.SoundsAPI;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Lang {

    public static String last_rank = "§cYou have already reached the last rank!";
    public static String evolvedMsg = "§aYou evolved to rank <rank>";
    public static String evolvedGlobal = "<player> §aevolved to rank <rank>";
    public static String rankredefine = "&aYour rank has been reset to rank <rank>";
    public static String rankredefineerror = "&cThere was a problem trying to reset your rank.";

    public static String SQLiteSuccess = "§aSQLite connection completed.";
    public static String SQLiteError = "§cCould not start SQLite.";
    public static String TableCreated = "§aSQLite table created.";
    public static String TableNoCreated = "§cSQLite table not created.";
    public static String PlayerNoLoaded = "§cThe player §c<player> was not loaded.";

    public static String events_sucess = "§aAll events were successfully loaded.";
    public static String events_error = "§cEvents were not loaded.";

    public static String commands_sucess = "§aAll commands were successfully loaded.";
    public static String commands_error = "§cCommands were not loaded.";

    public static List<String> joinMessage = Arrays.asList("","&7Welcome to the server,have a good time","&7make friends and conquer everyone.","");


    public static String chatFormat = "<rank><player> &7>>&r<message>";
    public static boolean plugin_chat = true;
    public static boolean first_join = true;
    public static boolean clear_chat = true;

    public static SoundsAPI evolve_sound = SoundsAPI.LEVEL_UP;
    public static SoundsAPI evolve_global_sound = SoundsAPI.AMBIENCE_THUNDER;
    public static SoundsAPI evolve_error_sound = SoundsAPI.NOTE_BASS;
    public static SoundsAPI reset_sound = SoundsAPI.BAT_TAKEOFF;
    public static SoundsAPI reset_sound_error = SoundsAPI.NOTE_BASS;

    public static void LoadFolders(){
        LoadSettings();
        LoadLang();
    }


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

        rankredefine = convert(Objects.requireNonNull(config.getString("rank-reset")).replace("&","§"));
        rankredefineerror = convert(Objects.requireNonNull(config.getString("rank-reset-error")).replace("&","§"));

    }
    public static void LoadSettings() {
        File file = new File(Main.plugin.getDataFolder(),"settings.yml");
        if(!file.exists()){Main.plugin.saveResource("settings.yml",true);}
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        evolve_sound = Objects.requireNonNull(SoundsAPI.valueOf(config.getString("evolve-sound")));
        evolve_error_sound = Objects.requireNonNull(SoundsAPI.valueOf(config.getString("evolve-error-sound")));
        evolve_global_sound = Objects.requireNonNull(SoundsAPI.valueOf(config.getString("evolve-global-sound")));
        reset_sound = Objects.requireNonNull(SoundsAPI.valueOf(config.getString("reset-sound")));
        reset_sound_error = Objects.requireNonNull(SoundsAPI.valueOf(config.getString("reset-sound-error")));

        chatFormat = convert(Objects.requireNonNull(config.getString("chat-format")).replace("&","§"));
        plugin_chat = config.getBoolean("chat");

        joinMessage = Objects.requireNonNull(convert(config.getStringList("join-message.message")));
        first_join = config.getBoolean("join-message.first-join");
        clear_chat = config.getBoolean("join-message.clear-chat");
    }
    public static String convert(String m){
        return m.replace("&","§");
    }

    public static List<String> convert(List<String> list){
        List<String> nlist = new ArrayList<>();
        for(String s : list){nlist.add(s);}
        return nlist;
    }

}
