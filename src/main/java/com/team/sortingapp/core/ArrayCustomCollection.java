package com.team.sortingapp.core;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.Collector;

public class ArrayCustomCollection<T> implements CustomCollection<T> {
    private final T[] elements;
    private int currentSize = 0;

    @SuppressWarnings("unchecked")
    public ArrayCustomCollection(int capacity) {
        // Так как размер задаётся пользователем заранее, фиксируем массив
        this.elements = (T[]) new Object[capacity];
    }

    @Override
    public void add(T item) {
        if (currentSize >= elements.length) {
            throw new IllegalStateException("Коллекция заполнена! Максимальный размер: " + elements.length);
        }
        elements[currentSize++] = item;
    }

    @Override
    public T get(int index) {
        checkIndex(index);
        return elements[index];
    }

    @Override
    public void set(int index, T item) {
        checkIndex(index);
        elements[index] = item;
    }

    @Override
    public int size() {
        return currentSize;
    }

    @Override
    public boolean isEmpty() {
        return currentSize == 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor < currentSize;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return elements[cursor++];
            }
        };
    }

    @Override
    public Iterable<T> stream() {
        return this;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= currentSize) {
            throw new IndexOutOfBoundsException("Неверный индекс: " + index + ", Размер: " + currentSize);
        }
    }

    /**
     * Бонус для Роли №3 (Ввод/вывод): Кастомный коллектор для Stream API.
     * Позволит писать: stream.collect(ArrayCustomCollection.toCustomCollection(size))
     */
    public static <E> Collector<E, ?, ArrayCustomCollection<E>> toCustomCollection(int capacity) {
        return Collector.of(
                () -> new ArrayCustomCollection<>(capacity),
                ArrayCustomCollection::add,
                (left, right) -> {
                    for (E item : right) {
                        left.add(item);
                    }
                    return left;
                }
        );
    }
}
