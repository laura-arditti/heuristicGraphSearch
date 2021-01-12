package it.polito.arditti;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class LocalSearchResult {
    SimpleGraph<Integer, DefaultEdge> nextGraph;
    Integer[] modifiedLink;
    boolean isRemoved;

    public LocalSearchResult(SimpleGraph<Integer, DefaultEdge> nextGraph, Integer[] modifiedLink, boolean isRemoved) {
        this.nextGraph = nextGraph;
        this.modifiedLink = modifiedLink;
        this.isRemoved = isRemoved;
    }
}
