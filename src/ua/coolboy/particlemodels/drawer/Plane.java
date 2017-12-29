package ua.coolboy.particlemodels.drawer;

import com.owens.oobjloader.builder.FaceVertex;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.util.Vector;
import ua.coolboy.particlemodels.utils.VertexUtil;

public abstract class Plane implements Drawable{

    public static Plane fromVectors(Drawer drawer, List<Vector> vertices) {
        if (vertices.size() > 4) {
            throw new UnsupportedOperationException("Polygons with 5 and more vertices aren't supported!");
        }
        if (vertices.size() == 3) {
            return new TrianglePlane(drawer, vertices);
        }
        if (vertices.size() == 4) {
            return new RectanglePlane(drawer, vertices);
        }
        if(vertices.size() < 3) {
            throw new UnsupportedOperationException("Vertices size < 3. Use line or point instead!");
        }
        return null; // how it can be called?
    }
    
    public static Plane fromVertex(Drawer drawer, List<FaceVertex> vertex) {
        List<Vector> list = new ArrayList<>();
        for(FaceVertex vert : vertex) {
            list.add(VertexUtil.toVector(vert.v));
        }
        return fromVectors(drawer, list);
    }
    
    public Plane fromVectors(Drawer drawer, Vector... vertices) {
        return fromVectors(drawer, Arrays.asList(vertices));
    }
    
    public abstract List<Vector> getVertices();
    
    public class Side implements Comparable {
        Vector one;
        Vector two;
        
        public Side(Vector one, Vector two) {
            this.one = one;
            this.two = two;
        }
        
        public Vector getFirstVector() {
            return one;
        }
        
        public Vector getSecondVector() {
            return two;
        }
        
        public double size() {
            return one.distance(two);
        }

        @Override
        public int compareTo(Object t) {
            if(t instanceof Plane) {
                return Math.abs((int) (size() - ((Side) t).size()));
            } else return 0;
        }
    }
}
