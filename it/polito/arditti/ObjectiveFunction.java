package it.polito.arditti;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.HashSet;
import java.util.Set;

public class ObjectiveFunction {
    private final TransitionData transitionData;

    public ObjectiveFunction(TransitionData data) {
        this.transitionData = data;
    }

    public Double evaluateObjective(SimpleGraph<Integer, DefaultEdge> currentGraph) {
        Double loss = 0.0;
        for(Transition transition : transitionData.getPath()){
            loss += Math.pow((transition.getPotentialChange()-estimatePotentialChange(transition,currentGraph)),2);
        }
        return loss/transitionData.size();
    }
    private double estimatePotentialChange(Transition transition, SimpleGraph<Integer, DefaultEdge> currentGraph) {
        double estimate = 0.0;
        Set<Transition> compatibleTransitions = transitionData.getCompatibleTransitions(transition,currentGraph);
        for (Transition compatibleTransition : compatibleTransitions){
            estimate+= compatibleTransition.getPotentialChange();
        }
        estimate = estimate / compatibleTransitions.size();
        return estimate;
    }
}
