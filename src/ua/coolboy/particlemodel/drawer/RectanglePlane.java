package ua.coolboy.particlemodel.drawer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import ua.coolboy.particlemodel.ParticleModel;
import ua.coolboy.particlemodel.utils.VertexUtil;

public class RectanglePlane extends Plane implements Drawable {

    Vector one;
    Vector two;
    Vector three;
    Vector four;

    Side sone;
    Side stwo;
    Side sthree;
    Side sfour;

    List<Vector> points;
    Drawer drawer;
    
    public RectanglePlane(Drawer drawer, List<Vector> vertices) {
        if (vertices.size() != 4) {
            throw new UnsupportedOperationException("Only 4 vertices supported!");
        }
        one = vertices.get(0);
        two = vertices.get(1);
        three = vertices.get(2);
        four = vertices.get(3);
        
        this.drawer = drawer;
        points = new ArrayList<>();
        
        createSides();
        calculatePoints();
    }

    public RectanglePlane(Drawer drawer, Vector one, Vector two, Vector three, Vector four) {
        this.one = one;
        this.two = two;
        this.three = three;
        this.four = four;
        
        this.drawer = drawer;
        points = new ArrayList<>();
        
        createSides();
        calculatePoints();
    }

    private void createSides() {
        sone = new Side(one, two);
        stwo = new Side(two, three);
        sthree = new Side(three, four);
        sfour = new Side(four, one);
    }

    private void calculatePoints() {
        Side max = getMaxSide();
        List<Vector> o = VertexUtil.cutSide(sone, optimizeIterations(max.size()));
        List<Vector> t = VertexUtil.cutSide(sthree, optimizeIterations(max.size()));
        for (int i = 0; i < o.size(); i++) {
            points.addAll(VertexUtil.connectPoints(o.get(i), t.get(i), false));
        }
    }

    private int optimizeIterations(double distance) {
        return (int) Math.round(distance / ParticleModel.MIN_DISTANCE);
    }
    
    @Override
    public void rotate(EulerAngle angle) {
        for(Vector vec : points) {
            vec.copy(VertexUtil.rotate(vec, angle));
        }
    }
    
    @Override
    public Void call() {
        return null;
    }
    
    private Side getMaxSide() {
        double max = Math.max(Math.max(sone.size(), stwo.size()),Math.max(sthree.size(), sfour.size()));
        if(max == sone.size()) return sone;
        else if(max == stwo.size()) return stwo;
        else if(max == sthree.size()) return sthree;
        else return sfour;
    }
    
    @Override
    public List<Vector> getVertices() {
        return Arrays.asList(one, two, three, four);
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
    public Drawer getDrawer() {
        return drawer;
    }

}
