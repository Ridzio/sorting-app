package com.team.sortingapp.sort;

import com.team.sortingapp.core.CustomCollection;
import com.team.sortingapp.core.SortStrategy;
import com.team.sortingapp.core.Sortable;

import java.util.Comparator;

/*
 * Стратегия сортировки выбором.
 * На каждом шаге находим минимальный элемент и ставим его на текущую позицию.
 */
public class SelectionSortStrategy<T extends Sortable> implements SortStrategy<T> {

    @Override
    @SuppressWarnings("unchecked")
    public void sort(CustomCollection<T> collection, int fieldIndex) {
        if (collection == null || collection.isEmpty()) {
            return;
        }

        int n = collection.size();
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            Comparable<?> minVal = collection.get(i).getFieldByIndex(fieldIndex);

            // Ищем минимальный элемент в неотсортированной части
            for (int j = i + 1; j < n; j++) {
                Comparable<?> currentVal = collection.get(j).getFieldByIndex(fieldIndex);
                if (((Comparable<Object>) currentVal).compareTo(minVal) < 0) {
                    minVal = currentVal;
                    minIndex = j;
                }
            }

            // Меняем местами, если нашли элемент меньше текущего
            if (minIndex != i) {
                T temp = collection.get(i);
                collection.set(i, collection.get(minIndex));
                collection.set(minIndex, temp);
            }
        }
    }

    @Override
    public void sort(CustomCollection<T> collection, Comparator<T> comparator) {
        if (collection == null || collection.isEmpty() || comparator == null) {
            return;
        }

        int n = collection.size();
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (comparator.compare(collection.get(j), collection.get(minIndex)) < 0) {
                    minIndex = j;
                }
            }

            if (minIndex != i) {
                T temp = collection.get(i);
                collection.set(i, collection.get(minIndex));
                collection.set(minIndex, temp);
            }
        }
    }
}
