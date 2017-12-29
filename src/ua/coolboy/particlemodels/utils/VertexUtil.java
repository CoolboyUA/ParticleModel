package ua.coolboy.particlemodels.utils;

import com.owens.oobjloader.builder.VertexGeometric;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import ua.coolboy.particlemodels.ParticleModels;
import ua.coolboy.particlemodels.drawer.Plane;

public final class VertexUtil {

    public static List<Vector> connectPoints(Vector one, Vector two, /*float scale,*/ boolean optimize) {
        List<Vector> vectors = new ArrayList<>();
        int step = (int) (one.distance(two) / ParticleModels.MIN_DISTANCE);
        for (int i = 0; i < step; i++) {
            float percent = (float) i / step;
            vectors.add(one.clone().add(two.clone().subtract(one).multiply(percent)));//.multiply(scale));
        }
        //Bukkit.getLogger().info(vectors.toString());
        if (!optimize) {
            return vectors;
        } else {
            return vectors;//optimize(vectors);
        }
    }

    public static List<Vector> optimize(List<Vector> points, int iterations) {
        int optimized = 0;
        for (int n = 0; n < iterations; n++) {
            for (int i = 0; i < points.size() - 1; i++) {
                if (points.get(i).distance(points.get(i + 1)) < ParticleModels.MIN_DISTANCE) {
                    points.remove(i + 1);
                    optimized++;
                }
            }
        }
        LogUtil.info("optimized: " + optimized);
        return points;
    }

    public static List<Vector> cutSide(Plane.Side side, int count) {
        List<Vector> vectors = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            float percent = (float) i / count;
            vectors.add(side.getFirstVector().clone().add(side.getSecondVector().clone().subtract(side.getFirstVector()).multiply(percent)));//.multiply(scale));
        }
        return vectors;
    }

    public static List<Vector> connectPoints(VertexGeometric one, VertexGeometric two, /*float scale,*/ boolean optimize) {
        return connectPoints(toVector(one), toVector(two),/* scale,*/ optimize);
    }

    public static Vector toVector(VertexGeometric ver) {
        return new Vector(ver.x, ver.y, ver.z);
    }

    public static Vector rotate(Vector vector, EulerAngle angle) {
        Rotation rotation = new Rotation(angle.getX(), angle.getY(), angle.getZ());
        return rotation.applyTo(vector);
    }

    
    public static Vector orthogonal(Vector vector) {
        double threshold = 0.6 * vector.length();
        double x = vector.getX();
        double y = vector.getY();
        double z = vector.getZ();
        
        if (threshold == 0) {
            return vector.zero();
        }

        if (Math.abs(x) <= threshold) {
            double inverse  = 1 / Math.sqrt(y * y + z * z);
            return new Vector(0, inverse * z, -inverse * y);
        } else if (Math.abs(y) <= threshold) {
            double inverse  = 1 / Math.sqrt(x * x + z * z);
            return new Vector(-inverse * z, 0, inverse * x);
        }
        double inverse  = 1 / Math.sqrt(x * x + y * y);
        return new Vector(inverse * y, -inverse * x, 0);
    }
}
