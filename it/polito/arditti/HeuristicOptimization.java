package it.polito.arditti;

import org.jgrapht.graph.SimpleGraph;

import java.util.Map;

public class HeuristicOptimization {
    private TransitionData data;
    private int nPlayers;
    private double tolerance;
    private int numSteps;


    public HeuristicOptimization(TrasitionData data, int nPlayers, double tolerance, int numSteps) {
        this.data = data;
        this.nPlayers = nPlayers;
        this.tolerance = tolerance;
        this.numSteps = numSteps;
    }



    public Separation run(){
        Separation currentSeparation = initializeSeparation();
        SimpleGraph<Integer,Integer[]> currentGraph = currentSeparation.buildUndirectedGraph();
        for(int t=0; t<numSteps; t++){
            LocalSearchResult result = localSearch(currentGraph);
            if(result.isRemoved) {
                currentSeparation = linkRegret(currentSeparation, result.modifiedLink);
            }else{
                currentSeparation = cliqueAnalisys(currentSeparation, result.modifiedLink);
            }
            currentGraph = currentSeparation.buildUndirectedGraph();
        }
        return currentSeparation;
    }

    private Separation initializeSeparation() {

    }

    private Separation cliqueAnalisys(Separation currentSeparation, Integer[] modifiedLink) {
    }

    private Separation linkRegret(Separation currentSeparation, Integer[] modifiedLink) {
    }

    private LocalSearchResult localSearch(SimpleGraph<Integer, Integer[]> currentGraph) {
    }




}
