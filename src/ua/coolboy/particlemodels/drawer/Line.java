package ua.coolboy.particlemodels.drawer;

import com.owens.oobjloader.builder.VertexGeometric;
import java.util.List;
import java.util.Objects;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import ua.coolboy.particlemodels.utils.VertexUtil;

public class Line implements Drawable {
    //Currently not used
    private Vector one;
    private Vector two;
    private List<Vector> points;
    private Drawer drawer;

    public Line(Drawer drawer,VertexGeometric one, VertexGeometric two) {
        this(drawer,VertexUtil.toVector(one), VertexUtil.toVector(two));
    }

    public Line(Drawer drawer, Vector one, Vector two) {
        this(drawer,one, two, true);
    }

    public Line(Drawer drawer, Vector one, Vector two, boolean optimize) {
        this.one = one;
        this.two = two;
        this.drawer = drawer;
        points = VertexUtil.connectPoints(one, two, optimize);
    }

    public Vector getStartPoint() {
        return one;
    }

    public Vector getFinishPoint() {
        return two;
    }
    
    public Void call() {
        draw(drawer.getLocation());
        return null;
    }
    
    @Override
    public void draw(Location location) {
        if (location == null) {
            return;
        }
        for (Vector vector : points) {
            location.getWorld().spawnParticle(Particle.REDSTONE, location.clone().add(vector), 0, Float.MIN_VALUE, 1, 0.5, 1);
        }
    }

    @Override
    public Drawable scale(float scale) {
        for (Vector vector : points) {
            vector.multiply(scale);
        }
        return this;
    }
    
    @Override
    public void rotate(EulerAngle angle) {
        for(Vector vec : points) {
            vec.copy(VertexUtil.rotate(vec, angle));
        }
    }
    
    @Override
    public Drawer getDrawer() {
        return drawer;
    }

    @Override
    public List<Vector> getPoints() {
        return points;
    }
    
    @Override
    public void setPoints(List<Vector> points) {
        this.points = points;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.one);
        hash = 53 * hash + Objects.hashCode(this.two);
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Line) {
            Line line = (Line) o;
            return one.equals(line.getStartPoint()) && two.equals(line.getFinishPoint())
                    || one.equals(line.getFinishPoint()) && two.equals(line.getStartPoint());
        } else {
            return false;
        }
    }

}
