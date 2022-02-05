package br.com.eskaryos.rankup.cmds;


import br.com.eskaryos.rankup.Main;
import br.com.eskaryos.rankup.menu.RankMenu;
import br.com.eskaryos.rankup.utils.bukkit.ItemUtils;
import br.com.eskaryos.rankup.utils.bukkit.JavaUtils;
import br.com.eskaryos.rankup.utils.bukkit.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.yaml.snakeyaml.Yaml;

import java.io.File;

public class RankUp extends Command {

    public RankUp() {
        super("rankup");

        try {
            SimpleCommandMap simpleCommandMap = (SimpleCommandMap) Bukkit.getServer().getClass()
                    .getDeclaredMethod("getCommandMap").invoke(Bukkit.getServer());
            simpleCommandMap.register(this.getName(), "eRankUP", this);
        } catch (ReflectiveOperationException ex) {
            Logger.log(Logger.LogLevel.ERROR, "Could not register command: " + ex);
        }
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            RankMenu.rankMenu(p,1);
        }

        return false;
    }
}
