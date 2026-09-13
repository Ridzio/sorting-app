package com.team.sortingapp;

import com.team.sortingapp.core.CustomCollection;
import com.team.sortingapp.core.DataSource;
import com.team.sortingapp.core.DataWriter;
import com.team.sortingapp.core.ElementCounter;
import com.team.sortingapp.core.SortStrategy;
import com.team.sortingapp.counter.ElementCounterImpl;
import com.team.sortingapp.sort.BubbleSortStrategy;
import com.team.sortingapp.sort.EvenOddFilterStrategy;
import com.team.sortingapp.sort.SelectionSortStrategy;
import com.team.sortingapp.core.Student;
import io.FileDataSource;
import io.OutputDataSource;
import io.RandomDataSource;
import io.ManualDataSource;

import java.util.Comparator;
import java.util.Scanner;

public class Main {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static CustomCollection<Student> currentCollection = null;

    public static void main(String[] args) {

        boolean isRunning = true;

        System.out.println("=== Sorting App ===");

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

    private static void printMenu() {

        System.out.println("""
                --------------------
                --- Главное меню ---
                1. Заполнить данные
                2. Отсортировать
                3. Записать результат в файл
                4. Подсчитать вхождения
                0. Выйти
                """);
        System.out.print("Ваш выбор: ");
    }

    private static void handleFileData() {

        System.out.println("""
                --- Заполнение данных ---
                1. Из файла
                2. Рандомно
                3. Вручную
                0. Назад
                """);
        System.out.print("Ваш выбор: ");

        String choice = SCANNER.nextLine().trim();

        switch (choice) {
            case "1" -> fileFromFile();
            case "2" -> fileRandom();
            case "3" -> fileManual();
            case "0" -> System.out.println("Возврат в главное меню.");
            default -> System.out.println("Неверный ввод.");
        }
    }

    private static void fileFromFile() {

        try {
            int length = askLength();
            if (length <= 0) return;

            DataSource<Student> dataSource = new FileDataSource<>("src/main/resources/default.json");
            currentCollection = dataSource.load(length);
            System.out.println("Загружено из файла: " + currentCollection.size() + " студентов.");
            printCollection(currentCollection);
        } catch (RuntimeException e) {
            System.out.println("Ошибка чтения файла: " + e.getMessage());
        }
    }

    private static void fileRandom() {
        int length = askLength();
        if (length <= 0) return;

        DataSource<Student> dataSource = new RandomDataSource<>();
        currentCollection = dataSource.load(length);
        System.out.println("Сгенерировано рандомом: " + currentCollection.size() + " студентов.");
        printCollection(currentCollection);
    }

    private static void fileManual() {
        int length = askLength();
        if (length <= 0) return;

        ManualDataSource dataSource = new ManualDataSource();
        currentCollection = dataSource.load(length);
        System.out.println("Введено вручную: " + currentCollection.size() + " студентов");
        printCollection(currentCollection);
    }

    private static void handleSort() {
        if (currentCollection == null || currentCollection.isEmpty()) {
            System.out.println("Сначала заполните данные (пункт 1).");
            return;
        }

        System.out.println("""
                --- Сортировка ---
                Выберите поле:
                1. Номер группы
                2. Средний балл
                3. Номер зачётки
                0. Назад
                """);
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

        System.out.println("""
                Выберите алгоритм:
                1. Bubble Sort
                2. Selection Sort
                3. EvenOddFilter
                0. Назад
                """);
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

        System.out.println("""
                Выберите порядок:
                1. По возрастанию
                2. По убыванию
                0. Назад
                """);
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

        SortStrategy<Student> strategy = switch (algo) {
            case "1" -> new BubbleSortStrategy<>();
            case "2" -> new SelectionSortStrategy<>();
            case "3" -> new EvenOddFilterStrategy<>(fieldIndex);
            default -> null;
        };

        if (strategy == null) {
            System.out.println("Не удалось создать стратегию.");
            return;
        }

        if ("2".equals(order)) {
            strategy.sort(currentCollection, buildComparator(fieldIndex, true));
        } else {
            strategy.sort(currentCollection, fieldIndex);
        }

        System.out.println("\nСортировка выполнена.");
        System.out.println(" Поле: " + fieldName(fieldIndex));
        System.out.println(" Алгоритм: " + algoName(algo));
        System.out.println(" Порядок: " + ("2".equals(order) ? "по убыванию" : "по возрастанию"));
        printCollection(currentCollection);
    }

    private static void handleWriteToFile() {
        if (currentCollection == null || currentCollection.isEmpty()) {
            System.out.println("Сначала заполните данные (пункт 1).");
            return;
        }

        System.out.println("\n--- Запись в файл ---");
        System.out.print("Введите путь к файлу: ");
        String path = SCANNER.nextLine().trim();

        try {
            DataWriter<Student> writer = new OutputDataSource<>(path.isEmpty() ? null : path);
            writer.writeAppend(currentCollection, path.isEmpty() ? null : path);
            System.out.println("Данные записаны в файл: " + (path.isEmpty() ? "defaultoutput.json" : path));
        } catch (RuntimeException e) {
            System.out.println("Ошибка записи: " + e.getMessage());
        }
    }

    private static void handleCountOccurrences() {
        if (currentCollection == null || currentCollection.isEmpty()) {
            System.out.println("Сначала заполните данные (пункт 1).");
            return;
        }

        System.out.println("\n--- Подсчёт вхождений ---");
        System.out.println("Введите студента для поиска: ");

        try {
            Student target = readStudentFromConsole();

            ElementCounter<Student> counter = new ElementCounterImpl<>();
            counter.count(currentCollection, target);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка валидации: " + e.getMessage());
        }
    }

    private static int askLength() {
        System.out.print("Введите количество студентов: ");
        try {
            int length = Integer.parseInt(SCANNER.nextLine().trim());
            if (length <= 0) {
                System.out.println("Длина должна быть больше 0.");
                return -1;
            }
            return length;
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: нужно ввести целое число");
            return -1;
        }
    }

    private static Student readStudentFromConsole() {
        System.out.print("Номер группы: ");
        int group = Integer.parseInt(SCANNER.nextLine().trim());

        System.out.print("Средний балл: ");
        double grade = Double.parseDouble(SCANNER.nextLine().trim());

        System.out.print("Номер зачетки: ");
        String record = SCANNER.nextLine().trim();

        return Student.builder()
                .groupNumber(group)
                .averageGrade(grade)
                .recordBookNumber(record)
                .build();
    }

    @SuppressWarnings("unchecked")
    private static Comparator<Student> buildComparator(int fieldIndex, boolean descending) {
        Comparator<Student> cmp = (s1, s2) -> {
            Comparable<Object> v1 = (Comparable<Object>) s1.getFieldByIndex(fieldIndex);
            Comparable<Object> v2 = (Comparable<Object>) s2.getFieldByIndex(fieldIndex);
            return v1.compareTo(v2);
        };
        return descending ? cmp.reversed() : cmp;
    }

    private static void printCollection(CustomCollection<Student> collection) {
        if (collection == null || collection.isEmpty()) {
            System.out.println(" (коллекция пуста)");
            return;
        }
        int i = 0;
        for (Student s : collection) {
            System.out.printf(" [%d] %s%n", i++, s);
        }
    }

    private static String fieldName(int fieldIndex) {
        return switch (fieldIndex) {
            case 0 -> "Номер группы";
            case 1 -> "Средний балл";
            case 2 -> "Номер зачетки";
            default -> "Неизвестное поле";
        };
    }

    private static String algoName(String algo) {
        return switch (algo) {
            case "1" -> "Bubble Sort";
            case "2" -> "Selection Sort";
            case "3" -> "EvenOddFilter";
            default -> "Неизвестный алгоритм";
        };
    }
}


