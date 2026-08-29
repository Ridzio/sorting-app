package com.team.sortingapp.core;

/**
 * Контракт записи результатов (отсортированных коллекций/найденных значений)
 * в файл. Запись производится в режиме добавления данных (append).
 *
 * @param <T> тип записываемых элементов
 */
public interface DataWriter<T extends Sortable> {

    void writeAppend(CustomCollection<T> collection, String filePath);
}
