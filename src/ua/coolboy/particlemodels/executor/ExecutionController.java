package ua.coolboy.particlemodels.executor;

import java.util.HashMap;
import ua.coolboy.particlemodels.drawer.Drawer;

public abstract class ExecutionController {

    private static final HashMap<Drawer, ExecutionType> busy = new HashMap<>(); //if drawer doing something, we'll know
    
    public static boolean isBusy(Drawer drawer) {
        return busy.keySet().contains(drawer);
    }
    
    public static void setBusy(Drawer drawer) {
        busy.put(drawer, ExecutionType.UNKNOWN);
    }
    
    public static void setBusy(Drawer drawer, ExecutionType type) {
        busy.put(drawer, type);
    }
    
    public static ExecutionType getType(Drawer drawer) {
        return busy.get(drawer);
    }
    
    public static void removeBusy(Drawer drawer) {
        busy.remove(drawer);
    }
    
    public static HashMap<Drawer, ExecutionType> getBusyDrawers() {
        return (HashMap<Drawer, ExecutionType>) busy.clone();
    }
    
}
