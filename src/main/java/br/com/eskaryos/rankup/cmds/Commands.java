package br.com.eskaryos.rankup.cmds;

import br.com.eskaryos.rankup.Main;
import br.com.eskaryos.rankup.data.Lang;
import br.com.eskaryos.rankup.utils.Logger;

public class Commands {

    public static void setupCommands() {
        try{
            Main m = Main.plugin;
            new RankUp();
            new Reset();
            Logger.log(Logger.LogLevel.INFO,Lang.commands_sucess);
        }catch (Exception e){
            Logger.log(Logger.LogLevel.ERROR, Lang.commands_error);
        }
    }
}
