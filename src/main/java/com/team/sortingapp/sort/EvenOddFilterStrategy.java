package com.team.sortingapp.sort;

import com.team.sortingapp.core.CustomCollection;
import com.team.sortingapp.core.SortStrategy;
import com.team.sortingapp.core.Sortable;

import java.util.Comparator;

/*
 * Специальная стратегия для доп. задания 1:
 * Объекты с ЧЁТНЫМИ значениями числового поля сортируются по возрастанию,
 * объекты с НЕЧЁТНЫМИ значениями остаются на своих позициях.
 */
public class EvenOddFilterStrategy<T extends Sortable> implements SortStrategy<T> {

    private final int fieldIndex; // Индекс числового поля для проверки чётности

    public EvenOddFilterStrategy(int fieldIndex) {
        this.fieldIndex = fieldIndex;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void sort(CustomCollection<T> collection, int fieldIndex) {
        if (collection == null || collection.isEmpty()) {
            return;
        }

        int n = collection.size();

        // Собираем индексы элементов с чётными значениями поля
        // и их значения для сортировки
        int[] evenIndices = new int[n];
        int evenCount = 0;

        for (int i = 0; i < n; i++) {
            Comparable<?> val = collection.get(i).getFieldByIndex(this.fieldIndex);
            // Проверяем, что значение — число (Integer или Double)
            if (val instanceof Number) {
                int intValue = ((Number) val).intValue();
                if (intValue % 2 == 0) {
                    evenIndices[evenCount++] = i;
                }
            }
        }

        // Сортируем только элементы с чётными значениями (пузырьком)
        for (int i = 0; i < evenCount - 1; i++) {
            for (int j = 0; j < evenCount - 1 - i; j++) {
                int idx1 = evenIndices[j];
                int idx2 = evenIndices[j + 1];

                Comparable<?> val1 = collection.get(idx1).getFieldByIndex(this.fieldIndex);
                Comparable<?> val2 = collection.get(idx2).getFieldByIndex(this.fieldIndex);

                if (((Comparable<Object>) val1).compareTo(val2) > 0) {
                    T temp = collection.get(idx1);
                    collection.set(idx1, collection.get(idx2));
                    collection.set(idx2, temp);
                }
            }
        }
    }

    @Override
    public void sort(CustomCollection<T> collection, Comparator<T> comparator) {
        // Для компаратора используем ту же логику, но сравнение через компаратор
        if (collection == null || collection.isEmpty() || comparator == null) {
            return;
        }

        int n = collection.size();
        int[] evenIndices = new int[n];
        int evenCount = 0;

        for (int i = 0; i < n; i++) {
            Comparable<?> val = collection.get(i).getFieldByIndex(this.fieldIndex);
            if (val instanceof Number) {
                int intValue = ((Number) val).intValue();
                if (intValue % 2 == 0) {
                    evenIndices[evenCount++] = i;
                }
            }
        }

        for (int i = 0; i < evenCount - 1; i++) {
            for (int j = 0; j < evenCount - 1 - i; j++) {
                int idx1 = evenIndices[j];
                int idx2 = evenIndices[j + 1];

                if (comparator.compare(collection.get(idx1), collection.get(idx2)) > 0) {
                    T temp = collection.get(idx1);
                    collection.set(idx1, collection.get(idx2));
                    collection.set(idx2, temp);
                }
            }
        }
    }
}