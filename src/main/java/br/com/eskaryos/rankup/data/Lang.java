package br.com.eskaryos.rankup.data;

import br.com.eskaryos.rankup.Main;
import br.com.eskaryos.rankup.utils.api.BannerCreator;
import br.com.eskaryos.rankup.utils.api.PatternTypes;
import br.com.eskaryos.rankup.utils.api.SoundsAPI;
import br.com.eskaryos.rankup.utils.bukkit.JavaUtils;
import br.com.eskaryos.rankup.utils.bukkit.Logger;
import br.com.eskaryos.rankup.utils.bukkit.Utils;
import org.bukkit.DyeColor;
import org.bukkit.Sound;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;

public class Lang extends Utils {

    public static String last_rank = "§cYou have already reached the last rank!";
    public static String evolvedMsg = "§aYou evolved to rank <rank>";
    public static String evolvedGlobal = "<player> §aevolved to rank <rank>";
    public static String rankredefine = "§aYour rank has been reset to rank <rank>";
    public static String rankredefineerror = "§cThere was a problem trying to reset your rank.";
    public static String invalidMenu = "§cThis menu does not exist, please try again.";
    public static String cantJump = "§cYou cannot evolve to a higher rank, evolve for <next> first.";
    public static String downgrade = "§cYou cannot downgrade your rank.";
    public static String evolveError = "§cYou cannot evolve to this rank.";
    public static String requirementError = "§cYou need to complete the requirement <requirement> to evolve.";

    public static String wrong_rank = "<rank> &crank does not exist.";
    public static String command_forcerank = "&cUse /force rank <rank>";
    public static String no_permission = "&cNo permission to use this.";


    public static String SQLiteSuccess = "§aSQLite connection completed.";
    public static String SQLiteError = "§cCould not start SQLite.";
    public static String TableCreated = "§aSQLite table created.";
    public static String TableNoCreated = "§cSQLite table not created.";
    public static String PlayerNoLoaded = "§cThe player §c<player> was not loaded.";


    public static String events_sucess = "§aAll events were successfully loaded.";
    public static String events_error = "§cEvents were not loaded.";
    public static String lastRankVariable = "§clast rank";

    public static String commands_sucess = "§aAll commands were successfully loaded.";
    public static String commands_error = "§cCommands were not loaded.";

    public static List<String> joinMessage = Arrays.asList("","&7Welcome to the server,have a good time","&7make friends and conquer everyone.","");


    public static String chatFormat = "<rank><player> &7>>&r<message>";
    public static boolean plugin_chat = true;
    public static boolean first_join = true;
    public static boolean clear_chat = true;

    public static int barsize = 10;
    public static String bar = "■";
    public static String color1 = "&a";
    public static String color2 = "&7";

    public static Sound reset_sound = getSound("BAT_TAKEOFF");
    public static Sound reset_sound_error = getSound("NOTE_BASS");

    public static Map<String, BannerCreator> banners = new HashMap<>();

    public static void LoadFolders(){
        LoadSettings();
        LoadLang();
        LoadBanners();
        File file = new File(Main.plugin.getDataFolder(),"help.yml");
        if(!file.exists()){Main.plugin.saveResource("help.yml",true);}
    }

