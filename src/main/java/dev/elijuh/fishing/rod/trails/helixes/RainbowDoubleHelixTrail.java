package dev.elijuh.fishing.rod.trails.helixes;

import dev.elijuh.fishing.rod.trails.HelixTrail;
import dev.elijuh.fishing.utils.HSVColor;
import org.bukkit.Location;
import org.bukkit.entity.Projectile;
import org.bukkit.util.Vector;
import xyz.xenondevs.particle.ParticleEffect;

import java.awt.*;

/**
 * @author elijuh
 */
public class RainbowDoubleHelixTrail extends HelixTrail {
    private final HSVColor hsv = HSVColor.fromRGB(255, 0, 0);

    public RainbowDoubleHelixTrail(Projectile projectile) {
        super(projectile, 0.5, 30,  1);
    }

    @Override
    public void doEffect(Location location, Vector offset) {
        hsv.progressHue(0.05f);
        Color color = new Color(hsv.toRGB());

        ParticleEffect.REDSTONE.display(location.clone().add(offset), color);
        ParticleEffect.REDSTONE.display(location.clone().subtract(offset), color);
    }
}
