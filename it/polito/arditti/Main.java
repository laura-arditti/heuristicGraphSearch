package it.polito.arditti;

import org.jgrapht.generate.GnmRandomGraphGenerator;
import org.jgrapht.graph.SimpleGraph;

import java.util.Random;

public class Main {

    public static void main(String[] args) {
        int nPlayers = 4;
        int nEdges = 5;
        int[] nActions = {3, 4, 3, 2};
        int pathLenght = 40;
        int sampleSize = 20;
        SimpleGraph<Integer, Integer[]> graph = new SimpleGraph<>(null, null, false);
        new GnmRandomGraphGenerator<Integer, Integer[]> (nPlayers,nEdges)
                .generateGraph(graph);
        GameForm gameForm = new GameForm(nPlayers,nActions);
        PotentialFunction potential = new PotentialFunction(gameForm,graph);
        TransitionData transitionData = new TransitionData(gameForm, pathLenght, potential);

        HeuristicOptimization optimizer = new HeuristicOptimization(transitionData,nPlayers,0.1, 1000);
        Separation heuristicSeparation = optimizer.run(sampleSize);
        System.out.println(heuristicSeparation.toString());

    }

}
