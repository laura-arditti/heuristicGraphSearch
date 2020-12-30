package it.polito.arditti;

public class Transition {
    private final Game game;
    private final Configuration start;
    private final Configuration end;

    public double getPotentialChange() {
        return potentialChange;
    }

    public Configuration getStart() {
        return start;
    }

    public Configuration getEnd() {
        return end;
    }


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

}