    public static void LoadBanners(){
        File file = new File(Main.plugin.getDataFolder(),"banners.yml");
        if(!file.exists()){Main.plugin.saveResource("banners.yml",true);}
        FileConfiguration config = JavaUtils.loadConfigUTF8(file);
        for(String key : Objects.requireNonNull(config.getConfigurationSection("Banners")).getKeys(false)){
            try{
                DyeColor color = DyeColor.valueOf(Objects.requireNonNull(config.getString("Banners." + key + ".BaseColor")).toUpperCase(Locale.ROOT));
                List<Pattern> patternList = new ArrayList<>();
                for(String pattern: config.getStringList("Banners."+key+".Patterns")){
                    DyeColor c = DyeColor.valueOf(pattern.split(":")[0].toUpperCase(Locale.ROOT));
                    PatternType p = PatternTypes.getTypeByName(pattern.split(":")[1]);
                    assert p != null;
                    patternList.add(new Pattern(c,p));
                }
                BannerCreator banner = new BannerCreator(color,patternList);
                banners.put(key,banner);
                Logger.log(Logger.LogLevel.INFO,"&aThe &f"+key+"&a banner has been successfully loaded.");
            }catch (Exception e){
                Logger.log(Logger.LogLevel.INFO,"&cUnable to load banners: " + e.getMessage());
            }
        }
    }
    public static void LoadLang(){
        File file = new File(Main.plugin.getDataFolder(),"lang.yml");
        if(!file.exists()){
            Main.plugin.saveResource("lang.yml",true);
        }
        FileConfiguration config = JavaUtils.loadConfigUTF8(file);
        last_rank = color(Objects.requireNonNull(config.getString("last_rank")));
        lastRankVariable = color(Objects.requireNonNull(config.getString("last_rank_var")));
        evolvedMsg = color(Objects.requireNonNull(config.getString("evolved-msg")));
        evolvedGlobal = color(Objects.requireNonNull(config.getString("evolved-global")));
        invalidMenu = color(Objects.requireNonNull(config.getString("invalid-menu")));
        cantJump = color(Objects.requireNonNull(config.getString("cant-jump")));
        downgrade = color(Objects.requireNonNull(config.getString("downgrade")));
        evolveError = color(Objects.requireNonNull(config.getString("evolve-error")));
        requirementError = color(Objects.requireNonNull(config.getString("requirement-error")));

        SQLiteSuccess = color(Objects.requireNonNull(config.getString("sqlite-sucess")));
        SQLiteError = color(Objects.requireNonNull(config.getString("sqlite-error")));
        TableCreated = color(Objects.requireNonNull(config.getString("table-created")));
        TableNoCreated = color(Objects.requireNonNull(config.getString("table-error")));
        PlayerNoLoaded = color(Objects.requireNonNull(config.getString("player-noloaded")));
        events_sucess = color(Objects.requireNonNull(config.getString("events_sucess")));
        events_error = color(Objects.requireNonNull(config.getString("events_error")));
        commands_sucess = color(Objects.requireNonNull(config.getString("commands_sucess")));
        commands_error = color(Objects.requireNonNull(config.getString("commands_error")));
        wrong_rank = color(Objects.requireNonNull(config.getString("wrong_rank")));
        no_permission = color(Objects.requireNonNull(config.getString("no-permission")));
        command_forcerank = color(Objects.requireNonNull(config.getString("command_forcerank")));

        rankredefine = color(Objects.requireNonNull(config.getString("rank-reset")).replace("&","§"));
        rankredefineerror = color(Objects.requireNonNull(config.getString("rank-reset-error")).replace("&","§"));

    }
    public static void LoadSettings() {
        File file = new File(Main.plugin.getDataFolder(),"settings.yml");
        if(!file.exists()){Main.plugin.saveResource("settings.yml",true);}
        YamlConfiguration config = JavaUtils.loadConfigUTF8(file);

        reset_sound = getSound(Objects.requireNonNull(config.getString("reset-sound")).toUpperCase(Locale.ROOT));
        reset_sound_error = getSound(config.getString("reset-sound-error"));

        chatFormat = color(Objects.requireNonNull(config.getString("chat-format")).replace("&","§"));
        plugin_chat = config.getBoolean("chat");

        joinMessage = Objects.requireNonNull(color(config.getStringList("join-message.message")));
        first_join = config.getBoolean("join-message.first-join");
        clear_chat = config.getBoolean("join-message.clear-chat");

        barsize = config.getInt("progress-bar.size");
        bar = color(config.getString("progress-bar.bar"));
        color1 = color(config.getString("progress-bar.color-1"));
        color2 = color(config.getString("progress-bar.color-2"));

        if(config.getBoolean("MySQL.Activated")){
            String host = config.getString("MySQL.Host");
            String base = config.getString("MySQL.Database");
            String user = config.getString("MySQL.User");
            String pass = config.getString("MySQL.Password");
            String port = config.getString("MySQL.Port");
            DataMain.mySQL = new MySQL(host,base,user,pass,port,"rankup");
        }

    }


}
