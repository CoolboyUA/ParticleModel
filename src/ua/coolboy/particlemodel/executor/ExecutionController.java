package ua.coolboy.particlemodel.executor;

import java.util.HashMap;
import ua.coolboy.particlemodel.drawer.Drawer;

public abstract class ExecutionController {

    private static final HashMap<Drawer, ExecutionType> busy = new HashMap<>();
    
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
