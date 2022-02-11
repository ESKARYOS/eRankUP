package br.com.eskaryos.rankup.cmds;


import br.com.eskaryos.rankup.data.DataMain;
import br.com.eskaryos.rankup.data.Lang;
import br.com.eskaryos.rankup.ranks.Rank;
import br.com.eskaryos.rankup.ranks.RankMain;
import br.com.eskaryos.rankup.utils.bukkit.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ForceEvolve extends Command {

    public ForceEvolve() {
        super("forcerank");
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
            if(p.hasPermission("rankup.forcerank")){
                if(args.length>0){
                    String subc = args[0];
                    if(RankMain.getRankByName(subc)!=null){
                        Rank evolve = RankMain.getRankByName(subc);
                        DataMain.getProfile(p.getUniqueId()).setRank(RankMain.clone(evolve));
                        if(evolve.getOrder()<RankMain.getFinalRank().getOrder()){
                            DataMain.getProfile(p.getUniqueId()).setNext(RankMain.getRankById(evolve.getOrder()+1));
                        }
                        evolve.sendEvolveMessage(p);
                        evolve.sendAllEvolveMessage(p);
                        evolve.sendEvolveSound(p);
                        evolve.sendAllEvolveSound();
                        evolve.sendEvolveBar(p);
                        evolve.sendEvolveBarAll(p);
                        evolve.sendEvolveTitle(p);

                        evolve.executeCommand(p);
                    }else{
                        p.sendMessage(Lang.wrong_rank.replace("<rank>",args[0]));
                    }
                }else{
                    p.sendMessage(Lang.command_forcerank);
                }
            }else{
                p.sendMessage(Lang.no_permission);
            }
        }

        return false;
    }
}
