package it.polito.arditti;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class OptimizationResult {
    public final SimpleGraph<Integer, DefaultEdge> graph;
    public final Separation separation;

    public OptimizationResult(SimpleGraph<Integer, DefaultEdge> currentGraph, Separation currentSeparation) {
        this.graph = currentGraph;
        this.separation = currentSeparation;
    }
}
