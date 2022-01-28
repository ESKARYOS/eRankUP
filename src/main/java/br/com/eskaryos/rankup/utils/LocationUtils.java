package br.com.eskaryos.rankup.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationUtils {

    public static String getLocation(Location loc) {
        World world = loc.getWorld();
        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();
        float yaw = loc.getYaw();
        float pitch = loc.getPitch();
        return world.getName() + ":" + x + ":" + y + ":" + z + ":" + yaw + ":" + pitch;
    }

    public static Location getLocation(String linha) {
        World world = Bukkit.getWorld(linha.split(":")[0]);
        double x = Double.parseDouble(linha.split(":")[1]);
        double y = Double.parseDouble(linha.split(":")[2]);
        double z = Double.parseDouble(linha.split(":")[3]);
        float yaw = Float.parseFloat(linha.split(":")[4]);
        float pitch = Float.parseFloat(linha.split(":")[5]);
        return new Location(world, x, y, z, yaw, pitch);
    }

    public static String getVector(Location l) {
        int x = l.getBlockX();
        int y = l.getBlockY();
        int z = l.getBlockZ();
        return "x:"+x+" y:"+y+" z:"+z;
    }
}
