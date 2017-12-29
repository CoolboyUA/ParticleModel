package ua.coolboy.particlemodel.runnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import ua.coolboy.particlemodel.drawer.Drawable;

public class CallableDrawer implements Callable<Void> {
    
    private List<Drawable> drawables = new ArrayList<>();
    
    public CallableDrawer(Drawable drawable) {
        this.drawables.add(drawable);
    }
    
    public CallableDrawer(List<Drawable> drawables) {
        this.drawables = drawables;
    }
    
    @Override
    public Void call() throws Exception {
        for (Drawable drawable : drawables) {
            drawable.draw(drawable.getDrawer().getLocation());
        }
        return null;
    }

}
