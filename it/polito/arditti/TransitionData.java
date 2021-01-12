package it.polito.arditti;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TransitionData {
    List<Transition> path;
    GameForm gameForm;

    public TransitionData(GameForm gameForm, int pathLength, PotentialFunction potential){
        this.path = new ArrayList<>();
        this.gameForm = gameForm;
        Configuration start = Configuration.random(gameForm);
        for (int i = 0; i < pathLength; i++) {
            Configuration end = start.getRandomComparable();
            double potentialChange =
                    potential.getPotential(end) - potential.getPotential(start);
            Transition transition = new Transition(gameForm, start, end, potentialChange);
            this.path.add(transition);
        }
    }

    public TransitionData(List<Transition> path, GameForm gameForm){
        this.path = path;
        this.gameForm = gameForm;

    }

    public PotentialData toPotentialData(){
        PotentialData potential = new PotentialData(gameForm);
        double potentialValue = 0.0;
        Configuration beginning = path.get(0).getStart();
        potential.add(beginning,potentialValue);
        for (Transition transition : path){
            potentialValue+= transition.getPotentialChange();
            potential.add(transition.getEnd(), potentialValue);
        }
        return potential;
    }

    int size(){
        return path.size();
    }

    public List<Transition> getPath() {
        return path;
    }

    public Set<Transition> getCompatibleTransitions(Transition transition, SimpleGraph<Integer, DefaultEdge> currentGraph) {
        Set<Transition> compatibleTransitions = new HashSet<>();
        Integer movingPlayer = transition.getMovingPlayer();
        SimpleGraph<Integer, DefaultEdge> finalCurrentGraph = currentGraph;
        List<Integer> neighbors = currentGraph.outgoingEdgesOf(movingPlayer)
                .stream().map(edge-> finalCurrentGraph.getEdgeTarget(edge))
                .collect(Collectors.toList());
        for (Transition otherTransition : this.path){
            if(transition.isCompatible(otherTransition,neighbors)){
                compatibleTransitions.add(otherTransition);
            }
        }
        return compatibleTransitions;
    }

    @Override
    public String toString() {
        StringBuffer result = new StringBuffer("Transition path: \n");
        for (Transition transition : path){
            result.append(transition.toString() + "\n");
        }
        return result.toString();
    }
}
