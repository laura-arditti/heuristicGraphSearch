package it.polito.arditti;

import org.jgrapht.alg.clique.BronKerboschCliqueFinder;
import org.jgrapht.graph.SimpleGraph;

import java.util.*;
import java.util.stream.IntStream;

public class PotentialFunction {
    private GameForm gameForm;
    private SimpleGraph<Integer,Integer[]> graph;
    private Map<Set<Integer>,Utility> localPotentials;

    public PotentialFunction(GameForm gameForm, SimpleGraph<Integer, Integer[]> graph) {
        this.gameForm = gameForm;
        this.graph = graph;
        BronKerboschCliqueFinder<Integer, Integer[]> cliqueFinder = new BronKerboschCliqueFinder<>(graph);
        Random rand = new Random();
        this.localPotentials = new HashMap<>();
        for (Set<Integer> clique : cliqueFinder){
            int nPlayers = clique.size();
            int[] nActions =
                    IntStream.range(0, gameForm.nPlayers)
                            .filter(player -> clique.contains(player))
                            .map(player -> gameForm.nActions[player])
                            .toArray();
            double[] localPotential =
                    IntStream.range(0, gameForm.getNConfigurations())
                    .mapToDouble(config->rand.nextDouble())
                    .toArray();
            localPotentials.put(clique,
                    new Utility(
                            new GameForm(nPlayers,nActions),
                            localPotential));
        }
    }
}
