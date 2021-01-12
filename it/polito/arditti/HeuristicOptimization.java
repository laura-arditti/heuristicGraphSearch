package it.polito.arditti;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.util.SupplierUtil;

import java.util.*;
import java.util.stream.Collectors;

public class HeuristicOptimization {
    private TransitionData data;
    private int nPlayers;
    private int numSteps;
    private ObjectiveFunction objectiveFunction;
    private PotentialData potentialData;


    public HeuristicOptimization(TransitionData data, int nPlayers, int numSteps) {
        this.data = data;
        this.nPlayers = nPlayers;
        this.numSteps = numSteps;
        this.objectiveFunction = new ObjectiveFunction(data);
        this.potentialData = data.toPotentialData();
    }



    public OptimizationResult run(int sampleSize){
        Separation currentSeparation = initializeSeparation(sampleSize);
        SimpleGraph<Integer, DefaultEdge> currentGraph = currentSeparation.buildUndirectedGraph();
        Map<Integer,Integer> estimatedDegrees = estimateDegrees(currentGraph,sampleSize/(double)data.size());
        double threshold = 0.1;
        int stepsSinceLastUpdate = 0;
        int round = 1;
        double temperature = coolingSchedule(round);
        for(int t=0; t<numSteps; t++){
            if (stepsSinceLastUpdate==1000){
                round++;
                temperature = coolingSchedule(round);
                stepsSinceLastUpdate=0;
            }
            else{
            stepsSinceLastUpdate++;
            }

            LocalSearchResult result = localSearch(currentGraph, estimatedDegrees, temperature);
            if(result.isRemoved) {
                currentSeparation = linkRegret(currentSeparation, result.modifiedLink);
            }else{
                currentSeparation = cliqueAnalisys(currentSeparation, result.modifiedLink,threshold);
            }
            currentGraph = currentSeparation.buildUndirectedGraph();
        }
        OptimizationResult result = new OptimizationResult(currentGraph,currentSeparation);
        return result;
    }

    private double coolingSchedule(int round) {
        double wellDepth = 50.0;
        return wellDepth/Math.log(1.0 + round);
    }

    private Map<Integer,Integer> estimateDegrees(SimpleGraph<Integer, DefaultEdge> currentGraph, double ratio) {
        Map<Integer,Integer> estimatedDegrees = new HashMap<>();
        for(Integer vertex : currentGraph.vertexSet()){
            int estimatedDegree = (int) Math.floor(currentGraph.degreeOf(vertex)*ratio);
            estimatedDegrees.put(vertex,estimatedDegree);
        }
        return estimatedDegrees;
    }

    private Separation initializeSeparation(int sampleSize) {
        PotentialData sample = potentialData.extractSample(sampleSize);
        return sample.getSeparation();

    }

    private LocalSearchResult localSearch(SimpleGraph<Integer, DefaultEdge> currentGraph, Map<Integer, Integer> estimatedDegrees, double temperature) {
        boolean isAccepted = false;
        List<Double> priorities = new ArrayList<Double>(Collections.nCopies(estimatedDegrees.keySet().size(), 0.0));
        for (Integer vertex : estimatedDegrees.keySet()){
            int currentDegree = currentGraph.degreeOf(vertex);
            double priority = estimatedDegrees.get(vertex)-currentDegree;
            priorities.set(vertex,priority);
        }
        Integer[] modifiedLink = new Integer[2];
        Boolean isRemoved = null;
        while (!isAccepted){
            Integer selectedVertex = Tools.getRandomInt(priorities);
            Random rand = new Random();
            isRemoved= rand.nextBoolean();
            if(!isRemoved){
                SimpleGraph<Integer, DefaultEdge> finalCurrentGraph1 = currentGraph;
                Set<Integer> neighbors = currentGraph.outgoingEdgesOf(selectedVertex)
                        .stream().map(edge-> finalCurrentGraph1.getEdgeTarget(edge))
                        .collect(Collectors.toSet());
                Set<Integer> complementary = new HashSet<>(currentGraph.vertexSet());
                complementary.removeAll(neighbors);
                complementary.remove(selectedVertex);
                List<Integer> nonNeighbors = complementary.stream().collect(Collectors.toList());
                if (nonNeighbors.isEmpty()){
                    continue;
                }
                Integer selectedNonNeighbor = Tools.getRandomFromList(nonNeighbors);
                SimpleGraph<Integer,DefaultEdge> candidate = new SimpleGraph<Integer,DefaultEdge>(SupplierUtil.createIntegerSupplier(), SupplierUtil.createDefaultEdgeSupplier(),false);
                for(Integer vertex : currentGraph.vertexSet()){
                    candidate.addVertex(vertex);
                }
                for(DefaultEdge edge : currentGraph.edgeSet()){
                    candidate.addEdge(currentGraph.getEdgeSource(edge),
                            currentGraph.getEdgeTarget(edge));
                }
                candidate.addEdge(selectedVertex,selectedNonNeighbor);
                isAccepted = evaluateAcceptance(currentGraph,candidate,temperature);
                if(isAccepted){
                    currentGraph = candidate;
                    modifiedLink = new Integer[]{selectedVertex,selectedNonNeighbor};
                }
            }
            else{
                SimpleGraph<Integer, DefaultEdge> finalCurrentGraph2 = currentGraph;
                List<Integer> neighbors = currentGraph.outgoingEdgesOf(selectedVertex)
                        .stream().map(edge-> finalCurrentGraph2.getEdgeTarget(edge))
                        .collect(Collectors.toList());
                if (neighbors.isEmpty()){
                    continue;
                }
                Integer selectedNeighbor = Tools.getRandomFromList(neighbors);
                SimpleGraph<Integer,DefaultEdge> candidate = new SimpleGraph<Integer,DefaultEdge>(SupplierUtil.createIntegerSupplier(),SupplierUtil.createDefaultEdgeSupplier(),false);
                for(Integer vertex : currentGraph.vertexSet()){
                    candidate.addVertex(vertex);
                }
                for(DefaultEdge edge : currentGraph.edgeSet()){
                    candidate.addEdge(currentGraph.getEdgeSource(edge),
                            currentGraph.getEdgeTarget(edge));
                }
                candidate.removeEdge(selectedVertex,selectedNeighbor);
                isAccepted = evaluateAcceptance(currentGraph,candidate,temperature);
                if(isAccepted){
                    currentGraph = candidate;
                    modifiedLink = new Integer[]{selectedVertex,selectedNeighbor};
                }
            }
        }
        return new LocalSearchResult(currentGraph, modifiedLink, isRemoved);
    }

