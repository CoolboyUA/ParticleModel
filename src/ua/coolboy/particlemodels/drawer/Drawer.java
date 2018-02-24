package ua.coolboy.particlemodels.drawer;

import com.owens.oobjloader.builder.Build;
import com.owens.oobjloader.builder.Face;
import ua.coolboy.particlemodels.executor.ExecutionController;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.bukkit.Location;
import ua.coolboy.particlemodels.ParticleModels;
import ua.coolboy.particlemodels.executor.ExecutionType;
import ua.coolboy.particlemodels.runnable.RepeatingDrawer;
import ua.coolboy.particlemodels.utils.LogUtil;
import ua.coolboy.particlemodels.utils.VertexUtil;

public class Drawer {
    //This thing creates particles
    private Build build;
    private List<Drawable> drawables;
    private ParticleModels plugin;
    private String name = "Unknown model";
    private float scale = 1;
    private Location location;

    public Drawer(Build build) {
        this.build = build;
        this.plugin = ParticleModels.getInstance();
        drawables = new ArrayList<>();
        parseBuild(); //Maybe async?
    }

    public Drawer(Build build, String name) {
        this(build);
        this.name = name;
    }

    public Drawer(Build build, String name, Location location) {
        this(build);
        this.name = name;
        this.location = location;
    }

    private void parseBuild() {
        ExecutionController.setBusy(this, ExecutionType.CALCULATING);
        for (Face face : build.faces) {
            drawables.add(Plane.fromVertex(this, face.vertices));
        }
        ExecutionController.removeBusy(this);
    }

    public void scale(float scale) {
        this.scale *= scale; //set relative scale
        ExecutionController.setBusy(this, ExecutionType.SCALING);
        for (Drawable draw : drawables) {
            draw.scale(scale);
            draw.setPoints(VertexUtil.optimize(draw.getPoints(),3));
        }
        ExecutionController.removeBusy(this);
    }
    
    public float getScale() {
        return scale;
    }
    
    public void repeat(Location location) {
        new RepeatingDrawer(this, location).runTaskTimerAsynchronously(plugin, 0, 20);
    }
    
    public void draw(Location location) {
        this.location = location;
        if (ExecutionController.isBusy(this)) {
            LogUtil.log(name + " is busy (" + ExecutionController.getType(this).toString() + ")");
        }
        
        ExecutorService executor = Executors.newFixedThreadPool(4);//Maybe try cached pool?
         try {
            executor.invokeAll(drawables);
        } catch (InterruptedException ex) {
            LogUtil.warn("Ooops, something went wrong while drawing!");
        }
         executor.shutdown();
        
        //new AsyncDrawer(drawables).runTask/*Asynchronously*/(plugin);
    }

    public void draw() {
        if (location == null) {
            LogUtil.log("Location is null in " + name);
            return;
        }
        if (ExecutionController.isBusy(this)) {
            LogUtil.log(name + " is busy (" + ExecutionController.getType(this).toString() + ")");
        }
        ExecutorService executor = Executors.newFixedThreadPool(2);
         try {
            executor.invokeAll(drawables);
        } catch (InterruptedException ex) {
            LogUtil.warn("Ooops, something went wrong while drawing!");
        }
        //new AsyncDrawer(drawables).runTask/*Asynchronously*/(plugin);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Build getBuild() {
        return build;
    }

    public List<Drawable> getDrawables() {
        return drawables;
    }
    
    public void setLocation(Location location) {
        this.location = location;
    }
    
    public Location getLocation() {
        return location.clone();
    }
}
