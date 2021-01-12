package it.polito.arditti;

import org.jgrapht.alg.drawing.FRLayoutAlgorithm2D;
import org.jgrapht.generate.GnmRandomGraphGenerator;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.util.SupplierUtil;

import java.util.Random;
import java.util.function.Supplier;

public class Main {

    public static void main(String[] args) {
        int nPlayers = 4;
        int nEdges = 5;
        int[] nActions = {3, 4, 3, 2};
        int pathLenght = 40;
        int sampleSize = 20;
        SimpleGraph<Integer, DefaultEdge> graph = new SimpleGraph<Integer, DefaultEdge>(SupplierUtil.createIntegerSupplier(), SupplierUtil.createDefaultEdgeSupplier(), false);
        new GnmRandomGraphGenerator<Integer, DefaultEdge> (nPlayers,nEdges)
                .generateGraph(graph);
        GraphDrawer.draw(graph);
        GameForm gameForm = new GameForm(nPlayers,nActions);
        PotentialFunction potential = new PotentialFunction(gameForm,graph);
        System.out.println(potential.toString());
        TransitionData transitionData = new TransitionData(gameForm, pathLenght, potential);

        /*HeuristicOptimization optimizer = new HeuristicOptimization(transitionData,nPlayers,2000);
        Separation heuristicSeparation = optimizer.run(sampleSize);
        System.out.println(heuristicSeparation.toString());*/

    }

}
