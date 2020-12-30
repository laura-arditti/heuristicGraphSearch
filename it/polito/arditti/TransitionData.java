package it.polito.arditti;

import java.util.List;

public class TransitionData {
    List<Transition> path;
    GameForm gameForm;

    public TransitionData(List<Transition> path, GameForm gameForm){
        this.path = path;
        this.gameForm = gameForm;

    }

    public PotentialData toPotentialData(){
        PotentialData potential = new PotentialData(gameForm);
        double potentialValue = 0.0;
        Configuration beginning = path.get(0).getStart();
        potential.add(beginning,potentialValue);
        for (Transition transition : path){
            potentialValue+= transition.getPotentialChange();
            potential.add(transition.getEnd(), potentialValue);
        }
        return potential;
    }

    int size(){
        return path.size();
    }
}
