package com.team.sortingapp.core;

/**
 * Контракт паттерна Strategy для сортировки. Каждая реализация — отдельный
 * алгоритм сортировки (пузырьковая, быстрая и т.д.), применяемый
 * к CustomCollection по заданному полю сущности.
 *
 * @param <T> тип элементов, должен реализовывать Sortable
 */
public interface SortStrategy<T extends Sortable> {

    /**
     * Сортирует коллекцию по указанному полю в натуральном порядке.
     *
     * @param collection коллекция для сортировки (изменяется на месте)
     * @param fieldIndex индекс поля, по которому производится сортировка
     */
    void sort(CustomCollection<T> collection, int fieldIndex);

    /**
     * Сортирует коллекцию с использованием переданного компаратора
     * (для кастомной сортировки — доп.задание 1 и т.п.).
     *
     * @param collection коллекция для сортировки
     * @param comparator компаратор, определяющий порядок
     */
    void sort(CustomCollection<T> collection, java.util.Comparator<T> comparator);
}
