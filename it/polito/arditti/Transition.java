package it.polito.arditti;

import org.jgrapht.graph.SimpleGraph;

import java.util.List;

public class Transition {
    private final Game game;
    private final Configuration start;
    private final Configuration end;
    private final double potentialChange;

    public Transition(Game game, Configuration start, Configuration end, double potentialChange) {
        this.game = game;
        this.start = start;
        this.end = end;
        this.potentialChange = potentialChange;
    }

    public int getMovingPlayer(){
        int[] startActions = start.getActions();
        int[] endActions = end.getActions();
        int  movingPlayer = -1;
        for (int player = 0; player < game.nPlayers; player++) {
            if (endActions[player] != startActions[player]){
                movingPlayer=player;
            };
        }
        return movingPlayer;
    }

    public double getPotentialChange() {
        return potentialChange;
    }

    public Configuration getStart() {
        return start;
    }

    public Configuration getEnd() {
        return end;
    }


    public boolean isCompatible(Transition otherTransition, List<Integer> neighbors) {
        boolean isCompatible=true;
        if(this.getMovingPlayer() != otherTransition.getMovingPlayer()){
            isCompatible=false;
            return isCompatible;
        }
        int movingPlayer=this.getMovingPlayer();
        if(this.getStart().getActions()[movingPlayer] != otherTransition.getStart().getActions()[movingPlayer]
        || this.getEnd().getActions()[movingPlayer] != otherTransition.getEnd().getActions()[movingPlayer]){
            isCompatible=false;
            return isCompatible;
        }
        for(Integer neighbor : neighbors){
            if(this.getStart().getActions()[neighbor] != otherTransition.getStart().getActions()[neighbor]){
                isCompatible=false;
                return isCompatible;
            }
        }
        return isCompatible;
    }
}
