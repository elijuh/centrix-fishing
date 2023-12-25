package dev.elijuh.fishing.tasks;

import dev.elijuh.fishing.Core;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

/**
 * @author elijuh
 */
@Getter
@RequiredArgsConstructor
public class MoveItemToPlayerTask extends BukkitRunnable {

    private final Player player;
    private final Item item;
    private final double magnitude;

    @Override
    public void run() {
        if (!player.isOnline() || item.isDead() || player.isDead() || player.getWorld() != item.getWorld()) {
            cancel();
            return;
        }

        Vector pv = player.getEyeLocation().toVector();
        Vector iv = item.getLocation().toVector();
        double dist = pv.distance(iv);
        Vector direction = pv.clone().subtract(iv).normalize().multiply(Math.min(dist, magnitude));
        item.setVelocity(direction);

        if (dist < 1.0) {
            cancel();
        }
    }

    public void start() {
        runTaskTimer(Core.i(), 0L, 2L);
    }
}
