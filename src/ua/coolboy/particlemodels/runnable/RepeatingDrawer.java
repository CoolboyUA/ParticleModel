package ua.coolboy.particlemodels.runnable;

import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import ua.coolboy.particlemodels.ParticleModels;
import ua.coolboy.particlemodels.drawer.Drawer;

public class RepeatingDrawer extends BukkitRunnable {

    ParticleModels plugin = ParticleModels.getInstance();
    
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
