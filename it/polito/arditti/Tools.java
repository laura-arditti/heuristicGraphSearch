package it.polito.arditti;

import java.util.*;
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

    public static <T> Set<Set<T>> powerSet(Set<T> originalSet) {
        Set<Set<T>> sets = new HashSet<Set<T>>();
        if (originalSet.isEmpty()) {
            sets.add(new HashSet<T>());
            return sets;
        }
        List<T> list = new ArrayList<T>(originalSet);
        T head = list.get(0);
        Set<T> rest = new HashSet<T>(list.subList(1, list.size()));
        for (Set<T> set : powerSet(rest)) {
            Set<T> newSet = new HashSet<T>();
            newSet.add(head);
            newSet.addAll(set);
            sets.add(newSet);
            sets.add(set);
        }
        return sets;
    }
}
