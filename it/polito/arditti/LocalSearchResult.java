package it.polito.arditti;

import org.jgrapht.graph.SimpleGraph;

public class LocalSearchResult {
    SimpleGraph<Integer,Integer[]> nextGraph;
    Integer[] modifiedLink;
    boolean isRemoved;
}
