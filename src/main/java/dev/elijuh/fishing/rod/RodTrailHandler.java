package dev.elijuh.fishing.rod;

import dev.elijuh.fishing.Core;
import dev.elijuh.fishing.rod.trails.helixes.RainbowDoubleHelixTrail;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author elijuh
 */
public class RodTrailHandler implements Listener {
    @Getter
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @EventHandler
    public void on(ProjectileLaunchEvent e) {
        if (e.getEntityType() == EntityType.FISHING_HOOK) {
            Bukkit.getScheduler().runTask(Core.i(), () -> new RainbowDoubleHelixTrail(e.getEntity()));
        }
    }
}
