package dev.elijuh.fishing;

import com.google.common.collect.ImmutableSet;
import dev.elijuh.fishing.animations.impl.sound.impl.FishCatchSoundCommon;
import dev.elijuh.fishing.commands.CommandManager;
import dev.elijuh.fishing.commands.impl.*;
import dev.elijuh.fishing.fish.services.FishService;
import dev.elijuh.fishing.listeners.BukkitListener;
import dev.elijuh.fishing.menu.Menu;
import dev.elijuh.fishing.storage.Storage;
import dev.elijuh.fishing.storage.mysql.MySQLStorage;
import dev.elijuh.fishing.user.User;
import dev.elijuh.fishing.user.UserManager;
import dev.elijuh.fishing.utils.Library;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

/**
 * @author elijuh
 */
@Getter
public class Core extends JavaPlugin {
    @Getter
    private static final Set<String> testingWhitelist = ImmutableSet.of(
        "elijuh", "BrotherHate"
    );

    private static Core instance;

    private Storage storage;
    private UserManager userManager;
    private CommandManager commandManager;
    private FishService fishService;

    @Override
    public void onLoad() {
        instance = this;
        for (Library lib : Library.values()) {
            lib.load();
        }
    }

    @Override
    public void onEnable() {
        storage = new MySQLStorage();
        fishService = new FishService();

        Bukkit.getPluginManager().registerEvents(new BukkitListener(), this);
        Bukkit.getPluginManager().registerEvents(new Menu.Handler(), this);

        userManager = new UserManager();
        commandManager = new CommandManager();

        commandManager.register(
            new GiveFishCommand(),
            new MaxRodCommand(),
            new TestCatchSoundCommand(),
            new FishesCommand(),
            new FishMarketCommand()
        );

        //static initialization
        FishCatchSoundCommon.getExecutor();
    }

    @Override
    public void onDisable() {
        commandManager.unregisterAll();
        if (userManager != null) {
            userManager.shutdown();
        }
        if (storage != null) {
            storage.shutdown();
        }
        FishCatchSoundCommon.getExecutor().shutdownNow();

        for (Player p : Bukkit.getOnlinePlayers()) {
            Inventory top = p.getOpenInventory().getTopInventory();
            if (top != null && top.getHolder() instanceof Menu) {
                p.closeInventory();
            }
        }
    }

    public User getUser(Player player) {
        return userManager.getUser(player);
    }

    public static Core i() {
        return instance;
    }
}
