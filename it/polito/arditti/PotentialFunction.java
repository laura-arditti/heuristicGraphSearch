package it.polito.arditti;

import org.jgrapht.alg.clique.BronKerboschCliqueFinder;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.*;
import java.util.stream.IntStream;

public class PotentialFunction {
    private GameForm gameForm;
    private SimpleGraph<Integer,DefaultEdge> graph;
    private Map<Set<Integer>,Utility> localPotentials;

    public PotentialFunction(GameForm gameForm, SimpleGraph<Integer, DefaultEdge> graph) {
        this.gameForm = gameForm;
        this.graph = graph;
        BronKerboschCliqueFinder<Integer, DefaultEdge> cliqueFinder = new BronKerboschCliqueFinder<>(graph);
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

    public Double getPotential(Configuration configuration) {
        int[] actions = configuration.getActions();
        Double value = 0.0;
        for(Set<Integer> clique : localPotentials.keySet()){
            int[] cliqueActions =
                    IntStream.range(0, gameForm.nPlayers)
                            .filter(player -> clique.contains(player))
                            .map(player -> actions[player])
                            .toArray();
            value += localPotentials
                    .get(clique).getUtility(cliqueActions);
        }
        return value;
    }

    public SimpleGraph<Integer, DefaultEdge> getGraph() {
        return graph;
    }

    @Override
    public String toString() {
        Configuration configuration = new Configuration(gameForm);
        StringBuffer result = new StringBuffer("Configuration : Potential \n");
        while(true){
            Double potential = getPotential(configuration);
            result.append(configuration.toString() + ":" + potential.toString() + "\n");
            if (!configuration.hasNext()){
                break;
            }
            else {
                configuration = configuration.getNext();
            }
        }

        return result.toString();
    }
}
