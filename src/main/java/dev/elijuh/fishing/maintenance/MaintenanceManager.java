package dev.elijuh.fishing.maintenance;

import dev.elijuh.fishing.Core;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * @author elijuh
 */
public class MaintenanceManager {
    private final Set<String> whitelistedNames = new HashSet<>();
    @Getter
    private boolean enabled;

    public MaintenanceManager() {
        this.enabled = Core.i().getConfig().getBoolean("maintenance.enabled");
        whitelistedNames.addAll(Core.i().getConfig().getStringList("maintenance.whitelistedNames"));
    }

    public void whitelist(String name) {
        whitelistedNames.add(name);
        Core.i().getConfig().set("maintenance.whitelistedNames", new ArrayList<>(whitelistedNames));
        Core.i().saveConfig();
    }

    public void unwhitelist(String name) {
        whitelistedNames.remove(name);
        Core.i().getConfig().set("maintenance.whitelistedNames", new ArrayList<>(whitelistedNames));
        Core.i().saveConfig();
    }

    public boolean allows(Player p) {
        return !this.enabled || whitelistedNames.contains(p.getName());
    }

    public void toggle() {
        this.enabled = !this.enabled;
        Core.i().getConfig().set("maintenance.enabled", this.enabled);
        Core.i().saveConfig();
    }
}
