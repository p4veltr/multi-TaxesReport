package org.example;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

public class Main {
    public static final int ARRAY_LENGTH = 10;
    public static final int MAX_ARRAY_VALUE = Integer.MAX_VALUE;
    public static void main(String[] args) {
        int[] shop1 = createArray();
        int[] shop2 = createArray();
        int[] shop3 = createArray();

        LongAdder la = new LongAdder();
        ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        calcSum(shop1, la, es);
        calcSum(shop2, la, es);
        calcSum(shop3, la, es);
        try {
            es.awaitTermination(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        es.shutdown();

        System.out.println(Arrays.toString(shop1));
        System.out.println(Arrays.toString(shop2));
        System.out.println(Arrays.toString(shop3));
        System.out.println("Общий итог: " + la.sum());
    }

    public static void calcSum(int[] arr, LongAdder la, ExecutorService es) {
        Runnable action = () -> Arrays.stream(arr).forEach(la::add);
        es.submit(action);
    }
    public static int[] createArray() {
        int[] arr = new int[ARRAY_LENGTH];
        Random rand = new Random();
        for (int i = 0; i < arr.length; i++) {
            arr[i] = rand.nextInt(MAX_ARRAY_VALUE);
        }
        return arr;
    }
}
