package br.com.eskaryos.rankup.cmds;

import br.com.eskaryos.rankup.Main;
import br.com.eskaryos.rankup.data.DataMain;
import br.com.eskaryos.rankup.data.Profile;
import br.com.eskaryos.rankup.menu.RankMenu;
import br.com.eskaryos.rankup.ranks.Rank;
import br.com.eskaryos.rankup.ranks.RankMain;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RankCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            if(args.length>0){
                String subcomando = args[0];
                if(subcomando.equalsIgnoreCase("reset") ||
                subcomando.equalsIgnoreCase("reload")){
                    Main.plugin.getServer().getPluginManager().disablePlugin(Main.plugin);
                    Main.plugin.getServer().getPluginManager().enablePlugin(Main.plugin);
                    p.sendMessage("§aPlugin reloaded.");
                }
            }else{
               try{
                   Profile profile = DataMain.getProfile(p.getUniqueId());
                   Rank next = profile.getNext();
                   if(next==null)return true;
                   if(RankMain.isLastRank(p))return true;
                   RankMenu.menu(next,p);
               }catch (Exception e){}
            }
        }
        return false;
    }
}
