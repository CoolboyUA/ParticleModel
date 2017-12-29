package ua.coolboy.particlemodels.runnable;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.bukkit.scheduler.BukkitRunnable;
import ua.coolboy.particlemodels.drawer.Drawable;
import ua.coolboy.particlemodels.utils.LogUtil;

public class AsyncDrawer extends BukkitRunnable {

    private static ExecutorService executor = Executors.newFixedThreadPool(1);
    private List<Drawable> drawables;
    
    
    public AsyncDrawer(List<Drawable> drawables) {
        this.drawables = drawables;
    }
    
    
    @Override
    public void run() {
        try {
            executor.invokeAll(drawables);
            LogUtil.log(drawables.toString());
        } catch (InterruptedException ex) {
            LogUtil.warn("Ooops, something went wrong while drawing!");
        }
    }

    /*List<Drawable> drawables = new ArrayList<>();
    Location location;

    public AsyncDrawer(Drawable drawable, Location location) {
        drawables.add(drawable);
        this.location = location;
    }

    public AsyncDrawer(List<Drawable> drawables, Location location) {
        this.drawables = drawables;
        this.location = location;
    }

    @Override
    public void run() {
        for (Drawable drawable : drawables) {
            drawable.draw(location);
        }
    }*/
}
