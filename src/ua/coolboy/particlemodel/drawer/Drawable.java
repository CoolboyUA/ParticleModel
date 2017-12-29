package ua.coolboy.particlemodel.drawer;

import java.util.List;
import java.util.concurrent.Callable;
import org.bukkit.Location;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

public interface Drawable extends Callable<Void> {

    public abstract void draw(Location location);
    public abstract Drawable scale(float scale);
    public abstract List<Vector> getPoints();
    public abstract void setPoints(List<Vector> points);
    public abstract void rotate(EulerAngle angle);
    public abstract Drawer getDrawer();
    @Override
    public abstract Void call();

}
