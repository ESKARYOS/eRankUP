package br.com.eskaryos.rankup.data;

import br.com.eskaryos.rankup.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Objects;

public class Lang {

    public static String last_rank = "§cVocê já atingiu o último rank!";
    public static String evolvedMsg = "§aVocê evoluiu para o rank <rank>";
    public static String evolvedGlobal = "<player> §aevoluiu para o rank <rank>";


    public static void LoadLang(){
        File file = new File(Main.plugin.getDataFolder(),"lang.yml");
        if(!file.exists()){Main.plugin.saveResource("lang.yml",true);}
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        last_rank = convert(Objects.requireNonNull(config.getString("last_rank")));
        evolvedMsg = convert(Objects.requireNonNull(config.getString("evolved-msg")));
        evolvedGlobal = convert(Objects.requireNonNull(config.getString("evolved-global")));

    }

    public static String convert(String m){
        return m.replace("&","§");
    }

}
