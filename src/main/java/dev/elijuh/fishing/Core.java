package dev.elijuh.fishing;

import dev.elijuh.fishing.animations.impl.sound.impl.FishCatchSoundCommon;
import dev.elijuh.fishing.commands.CommandManager;
import dev.elijuh.fishing.commands.impl.*;
import dev.elijuh.fishing.fish.services.FishService;
import dev.elijuh.fishing.leaderboard.Leaderboard;
import dev.elijuh.fishing.leaderboard.LeaderboardManager;
import dev.elijuh.fishing.listeners.BukkitListener;
import dev.elijuh.fishing.maintenance.MaintenanceManager;
import dev.elijuh.fishing.menu.Menu;
import dev.elijuh.fishing.rod.RodTrailHandler;
import dev.elijuh.fishing.storage.Storage;
import dev.elijuh.fishing.storage.mysql.MySQLStorage;
import dev.elijuh.fishing.user.User;
import dev.elijuh.fishing.user.UserManager;
import dev.elijuh.fishing.utils.Library;
import dev.elijuh.fishing.utils.YamlFile;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;

/**
 * @author elijuh
 */
@Getter
public class Core extends JavaPlugin {
    private static Core instance;

    private YamlFile locations;

    private Storage storage;
    private UserManager userManager;
    private LeaderboardManager leaderboardManager;
    private CommandManager commandManager;
    private MaintenanceManager maintenanceManager;
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
        getConfig().options().copyDefaults(true);
        getConfig().addDefault("maintenance.enabled", true);
        getConfig().addDefault("maintenance.whitelistedNames", new ArrayList<>());
        saveConfig();

        storage = new MySQLStorage();
        fishService = new FishService();

        locations = new YamlFile(new File(getDataFolder(), "locations.yml"));


        Bukkit.getPluginManager().registerEvents(new BukkitListener(), this);
        Bukkit.getPluginManager().registerEvents(new RodTrailHandler(), this);
        Menu.initialize(this);

        userManager = new UserManager();
        leaderboardManager = new LeaderboardManager();
        leaderboardManager.register(
            new Leaderboard("&6Fishes Caught Leaderboard", "fishCaught", 10)
        );

        commandManager = new CommandManager();

        commandManager.register(
            new GiveFishCommand(),
            new MaxRodCommand(),
            new TestCatchSoundCommand(),
            new FishesCommand(),
            new FishMarketCommand(),
            new SetFishingStatCommand(),
            new FishAdminCommand()
        );

        maintenanceManager = new MaintenanceManager();

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
        if (leaderboardManager != null) {
            leaderboardManager.shutdown();
        }

        FishCatchSoundCommon.getExecutor().shutdownNow();
        RodTrailHandler.getScheduler().shutdownNow();

        for (Player p : Bukkit.getOnlinePlayers()) {
            Inventory top = p.getOpenInventory().getTopInventory();
            if (top != null && top.getHolder() instanceof Menu) {
                ((Menu) top.getHolder()).onCloseEvent(new InventoryCloseEvent(p.getOpenInventory()));
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
