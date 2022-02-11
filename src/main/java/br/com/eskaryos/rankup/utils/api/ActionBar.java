package br.com.eskaryos.rankup.utils.api;

import java.lang.reflect.Constructor;

import br.com.eskaryos.rankup.utils.bukkit.ColorUtils;
import br.com.eskaryos.rankup.utils.bukkit.JavaUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ActionBar {
    public static void sendPacket(Player player, Object packet) {
        try {
            Object handle = player.getClass().getMethod("getHandle", new Class[0]).invoke(player, new Object[0]);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", new Class[] { getNMSClass("Packet") }).invoke(playerConnection, new Object[] { packet });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Class<?> getNMSClass(String name) {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void sendTitle(Player player, String title, String subtitle, int fadein, int stay, int fadeout) {
        if (JavaUtils.getVersion().contains("1_8") || JavaUtils.getVersion().contains("1_9") ||
                JavaUtils.getVersion().contains("1_11")) {
            title = ChatColor.translateAlternateColorCodes('&', title);
            subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);
            Class<?> chatSerializer = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0];
            Class<?> chatComponent = getNMSClass("IChatBaseComponent");
            Class<?> packetTitle = getNMSClass("PacketPlayOutTitle");
            try {
                Object enumTitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);
                Constructor<?> constructorTitle = packetTitle.getDeclaredConstructor(new Class[] { packetTitle.getDeclaredClasses()[0], chatComponent, int.class, int.class, int.class });
                Object chatTitle = chatSerializer.getMethod("a", new Class[] { String.class }).invoke(chatSerializer, new Object[] { "{\"text\": \"" + title + "\"}" });
                Object packet = constructorTitle.newInstance(new Object[] { enumTitle, chatTitle, Integer.valueOf(fadein), Integer.valueOf(stay), Integer.valueOf(fadeout) });
                sendPacket(player, packet);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                Object enumSubtitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null);
                Constructor<?> constructorSubtitle = packetTitle.getDeclaredConstructor(new Class[] { packetTitle.getDeclaredClasses()[0], chatComponent, int.class, int.class, int.class });
                Object chatSubtitle = chatSerializer.getMethod("a", new Class[] { String.class }).invoke(chatSerializer, new Object[] { "{\"text\": \"" + subtitle + "\"}" });
                Object packet = constructorSubtitle.newInstance(new Object[] { enumSubtitle, chatSubtitle, Integer.valueOf(fadein), Integer.valueOf(stay), Integer.valueOf(fadeout) });
                sendPacket(player, packet);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            player.sendTitle(title, subtitle, fadein, stay, fadeout);
        }
    }

    public static void sendActionBar(Player player, String message) {
        if (JavaUtils.getVersion().contains("1_17")) {
            TextComponent text_component = new TextComponent(message);
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, (BaseComponent)text_component);
            return;
        }
        if (!JavaUtils.isAbove1_8()) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
            return;
        }
        if (JavaUtils.getVersion().contains("1_13") || JavaUtils.getVersion().contains("1_12")) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
            return;
        }
        message = ChatColor.translateAlternateColorCodes('&', message);
        Class<?> chatSerializer = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0];
        Class<?> chatComponent = getNMSClass("IChatBaseComponent");
        Class<?> packetActionbar = getNMSClass("PacketPlayOutChat");
        try {
            Constructor<?> ConstructorActionbar = packetActionbar.getDeclaredConstructor(new Class[] { chatComponent, byte.class });
            Object actionbar = chatSerializer.getMethod("a", new Class[] { String.class }).invoke(chatSerializer, new Object[] { "{\"text\": \"" + message + "\"}" });
            Object packet = ConstructorActionbar.newInstance(new Object[] { actionbar, Byte.valueOf((byte)2) });
            sendPacket(player, packet);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
