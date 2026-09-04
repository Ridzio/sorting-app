package com.team.sortingapp.counter;

import com.team.sortingapp.core.CustomCollection;
import com.team.sortingapp.core.ElementCounter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ElementCounterImpl<T> implements ElementCounter<T> {
    private static final int THREAD_COUNT = 4;

    @Override
    public long count(CustomCollection<T> collection, T target) {
        Objects.requireNonNull(collection, "Коллекция не может быть null");

        int size = collection.size();
        if (size == 0) {
            System.out.println("Найдено вхождений: 0");
            return 0;
        }

        int chunkSize = (size + THREAD_COUNT - 1) / THREAD_COUNT;
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        List<Future<Long>> futures = new ArrayList<>(THREAD_COUNT);

        try {
            for (int from = 0; from < size; from += chunkSize) {
                int rangeStart = from;
                int rangeEnd = Math.min(from + chunkSize, size);
                futures.add(executor.submit(() -> countInRange(collection, target, rangeStart, rangeEnd)));
            }

            long total = 0;
            for (Future<Long> future : futures) {
                total += future.get();
            }

            System.out.println("Найдено вхождений: " + total);
            return total;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Подсчёт вхождений был прерван", e);
        } catch (ExecutionException e) {
            throw new IllegalStateException("Ошибка при подсчёте вхождений", e.getCause());
        } finally {
            executor.shutdown();
        }
    }

    private long countInRange(CustomCollection<T> collection, T target, int from, int to) {
        long count = 0;
        for (int i = from; i < to; i++) {
            if (Objects.equals(collection.get(i), target)) {
                count++;
            }
        }
        return count;
    }
}
