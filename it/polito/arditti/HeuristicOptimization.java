package it.polito.arditti;

import org.jgrapht.graph.SimpleGraph;

import java.util.HashMap;
import java.util.Map;

public class HeuristicOptimization {
    private TransitionData data;
    private int nPlayers;
    private double tolerance;
    private int numSteps;


    public HeuristicOptimization(TransitionData data, int nPlayers, double tolerance, int numSteps) {
        this.data = data;
        this.nPlayers = nPlayers;
        this.tolerance = tolerance;
        this.numSteps = numSteps;
    }



    public Separation run(int sampleSize){
        Separation currentSeparation = initializeSeparation(sampleSize);
        SimpleGraph<Integer,Integer[]> currentGraph = currentSeparation.buildUndirectedGraph();
        Map<Integer,Integer> estimatedDegrees = estimateDegrees(currentGraph,sampleSize/(double)data.size());
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

    private Map<Integer,Integer> estimateDegrees(SimpleGraph<Integer, Integer[]> currentGraph, double ratio) {
        Map<Integer,Integer> estimatedDegrees = new HashMap<>();
        for(Integer vertex : currentGraph.vertexSet()){
            int estimatedDegree = (int) Math.floor(currentGraph.degreeOf(vertex)*ratio);
            estimatedDegrees.put(vertex,estimatedDegree);
        }
        return estimatedDegrees;
    }

    private Separation initializeSeparation(int sampleSize) {
        PotentialData potentialData = this.data.toPotentialData();
        PotentialData sample = potentialData.extractSample(sampleSize);
        return sample.getSeparation();

    }

    private LocalSearchResult localSearch(SimpleGraph<Integer, Integer[]> currentGraph) {
    }

    private Separation cliqueAnalisys(Separation currentSeparation, Integer[] modifiedLink) {
    }

    private Separation linkRegret(Separation currentSeparation, Integer[] modifiedLink) {
    }





}
