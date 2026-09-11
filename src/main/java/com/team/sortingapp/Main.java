package com.team.sortingapp;

import com.team.sortingapp.core.CustomCollection;
import com.team.sortingapp.core.Student;

import java.util.Scanner;

/**
 * Точка входа. Реализация CLI-цикла — задача модуля "CLI + интеграция".
 */
public class Main {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static CustomCollection<Student> currentCollection = null;

    public static void main(String[] args) {

        boolean isRunning = true;

        System.out.println("Sorting App — TODO: implement CLI menu loop");

        while (isRunning) {
            System.out.println();
            printMenu();
            String input = SCANNER.nextLine().trim();

            switch (input) {
                case "1" -> handleFileData();
                case "2" -> handleSort();
                case "3" -> handleWriteToFile();
                case "4" -> handleCountOccurrences();
                case "0" -> {
                    System.out.println("Выход из программы.");
                    isRunning = false;
                }
                default -> System.out.println("Неверный ввод. Пожалуйста, выберите пункт из меню.");
            }


        }


    }
    // Ранний внешний вариант Меню
    private static void printMenu() {
        System.out.println("---".repeat(6));
        System.out.println("--- Главное меню ---");
        System.out.println("1. Заполнить данные");
        System.out.println("2. Отсортировать");
        System.out.println("3. Записать результат в файл");
        System.out.println("4. Подсчитать вхождения");
        System.out.println("0. Выйти");
        System.out.print("Ваш выбор: ");
    }

    private static void handleFileData() {
        System.out.println("\n--- Заполнение данных --- ");
        System.out.println("1. Из файла");
        System.out.println("2. Рандомно");
        System.out.println("3. Вручную");
        System.out.println("0. Назад");
        System.out.print("Ваш выбор: ");

        String choice = SCANNER.nextLine().trim();

        switch (choice) {
            case "1" -> System.out.println("fileFromFile()");
            case "2" -> System.out.println("fileRandom()");
            case "3" -> System.out.println("fileManual()");
            case "0" -> System.out.println("Возврат в главное меню.");
            default -> System.out.println("Неверный ввод.");
        }
    }

    //private static void fileFromFile() {}

    //private static void fileRandom() {}

    //private static void fileManual() {}

    private static void handleSort() {
        if (currentCollection == null || currentCollection.isEmpty()) {
            System.out.println("Сначала заполните данные (пункт 1).");
            return;
        }

        System.out.println("\n--- Сортировка --- ");
        System.out.println("Выберите поле:");
        System.out.println("1. Номер группы");
        System.out.println("2. Средний балл");
        System.out.println("3. Номер зачетки");
        System.out.println("0. Назад");
        System.out.print("Ваш выбор: ");

        String fieldInput = SCANNER.nextLine().trim();

        if ("0".equals(fieldInput)) {
            System.out.println("Возврат в главное меню");
            return;
        }

        int fieldIndex;
        switch (fieldInput) {
            case "1" -> fieldIndex = 0;
            case "2" -> fieldIndex = 1;
            case "3" -> fieldIndex = 2;
            default -> {
                System.out.println("Неверный ввод.");
                return;
            }
        }

        System.out.println("\nВыберите алгоритм:");
        System.out.println("1. Bubble Sort");
        System.out.println("2. Selection Sort");
        System.out.println("3. EvenOddFilter");
        System.out.println("0. Назад");
        System.out.print("Ваш выбор: ");

        String algo = SCANNER.nextLine().trim();

        if ("0".equals(algo)) {
            System.out.println("Возврат в главное меню.");
            return;
        }

        if (!algo.matches("[1-3]")) {
            System.out.println("Неверный ввод.");
            return;
        }

        System.out.println("\nВыберите порядок: ");
        System.out.println("1. По возрастанию");
        System.out.println("2. По убыванию");
        System.out.println("0. Назад");
        System.out.print("Ваш выбор: ");

        String order = SCANNER.nextLine().trim();

        if ("0".equals(order)) {
            System.out.println("Возврат в главное меню.");
            return;
        }

        if (!order.matches("[1-2]")) {
            System.out.println("Неверный ввод.");
            return;
        }

        //Ожидаю обновлений в коде

    }

    private static void handleWriteToFile() {
        if (currentCollection == null || currentCollection.isEmpty()) {
            System.out.println("Сначала заполните данные (пункт 1).");
            return;
        }

        System.out.println("\n--- Запись в файл ---");
        System.out.print("Введите путь к файлу: ");
        String path = SCANNER.nextLine().trim();

        //ждем

    }

    private static void handleCountOccurrences() {
        if (currentCollection == null || currentCollection.isEmpty()) {
            System.out.println("Сначала заполните данные (пункт 1).");
            return;
        }

        System.out.println("\n--- Подсчет вхождений ---");
        System.out.println("Введите студента для поиска: ");

        //Ждем
    }
}

