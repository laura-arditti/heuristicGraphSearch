package it.polito.arditti;

import org.jgrapht.graph.SimpleGraph;

import java.util.HashSet;
import java.util.Set;

public class Separation {
    protected final Set<Set<Integer>> groups;
    protected final int nPlayers;

    public Separation(int nPlayers) {
        this.nPlayers = nPlayers;
        this.groups = new HashSet<>();
    }

    public void add(Set<Integer> group) {
        boolean isMaximal = true;
        for (Set<Integer> otherGroup: groups) {
            if (otherGroup.containsAll(group)){
                isMaximal = false;
                break;
            }
        }
        if(isMaximal){
            groups.add(group);
        }
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        for (Set<Integer> group : this.groups) {
            result.append(group.toString() + ", " );
        }
        return result.toString();
    }

    public Splitting toSplitting() {
        Separation[] separations = new Separation[nPlayers];
        for (int player = 0; player < nPlayers; player++){
            for (Set<Integer> group : this.groups) {
                if (group.contains(player)){
                    separations[player].add(group);
                }
            }
        }
        Splitting splitting = new Splitting(separations);
        return splitting;
    }

    public Set<Integer> getNeighbors(int player){
        Set<Integer> neighborhood = new HashSet<>();
        for (Set<Integer> group : this.groups) {
            if (group.contains(player)){
                group.remove(player);
                neighborhood.addAll(group);
            }
            }
        return neighborhood;
    }

    public SimpleGraph<Integer,Integer[]> buildUndirectedGraph(){
        SimpleGraph<Integer,Integer[]> graph = new SimpleGraph<Integer,Integer[]>(null,null, false);
        for (int player=0; player<nPlayers; player++){
            graph.addVertex(player);
        }
        for (int player=0; player<nPlayers; player++){
            Set<Integer> neighborhood = this.getNeighbors(player);
            for( int neighbor : neighborhood){
                graph.addEdge(player,neighbor);
            }
        }
        return graph;
    }
}
