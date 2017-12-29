package ua.coolboy.particlemodel.runnable;

import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import ua.coolboy.particlemodel.ParticleModel;
import ua.coolboy.particlemodel.drawer.Drawer;

public class RepeatingDrawer extends BukkitRunnable {

    ParticleModel plugin = ParticleModel.getInstance();
    
    Drawer drawer;
    Location location;

    public RepeatingDrawer(Drawer drawer, Location location) {
        this.drawer = drawer;
        this.location = location;
    }

    public RepeatingDrawer(Drawer drawer) {
        this.drawer = drawer;
    }

    @Override
    public void run() {
        if(!plugin.isEnabled()) this.cancel();
        if (location != null) {
            drawer.draw(location);
        } else if (drawer.getLocation() != null) {
            drawer.draw();
        }
    }
}
