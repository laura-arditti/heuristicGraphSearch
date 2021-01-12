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
        System.out.println("Random graph generated");
        System.out.println(graph.toString());
        //GraphDrawer.draw(graph);
        GameForm gameForm = new GameForm(nPlayers,nActions);
        System.out.println("Number of configurations: " + gameForm.getNConfigurations());
        PotentialFunction potential = new PotentialFunction(gameForm,graph);
        System.out.println(potential.toString());
        TransitionData transitionData = new TransitionData(gameForm, pathLenght, potential);
        System.out.println(transitionData.toString());
        Separation benchmark = transitionData.toPotentialData().getSeparation();

        HeuristicOptimization optimizer = new HeuristicOptimization(transitionData,nPlayers,2000);
        OptimizationResult heuristicResult = optimizer.run(sampleSize);
        System.out.println(heuristicResult.graph.toString());
        System.out.println("Heuristic separation: \n" + heuristicResult.separation.toString());
        System.out.println("Exact separation given transition data: \n" + benchmark.toString());

    }

}
