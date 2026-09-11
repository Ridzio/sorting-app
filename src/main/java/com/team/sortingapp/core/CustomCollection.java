package com.team.sortingapp.core;

/**
 * Контракт кастомной коллекции. Используется и модулем ввода/вывода данных
 * (для заполнения), и модулем сортировки (для перестановки элементов).
 * Запрещено использовать готовые реализации Collection API — только
 * собственная имплементация (например, на базе массива).
 *
 * @param <T> тип хранимых элементов
 */
public interface CustomCollection<T> extends Iterable<T> {

    void add(T item);

    T get(int index);

    void set(int index, T item);

    int size();

    boolean isEmpty();

}
