package it.polito.arditti;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class Tools {

    public static int getRandomInt(List<Double> priorities) {
        Random rand = new Random();
        double totalSum = priorities.stream().mapToDouble(i->i).sum();
        double index = rand.nextDouble()*totalSum;
        double sum = 0;
        int i=0;
        while(sum < index ) {
            sum = sum + priorities.get(i);
            i++;
        }
        return Math.max(0,i-1);
    }

    public static int getRandomFromList(List<Integer> elements) {
        Random rand = new Random();
        int index = rand.nextInt(elements.size());
        return elements.get(index);
    }
}
