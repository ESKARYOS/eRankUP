package br.com.eskaryos.rankup.cmds;


import br.com.eskaryos.rankup.data.DataMain;
import br.com.eskaryos.rankup.data.Lang;
import br.com.eskaryos.rankup.ranks.RankMain;
import br.com.eskaryos.rankup.utils.JavaUtils;
import br.com.eskaryos.rankup.utils.Logger;
import br.com.eskaryos.rankup.utils.api.RankHolder;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;

public class Reset extends Command {

    public Reset() {
        super("reset");
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
            if(DataMain.getProfile(p.getUniqueId()).getRank()!=null && DataMain.getProfile(p.getUniqueId()).getRank().getOrder()<=0){
                p.sendMessage(Lang.rankredefineerror);
                JavaUtils.playSound(p,Lang.reset_sound_error,1F,1F);
                return true;
            }
            DataMain.getProfile(p.getUniqueId()).setRank(RankMain.clone(RankMain.getDefaultRank()));
            JavaUtils.playSound(p,Lang.reset_sound,1F,1F);
            p.sendMessage(RankHolder.hook(p, Lang.rankredefine));
        }

        return false;
    }
}
