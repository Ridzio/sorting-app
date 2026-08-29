package com.team.sortingapp.core;

/**
 * Контракт многопоточного подсчёта количества вхождений элемента N
 * в коллекцию. Результат выводится в консоль реализацией.
 *
 * @param <T> тип элементов коллекции
 */
public interface ElementCounter<T> {

    /**
     * Подсчитывает количество вхождений target в collection,
     * используя несколько потоков, и печатает результат в консоль.
     *
     * @param collection коллекция для подсчёта
     * @param target искомый элемент
     * @return количество найденных вхождений
     */
    long count(CustomCollection<T> collection, T target);
}
