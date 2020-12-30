package it.polito.arditti;

import org.jgrapht.graph.SimpleGraph;

public class LocalSearchResult {
    SimpleGraph<Integer,Integer[]> nextGraph;
    Integer[] modifiedLink;
    boolean isRemoved;

    public LocalSearchResult(SimpleGraph<Integer, Integer[]> nextGraph, Integer[] modifiedLink, boolean isRemoved) {
        this.nextGraph = nextGraph;
        this.modifiedLink = modifiedLink;
        this.isRemoved = isRemoved;
    }
}
