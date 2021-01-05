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

    public Separation(int nPlayers,Set<Set<Integer>> groups) {
        this.nPlayers = nPlayers;
        this.groups = groups;
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

    // get cliques containing a couple of players
    public Set<Set<Integer>> getCliques(Integer[] modifiedLink) {
        Set<Integer> couple = Set.of(modifiedLink);
        Set<Set<Integer>> cliques = new HashSet<>();
        for(Set<Integer> group : groups){
            if(group.containsAll(couple)){
                cliques.add(group);
            }
        }
        return cliques;
    }

    // get all cliques/groups from the separation
    public Set<Set<Integer>> getCliques() {
        return this.groups;
    }

    public Set<Set<Integer>> getCliques(Integer playerI) {
        Set<Set<Integer>> cliques = new HashSet<>();
        for(Set<Integer> group : groups){
            if(group.contains(playerI)){
                cliques.add(group);
            }
        }
        return cliques;
    }

    public Separation maximalize() {
        Separation result = new Separation(nPlayers);
        for (Set<Integer> group : this.groups) {
            boolean keep = true;
            for (Set<Integer> otherGroup : this.groups){
                if (otherGroup.containsAll(group)) {
                    if(!group.equals(otherGroup)){
                        keep = false;
                    }
                }
            }
            if (keep){
                result.add(group);
            }
        }
        return result;
    }
}
