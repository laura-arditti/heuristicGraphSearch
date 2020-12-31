package it.polito.arditti;

import org.jgrapht.graph.SimpleGraph;

import java.util.*;
import java.util.stream.Collectors;

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
            LocalSearchResult result = localSearch(currentGraph, estimatedDegrees);
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

    private LocalSearchResult localSearch(SimpleGraph<Integer, Integer[]> currentGraph, Map<Integer, Integer> estimatedDegrees) {
        boolean isAccepted = false;
        List<Double> priorities = new ArrayList<>();
        for (Integer vertex : estimatedDegrees.keySet()){
            int currentDegree = currentGraph.degreeOf(vertex);
            double priority = estimatedDegrees.get(vertex)-currentDegree;
            priorities.add(vertex,priority);
        }
        double temperature = 1.0;
        Integer[] modifiedLink = new Integer[2];
        while (!isAccepted){
            Integer selectedVertex = Tools.getRandomInt(priorities);
            Random rand = new Random();
            boolean isRemoved= rand.nextBoolean();
            if(!isRemoved){
                SimpleGraph<Integer, Integer[]> finalCurrentGraph1 = currentGraph;
                Set<Integer> neighbors = currentGraph.outgoingEdgesOf(selectedVertex)
                        .stream().map(edge-> finalCurrentGraph1.getEdgeTarget(edge))
                        .collect(Collectors.toSet());
                Set<Integer> complementary = new HashSet<>(currentGraph.vertexSet());
                complementary.removeAll(neighbors);
                List<Integer> nonNeighbors = complementary.stream().collect(Collectors.toList());
                Integer selectedNonNeighbor = Tools.getRandomFromList(nonNeighbors);
                SimpleGraph<Integer,Integer[]> candidate = new SimpleGraph<Integer,Integer[]>(null,null,false);
                for(Integer vertex : currentGraph.vertexSet()){
                    candidate.addVertex(vertex);
                }
                for(Integer[] edge : currentGraph.edgeSet()){
                    candidate.addEdge(edge[0],edge[1]);
                }
                candidate.addEdge(selectedVertex,selectedNonNeighbor);
                isAccepted = evaluateAcceptance(currentGraph,candidate,temperature);
                if(isAccepted){
                    currentGraph = candidate;
                    modifiedLink = new Integer[]{selectedVertex,selectedNonNeighbor};
                }
            }
            else{
                SimpleGraph<Integer, Integer[]> finalCurrentGraph = currentGraph;
                List<Integer> neighbors = currentGraph.outgoingEdgesOf(selectedVertex)
                        .stream().map(edge-> finalCurrentGraph.getEdgeTarget(edge))
                        .collect(Collectors.toList());
                Integer selectedNeighbor = Tools.getRandomFromList(neighbors);
                SimpleGraph<Integer,Integer[]> candidate = new SimpleGraph<Integer,Integer[]>(null,null,false);
                for(Integer vertex : currentGraph.vertexSet()){
                    candidate.addVertex(vertex);
                }
                for(Integer[] edge : currentGraph.edgeSet()){
                    candidate.addEdge(edge[0],edge[1]);
                }
                candidate.removeEdge(selectedVertex,selectedNeighbor);
                isAccepted = evaluateAcceptance(currentGraph,candidate,temperature);
                if(isAccepted){
                    currentGraph = candidate;
                    modifiedLink = new Integer[]{selectedVertex,selectedNeighbor};
                }
                return new LocalSearchResult(currentGraph, modifiedLink, isRemoved);
            }

        }
    }

    private boolean evaluateAcceptance(SimpleGraph<Integer, Integer[]> currentGraph, SimpleGraph<Integer, Integer[]> candidate, double temperature) {
    }

    private Separation cliqueAnalisys(Separation currentSeparation, Integer[] modifiedLink) {
    }

    private Separation linkRegret(Separation currentSeparation, Integer[] modifiedLink) {
    }





}
