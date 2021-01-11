package it.polito.arditti;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

public class Configuration {
    private final GameForm gameForm;
    private final int[] actions;
    private final int index;

    public Configuration(GameForm gameForm) {
        this.gameForm = gameForm;
        this.actions = new int[gameForm.nPlayers];
        for (int player = 0; player < gameForm.nPlayers; player++) {
            this.actions[player] = 0;
        }
        this.index = 0;
    }

    public Configuration(GameForm gameForm, int[] actions) {
        this.gameForm = gameForm;
        this.actions = actions;
        for (int player = 0; player < gameForm.nPlayers; player++){
            if (actions[player]<gameForm.nActions[player]-1){
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
        int[] nextActions = Arrays.copyOf(actions,gameForm.nPlayers);
        nextActions[index]++;
        for(int player = 0; player< index-1; player++){
            nextActions[player]=0;
        }
        Configuration next = new Configuration(gameForm, nextActions);
        return next;
    }

    public Configuration getCloser() {
        int[] closerActions = Arrays.copyOf(actions,gameForm.nPlayers);
        for(int player = 0; player< index; player++){
            closerActions[player]=0;
        }
        Configuration closer = new Configuration(gameForm, closerActions);
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

    public Configuration getRandomComparable() {
        Random random = new Random();
        int movingPlayer = random.nextInt(gameForm.nPlayers);
        int[] otherActions = IntStream.range(0,gameForm.nActions[movingPlayer]-1)
                .filter(action -> action != actions[movingPlayer])
                .toArray();
        int index = random.nextInt(otherActions.length);
        int newAction = otherActions[index];
        int[] newActions = Arrays.copyOf(actions, actions.length);
        newActions[movingPlayer]=newAction;
        return new Configuration(gameForm,newActions);
    }
}
