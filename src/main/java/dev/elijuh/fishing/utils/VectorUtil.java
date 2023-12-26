package dev.elijuh.fishing.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.util.Vector;

/**
 * @author elijuh
 */
@UtilityClass
public class VectorUtil {

    public void rotateByAngleX(Vector vector, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double y = cos * vector.getY() - sin * vector.getZ();
        double z = sin * vector.getY() + cos * vector.getZ();
        vector.setY(y).setZ(z);
    }

    public void rotateByAngleY(Vector vector, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double x = cos * vector.getX() + sin * vector.getZ();
        double z = -sin * vector.getX() + cos * vector.getZ();
        vector.setX(x).setZ(z);
    }
}