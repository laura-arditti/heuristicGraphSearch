package it.polito.arditti;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

public class Configuration {
    private final GameForm game;
    private final int[] actions;
    private final int index;

    public Configuration(GameForm game) {
        this.game = game;
        this.actions = new int[game.nPlayers];
        for (int player = 0; player < game.nPlayers; player++) {
            this.actions[player] = 0;
        }
        this.index = 0;
    }

    public Configuration(GameForm game, int[] actions) {
        this.game = game;
        this.actions = actions;
        for (int player = 0; player < game.nPlayers; player++){
            if (actions[player]<game.nActions[player]-1){
                this.index = player;
                return;
            }
        }
        this.index = -1;
    }

    public static Configuration random(GameForm gameForm) {
        Random rand = new Random();
        int[] actions = IntStream.range(0, gameForm.nPlayers)
                .map(player -> rand.nextInt(gameForm.nActions[player]))
                .toArray();
        return new Configuration(gameForm,actions);
    }

    public Configuration getNext() {
        if( index < 0){
            return null;
        }
        int[] nextActions = Arrays.copyOf(actions,game.nPlayers);
        nextActions[index]++;
        for(int player = 0; player< index-1; player++){
            nextActions[player]=0;
        }
        Configuration next = new Configuration(game, nextActions);
        return next;
    }

    public Configuration getCloser() {
        int[] closerActions = Arrays.copyOf(actions,game.nPlayers);
        for(int player = 0; player< index; player++){
            closerActions[player]=0;
        }
        Configuration closer = new Configuration(game, closerActions);
        return closer;
    }

    public int getCloserPlayer() {
        return this.index;
    }

    public int[] getActions() {
        return this.actions;
    }

    public boolean hasNext(){
        return index>0;
    }
}
