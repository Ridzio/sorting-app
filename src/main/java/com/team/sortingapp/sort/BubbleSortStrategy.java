package com.team.sortingapp.sort;

import com.team.sortingapp.core.CustomCollection;
import com.team.sortingapp.core.SortStrategy;
import com.team.sortingapp.core.Sortable;

import java.util.Comparator;

public class BubbleSortStrategy<T extends Sortable> implements SortStrategy<T> {

    @Override
    @SuppressWarnings("unchecked")
    public void sort(CustomCollection<T> collection, int fieldIndex) {
        if (collection == null || collection.isEmpty()) {
            return;
        }

        int n = collection.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1 - i; j++) {
                Comparable<?> val1 = collection.get(j).getFieldByIndex(fieldIndex);
                Comparable<?> val2 = collection.get(j + 1).getFieldByIndex(fieldIndex);

                if (((Comparable<Object>) val1).compareTo(val2) > 0) {
                    T temp = collection.get(j);
                    collection.set(j, collection.get(j + 1));
                    collection.set(j + 1, temp);
                }
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
            for (int j = 0; j < n - 1 - i; j++) {
                if (comparator.compare(collection.get(j), collection.get(j + 1)) > 0) {
                    T temp = collection.get(j);
                    collection.set(j, collection.get(j + 1));
                    collection.set(j + 1, temp);
                }
            }
        }
    }
}
