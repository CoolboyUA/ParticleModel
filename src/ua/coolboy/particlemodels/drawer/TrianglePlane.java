package ua.coolboy.particlemodels.drawer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import ua.coolboy.particlemodels.ParticleModels;
import ua.coolboy.particlemodels.utils.VertexUtil;

public class TrianglePlane extends Plane implements Drawable {

    Vector one;
    Vector two;
    Vector three;

    Side sone;
    Side stwo;
    Side sthree;
    
    
    List<Line> lines;
    List<Vector> points;
    Drawer drawer;

    public TrianglePlane(Drawer drawer, List<Vector> vertices) {
        if (vertices.size() != 3) {
            throw new UnsupportedOperationException("Only 3 vertices supported!");
        }
        one = vertices.get(0);
        two = vertices.get(1);
        three = vertices.get(2);
        
        this.drawer = drawer;
        this.points = new ArrayList<>();
        this.lines = new ArrayList<>();

        createSides();
        calculatePoints();
    }

    public TrianglePlane(Drawer drawer, Vector one, Vector two, Vector three) {
        this.one = one;
        this.two = two;
        this.three = three;

        this.points = new ArrayList<>();
        this.drawer = drawer;

        createSides();
        calculatePoints();
    }

    private void createSides() {
        sone = new Side(one, two);
        stwo = new Side(two, three);
        sthree = new Side(three, one);
    }

    private void calculatePoints() {
        List<Side> sides = getSortedSides();
        Side first = sides.get(0);
        Side second = sides.get(1);
        List<Vector> f = VertexUtil.cutSide(first, optimizeIterations(first.size()));
        List<Vector> s = VertexUtil.cutSide(second, optimizeIterations(first.size()));
        Collections.reverse(s);
        //Bukkit.getLogger().info("OptiSize: " + optimizeIterations(first.size()));
        for (int i = 0; i < f.size(); i++) {
            lines.add(new Line(drawer, f.get(i),s.get(i)));
        }
    }

    private int optimizeIterations(double distance) {
        return (int) Math.round(distance / ParticleModels.MIN_DISTANCE) + 1;
    }

    @Override
    public void rotate(EulerAngle angle) {
        for(Vector vec : points) {
            vec.copy(VertexUtil.rotate(vec, angle));
        }
    }

    private List<Side> getSortedSides() {
        List<Side> list = Arrays.asList(
                new Side(one, two),
                new Side(two, three),
                new Side(three, one)
        );
        Collections.sort(list);
        return list;
    }

    @Override
    public List<Vector> getVertices() {
        return Arrays.asList(one, two, three);
    }

    @Override
    public List<Vector> getPoints() {
        points.clear();
        for(Line line: lines) {
            points.addAll(line.getPoints());
        }
        return points;
    }
    
    @Override
    public void setPoints(List<Vector> points) {
        this.points = points;
    }
    
    @Override
    public Drawer getDrawer() {
        return drawer;
    }
    
    @Override
    public Void call() {
        draw(drawer.getLocation());
        return null;
    }
    
    @Override
    public void draw(Location location) {
        if (location == null) {
            return;
        }
        //Collections.shuffle(points);
        for (Vector vector : getPoints()) {
            //try {
            location.getWorld().spawnParticle(Particle.REDSTONE, location.clone().add(vector), 0, Float.MIN_VALUE, 1, 0.5, 1);
            /*} catch (NullPointerException ex) {
                //Bukkit.getLogger().info("null");
                
            }*/
        }
    }

    @Override
    public Drawable scale(float scale) {
        for (Vector vector : points) {
            vector.multiply(scale);
        }
        return this;

    }

}
