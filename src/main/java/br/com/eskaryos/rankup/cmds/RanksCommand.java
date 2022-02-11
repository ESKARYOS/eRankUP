package br.com.eskaryos.rankup.cmds;


import br.com.eskaryos.rankup.menu.RankMenu;
import br.com.eskaryos.rankup.utils.bukkit.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RanksCommand extends Command {

    public RanksCommand() {
        super("ranks");
        try {
            SimpleCommandMap simpleCommandMap = (SimpleCommandMap) Bukkit.getServer().getClass()
                    .getDeclaredMethod("getCommandMap").invoke(Bukkit.getServer());
            simpleCommandMap.register(this.getName(), "eRankUP", this);
        } catch (ReflectiveOperationException ex) {
            Logger.log(Logger.LogLevel.ERROR, "Could not register command: " + ex);
        }
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String s, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            RankMenu.rankMenu(p,1);
        }

        return false;
    }
}
