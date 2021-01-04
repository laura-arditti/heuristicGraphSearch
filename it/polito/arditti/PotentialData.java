package it.polito.arditti;

import java.util.*;

public class PotentialData {
    Map<Configuration,Double> potentialValues = new HashMap<>();
    GameForm game;

    public PotentialData(GameForm game) {
        this.game = game;
    }

    public void add(Configuration configuration, Double potentialValue){
        potentialValues.put(configuration,potentialValue);
    }

    public Configuration findConfigurationInData(int[] actions){
        Configuration configuration = null;
        for (Configuration dataConfig : potentialValues.keySet()){
            if (dataConfig.getActions()==actions){
                configuration = dataConfig;
                break;
            }
        }
        return configuration;
    }

    public Separation getSeparation() {
        Separation separation = new Separation(game.nPlayers);
        // cycle over all subsets of players
        for (int set = 1; set<1<<game.nPlayers; set++){
            // for each player in playerList, pairList contains
            // the list of all of its possible pairs of actions
            List<List<int[]>> pairList = new ArrayList<>();
            List<Integer> playerList = new ArrayList<>();
            // for each player in otherPlayerList, maxOtherActions
            // contains its number of actions
            List<Integer> maxOtherActions = new ArrayList<>();
            List<Integer> otherPlayerList = new ArrayList<>();
            for(int player = 0; player<game.nPlayers; player++){
                // if player belongs to set
                if (((set>>player)&1)==1){
                    pairList.add(game.getPairsOfActions(player));
                    playerList.add(player);
                }
                else {
                    maxOtherActions.add(game.nActions[player]);
                    otherPlayerList.add(player);
                }
            }
            int[] position = new int[playerList.size()];
            for (int i = 0; i<position.length; i++){
                position[i]=0;
            }

            int[] otherActions = new int[game.nPlayers-playerList.size()];
            for (int i = 0; i<otherActions.length; i++){
                otherActions[i]=0;
            }

            boolean outsideFinished = false;
            while(!outsideFinished) {

                boolean insideFinished = false;
                while (!insideFinished) {
                    List<int[]> cube = new ArrayList<>();
                    for (int i = 0; i < position.length; i++) {
                        cube.add(pairList.get(i).get(position[i]));
                    }

                    List<Integer> grayCodes = GrayCode.getCodes(cube.size());

                    double sum = 0;
                    double sign = 1;
                    for (int vertex : grayCodes) {
                        int[] strategy = new int[game.nPlayers];
                        for (int i = 0; i< playerList.size(); i++){
                            strategy[playerList.get(i)] = pairList.get(i).get(position[i])[(vertex>>i)&1];
                        }
                        for (int i = 0; i< otherPlayerList.size(); i++){
                            strategy[otherPlayerList.get(i)] = otherActions[i];
                        }
                        // check if the strategy (current vertex of the cube) is
                        // available in potentialValues data as a configuration
                        // If not, break and pass to the next cube
                        Configuration configuration = findConfigurationInData(strategy);
                        if(configuration == null){
                            break;
                        }
                        sum += sign*this.potentialValues.get(configuration);
                        sign=-sign;
                    }

                    if( sum != 0){
                        Set<Integer> group = new HashSet<>(playerList);
                        separation.add(group);
                    }

                    insideFinished = true;
                    for (int i = 0; i < position.length; i++) {
                        if (position[i] < pairList.get(i).size()-1) {
                            position[i]++;
                            insideFinished = false;
                            break;
                        }
                        position[i] = 0;
                    }
                }
                outsideFinished = true;
                for (int i = 0; i < otherActions.length; i++) {
                    if (otherActions[i] < maxOtherActions.get(i)-1) {
                        otherActions[i]++;
                        outsideFinished = false;
                        break;
                    }
                    otherActions[i] = 0;
                }
            }
        }
        Separation result = new Separation(game.nPlayers);
        for (Set<Integer> group : separation.groups) {
            boolean keep = true;
            for (Set<Integer> otherGroup : separation.groups){
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

    public PotentialData extractSample(int size){
        PotentialData sample = new PotentialData(game);
        if (size >= potentialValues.keySet().size()){
            return this;
        }
        else{
            int item = new Random().nextInt(size);
            int i = 0;
            for(Map.Entry<Configuration,Double> entry : potentialValues.entrySet()){
                if (i == item)
                    sample.add(entry.getKey(), entry.getValue());
                i++;
            }
        }
        return sample;
    }

    public boolean checkDependence(Set<Integer> candidate) {
    }
}

