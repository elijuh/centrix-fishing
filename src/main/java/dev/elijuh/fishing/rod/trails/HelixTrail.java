package dev.elijuh.fishing.rod.trails;

import dev.elijuh.fishing.rod.RodTrailHandler;
import dev.elijuh.fishing.utils.VectorUtil;
import org.bukkit.Location;
import org.bukkit.entity.Projectile;
import org.bukkit.util.Vector;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author elijuh
 */
public abstract class HelixTrail implements Runnable {
    private final Projectile projectile;
    private final double radius, theta;
    private final int perTick;
    private final ScheduledFuture<?> scheduled;

    public HelixTrail(Projectile projectile, double radius, int particlesPerRotation, int perTick) {
        this.projectile = projectile;
        this.radius = radius;
        this.theta = Math.PI * 2 / particlesPerRotation;
        this.perTick = perTick;
        scheduled = RodTrailHandler.getScheduler().scheduleAtFixedRate(this, 0L, 50L, TimeUnit.MILLISECONDS);
        lastLoc = projectile.getLocation().setDirection(projectile.getVelocity());
    }

    private int counter;
    private Location lastLoc;

    @Override
    public void run() {
        if (projectile.isDead() || projectile.isOnGround()) {
            scheduled.cancel(false);
            return;
        }

        Location loc = projectile.getLocation();
        loc.setDirection(projectile.getVelocity());

        Location add = loc.clone().subtract(lastLoc);

        for (int i = 0; i < perTick; i++) {
            Location l = lastLoc.clone();
            l.add(add.clone().multiply(1.0 / perTick * i));

            double x = radius * Math.cos(theta * counter);
            double y = radius * Math.sin(theta * counter);

            Vector vec = new Vector(x, y, 0);
            VectorUtil.rotateByAngleX(vec, Math.toRadians(loc.getPitch()));
            VectorUtil.rotateByAngleY(vec, Math.toRadians(-loc.getYaw()));

            doEffect(l, vec);
            counter++;
        }
        this.lastLoc = loc;
    }

    public abstract void doEffect(Location location, Vector offset);
}
