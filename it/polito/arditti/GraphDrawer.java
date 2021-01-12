package it.polito.arditti;

import org.jgrapht.alg.drawing.FRLayoutAlgorithm2D;
import org.jgrapht.alg.drawing.model.Box2D;
import org.jgrapht.alg.drawing.model.MapLayoutModel2D;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class GraphDrawer {

    public static void draw(SimpleGraph<Integer, DefaultEdge> graph) {
        Box2D box = new Box2D(100,100);
        org.jgrapht.alg.drawing.model.MapLayoutModel2D<Integer> model = new MapLayoutModel2D<>(box);
        FRLayoutAlgorithm2D<Integer, DefaultEdge> FRlayout = new FRLayoutAlgorithm2D<Integer, DefaultEdge>();
        FRlayout.layout(graph,model);

    }
}
