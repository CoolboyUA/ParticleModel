package ua.coolboy.particlemodel.runnable;

import java.util.List;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import ua.coolboy.particlemodel.drawer.Drawable;

public class PointsDrawer extends BukkitRunnable {
    
    List<Drawable> drawables;
    Location location;
    
    public PointsDrawer(List<Drawable> list, Location location) {
        drawables = list;
        this.location = location;
    }

    @Override
    public void run() {
        for(Drawable draw : drawables) {
            World world = location.getWorld();
            for(Vector vector : draw.getPoints()) {
                world.spawnParticle(Particle.REDSTONE, location.clone().add(vector), 0, Float.MIN_NORMAL, 1, 0.5, 1);
            }
        }
    }

}
