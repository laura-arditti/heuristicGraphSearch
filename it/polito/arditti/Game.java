package it.polito.arditti;

import java.util.ArrayList;
import java.util.List;

public class Game extends GameForm {
    protected final Utility[] utilities;

    public Game(int nPlayers, int[] nActions, double[][] utilities) {
        super(nPlayers,nActions);
        this.utilities = new Utility[nPlayers];
        for (int player = 0; player<nPlayers; player++){
            this.utilities[player] = new Utility(this,utilities[player]);
        }
    }

    public Splitting getSplitting() {
        Separation[] separations = new Separation[nPlayers];
        for (int player = 0; player<nPlayers; player++){
            separations[player] = utilities[player].getSeparation();
        }
        Splitting splitting = new Splitting(separations);
        return splitting;
    }
}
