package br.com.eskaryos.rankup.cmds;

import br.com.eskaryos.rankup.Main;
import br.com.eskaryos.rankup.data.Lang;
import br.com.eskaryos.rankup.utils.bukkit.Logger;

import java.util.Objects;

public class Commands {

    public static void setupCommands() {
        try{
            Main m = Main.plugin;
            new RanksCommand();
            new Reset();
            new ForceEvolve();
            Objects.requireNonNull(m.getCommand("rankup")).setExecutor(new RankCommand());
            Logger.log(Logger.LogLevel.INFO,Lang.commands_sucess);
        }catch (Exception e){
            Logger.log(Logger.LogLevel.ERROR, Lang.commands_error);
        }
    }
}
