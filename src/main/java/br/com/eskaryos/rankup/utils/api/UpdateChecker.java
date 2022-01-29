package br.com.eskaryos.rankup.utils.api;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

import br.com.eskaryos.rankup.Main;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class UpdateChecker {
    private Main plugin;

    private int resourceId;

    public UpdateChecker(Main plugin, int resourceId) {
        this.plugin = plugin;
        this.resourceId = resourceId;
    }

    public void getLatestVersion(Consumer<String> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously((Plugin)this.plugin, () -> {
            try(InputStream inputStream = (new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId)).openStream(); Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext())
                    consumer.accept(scanner.next());
            } catch (IOException exception) {
                this.plugin.getLogger().info("Update checker is broken, can't find an update!" + exception.getMessage());
            }
        });
    }
}