    private boolean evaluateAcceptance(SimpleGraph<Integer, DefaultEdge> currentGraph, SimpleGraph<Integer, DefaultEdge> candidate, double temperature) {
        boolean isAccepted = false;
        Double currentObjective = objectiveFunction.evaluateObjective(currentGraph);
        Double candidateObjective = objectiveFunction.evaluateObjective(candidate);
        if(candidateObjective<=currentObjective){
            isAccepted=true;
        }
        else{
            List<Double> probabilities = List.of(1-Math.exp((currentObjective-candidateObjective)/temperature),Math.exp((currentObjective-candidateObjective)/temperature));
            int index = Tools.getRandomInt(probabilities);
            if(index==1){
                isAccepted=true;
            }
        }
        return isAccepted;
    }

    private Separation linkRegret(Separation currentSeparation, Integer[] modifiedLink) {
        Set<Set<Integer>> involvedCliques = currentSeparation.getCliques(modifiedLink);
        Set<Set<Integer>> newCliques = currentSeparation.getCliques();
        newCliques.removeAll(involvedCliques);
        for(Set<Integer> clique : involvedCliques){
            for(Integer player : modifiedLink){
                Set<Integer> newClique = clique.stream()
                        .filter(e->e!=player)
                        .collect(Collectors.toSet());
                newCliques.add(newClique);
            }
        }
        return new Separation(nPlayers,newCliques).maximalize();
    }

    private Separation cliqueAnalisys(Separation currentSeparation, Integer[] modifiedLink, double threshold) {
        Integer playerI = modifiedLink[0];
        Integer playerJ= modifiedLink[1];
        Set<Set<Integer>> cliquesI = currentSeparation.getCliques(playerI);
        Set<Set<Integer>> cliquesJ = currentSeparation.getCliques(playerJ);
        Map<Set<Integer>,Double> subcliquePriorities = new HashMap<>();
        for( Set<Integer> cliqueI : cliquesI){
            Set<Integer> cliqueIWithoutI = cliqueI.stream()
                    .filter(e->e!=playerI)
                    .collect(Collectors.toSet());
            Set<Set<Integer>> powerSet = Tools.powerSet(cliqueIWithoutI);
            for (Set<Integer> K : powerSet){
                K.add(playerI);
                double priority = 1.0/K.size();
                for (Set<Integer> cliqueJ : cliquesJ){
                    Set<Integer> intersection = new HashSet<Integer>(K);
                    intersection.retainAll(cliqueJ);
                    Set<Integer> union = new HashSet<Integer>(K);
                    union.addAll(cliqueJ);
                    priority+= intersection.size()/union.size();
                }
                subcliquePriorities.put(K,priority);
            }
        }
        Set<Set<Integer>> newGroups = currentSeparation.getCliques();
        newGroups.add(Set.of(modifiedLink));
        for (Set<Integer> subcliqueI : subcliquePriorities.keySet()){
            if (subcliquePriorities.get(subcliqueI)> threshold){
                Set<Integer> candidate = new HashSet<>(subcliqueI);
                candidate.add(playerJ);
                boolean isGroup = potentialData.checkDependence(candidate);
                if(isGroup){
                    newGroups.add(candidate);
                    candidate.remove(playerJ);
                    newGroups.remove(candidate);
                }
                /*else{
                    newGroups.add(Set.of(modifiedLink));
                }*/
            }
        }
        return new Separation(nPlayers,newGroups).maximalize();
    }





}
