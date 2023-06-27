package com.urise.webapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Hw12 {

    public static void main(String[] args) {
        int[] array = new int[]{1, 2, 3, 3, 2, 3};
        System.out.println(minValue(array));
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        System.out.println(oddOrEven(list));
    }

    static int minValue(int[] values) {
        return Arrays.stream(values)
                .distinct()
                .sorted()
                .reduce(0, (x, y) -> x * 10 + y);
    }

    static List<Integer> oddOrEven(List<Integer> integers) {
        final Map<Boolean, List<Integer>> map = integers
                .stream()
                .collect(Collectors.partitioningBy(number -> number % 2 == 0));

        return map.get(false).size() % 2 == 0 ? map.get(true) : map.get(false);
    }
}