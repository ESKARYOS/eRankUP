package br.com.eskaryos.rankup.utils.bukkit;


import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorUtils {

    private static final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");

    public static List<String> translateStringColor(List<String> list){
        List<String> nList = new ArrayList<>();
        for(String l : list){nList.add(translateStringColor(l));}
        return nList;
    }

    public static String translateStringColor(String message){
        String msg = ChatColor.translateAlternateColorCodes('&',message);
        if(hasVersion()){
            msg = hex(msg);
        }
        return msg;
    }

    public static String hex(String msg){
        if(hasVersion()){
            Matcher matcher = pattern.matcher(msg);
            while(matcher.find()){
                String color = msg.substring(matcher.start(),matcher.end());
                msg = msg.replace(color, ChatColor.of(color)+"");
                matcher = pattern.matcher(msg);
            }
        }
        return ChatColor.translateAlternateColorCodes('&',msg);
    }

    private static boolean hasVersion(){
        if(Bukkit.getVersion().contains("1.8"))return false;
        if(Bukkit.getVersion().contains("1.9"))return false;
        if(Bukkit.getVersion().contains("1.10"))return false;
        if(Bukkit.getVersion().contains("1.11"))return false;
        if(Bukkit.getVersion().contains("1.12"))return false;
        if(Bukkit.getVersion().contains("1.13"))return false;
        if(Bukkit.getVersion().contains("1.14"))return false;
        return !Bukkit.getVersion().contains("1.15");
    }
}

