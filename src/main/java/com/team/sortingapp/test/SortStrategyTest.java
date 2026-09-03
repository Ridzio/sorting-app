package com.team.sortingapp.test;

import com.team.sortingapp.core.ArrayCustomCollection;
import com.team.sortingapp.core.CustomCollection;
import com.team.sortingapp.core.Student;
import com.team.sortingapp.sort.BubbleSortStrategy;
import com.team.sortingapp.sort.EvenOddFilterStrategy;
import com.team.sortingapp.sort.SelectionSortStrategy;

public class SortStrategyTest {

    private static int passedTests = 0;
    private static int failedTests = 0;

    public static void main(String[] args) {
        System.out.println("=== ТЕСТЫ СТРАТЕГИЙ СОРТИРОВКИ ===\n");

        testBubbleSortByGroupNumber();
        testBubbleSortByAverageGrade();
        testBubbleSortByRecordBookNumber();

        testSelectionSortByGroupNumber();
        testSelectionSortByAverageGrade();

        testBubbleSortWithComparator();

        testEvenOddFilter();

        System.out.println("\n=== ИТОГИ ===");
        System.out.println("Пройдено: " + passedTests);
        System.out.println("Провалено: " + failedTests);
    }

    private static void testBubbleSortByGroupNumber() {
        System.out.println("Тест: BubbleSort по номеру группы (поле 0)");

        CustomCollection<Student> collection = createTestCollection();
        BubbleSortStrategy<Student> strategy = new BubbleSortStrategy<>();

        strategy.sort(collection, 0);

        boolean isSorted = true;
        for (int i = 0; i < collection.size() - 1; i++) {
            if (collection.get(i).getGroupNumber() > collection.get(i + 1).getGroupNumber()) {
                isSorted = false;
                break;
            }
        }

        printResult(isSorted, "BubbleSort по группе", collection);
    }

    private static void testBubbleSortByAverageGrade() {
        System.out.println("\nТест: BubbleSort по среднему баллу (поле 1)");

        CustomCollection<Student> collection = createTestCollection();
        BubbleSortStrategy<Student> strategy = new BubbleSortStrategy<>();

        strategy.sort(collection, 1); // сортировка по averageGrade

        boolean isSorted = true;
        for (int i = 0; i < collection.size() - 1; i++) {
            if (collection.get(i).getAverageGrade() > collection.get(i + 1).getAverageGrade()) {
                isSorted = false;
                break;
            }
        }

        printResult(isSorted, "BubbleSort по баллу", collection);
    }

    private static void testBubbleSortByRecordBookNumber() {
        System.out.println("\nТест: BubbleSort по номеру зачётки (поле 2)");

        CustomCollection<Student> collection = createTestCollection();
        BubbleSortStrategy<Student> strategy = new BubbleSortStrategy<>();

        strategy.sort(collection, 2); // сортировка по recordBookNumber

        boolean isSorted = true;
        for (int i = 0; i < collection.size() - 1; i++) {
            if (collection.get(i).getRecordBookNumber().compareTo(
                    collection.get(i + 1).getRecordBookNumber()) > 0) {
                isSorted = false;
                break;
            }
        }

        printResult(isSorted, "BubbleSort по зачётке", collection);
    }

    private static void testSelectionSortByGroupNumber() {
        System.out.println("\nТест: SelectionSort по номеру группы (поле 0)");

        CustomCollection<Student> collection = createTestCollection();
        SelectionSortStrategy<Student> strategy = new SelectionSortStrategy<>();

        strategy.sort(collection, 0);

        boolean isSorted = true;
        for (int i = 0; i < collection.size() - 1; i++) {
            if (collection.get(i).getGroupNumber() > collection.get(i + 1).getGroupNumber()) {
                isSorted = false;
                break;
            }
        }

        printResult(isSorted, "SelectionSort по группе", collection);
    }

    private static void testSelectionSortByAverageGrade() {
        System.out.println("\nТест: SelectionSort по среднему баллу (поле 1)");

        CustomCollection<Student> collection = createTestCollection();
        SelectionSortStrategy<Student> strategy = new SelectionSortStrategy<>();

        strategy.sort(collection, 1);

        boolean isSorted = true;
        for (int i = 0; i < collection.size() - 1; i++) {
            if (collection.get(i).getAverageGrade() > collection.get(i + 1).getAverageGrade()) {
                isSorted = false;
                break;
            }
        }

        printResult(isSorted, "SelectionSort по баллу", collection);
    }

    private static void testBubbleSortWithComparator() {
        System.out.println("\nТест: BubbleSort с компаратором (по баллу по убыванию)");

        CustomCollection<Student> collection = createTestCollection();
        BubbleSortStrategy<Student> strategy = new BubbleSortStrategy<>();

        java.util.Comparator<Student> comparator = (s1, s2) ->
                Double.compare(s2.getAverageGrade(), s1.getAverageGrade());

        strategy.sort(collection, comparator);

        boolean isSortedDesc = true;
        for (int i = 0; i < collection.size() - 1; i++) {
            if (collection.get(i).getAverageGrade() < collection.get(i + 1).getAverageGrade()) {
                isSortedDesc = false;
                break;
            }
        }

        printResult(isSortedDesc, "BubbleSort с компаратором (убывание)", collection);
    }

    private static void testEvenOddFilter() {
        System.out.println("\nТест: EvenOddFilter (чётные по группе сортируются, нечётные на местах)");

        CustomCollection<Student> collection = new ArrayCustomCollection<>(5);
        collection.add(Student.builder().groupNumber(105).averageGrade(3.0).recordBookNumber("111").build());
        collection.add(Student.builder().groupNumber(102).averageGrade(4.0).recordBookNumber("222").build());
        collection.add(Student.builder().groupNumber(103).averageGrade(5.0).recordBookNumber("333").build());
        collection.add(Student.builder().groupNumber(104).averageGrade(2.0).recordBookNumber("444").build());
        collection.add(Student.builder().groupNumber(101).averageGrade(4.5).recordBookNumber("555").build());

        EvenOddFilterStrategy<Student> strategy = new EvenOddFilterStrategy<>(0);
        strategy.sort(collection, 0);

        boolean isCorrect = true;

        if (collection.get(0).getGroupNumber() != 105) isCorrect = false;
        if (collection.get(2).getGroupNumber() != 103) isCorrect = false;
        if (collection.get(4).getGroupNumber() != 101) isCorrect = false;

        if (collection.get(1).getGroupNumber() != 102) isCorrect = false;
        if (collection.get(3).getGroupNumber() != 104) isCorrect = false;

        printResult(isCorrect, "EvenOddFilter", collection);
    }

    private static CustomCollection<Student> createTestCollection() {
        CustomCollection<Student> collection = new ArrayCustomCollection<>(5);
        collection.add(Student.builder().groupNumber(103).averageGrade(4.5).recordBookNumber("333").build());
        collection.add(Student.builder().groupNumber(101).averageGrade(3.0).recordBookNumber("111").build());
        collection.add(Student.builder().groupNumber(105).averageGrade(5.0).recordBookNumber("555").build());
        collection.add(Student.builder().groupNumber(102).averageGrade(4.0).recordBookNumber("222").build());
        collection.add(Student.builder().groupNumber(104).averageGrade(2.5).recordBookNumber("444").build());
        return collection;
    }

    private static void printResult(boolean passed, String testName, CustomCollection<Student> collection) {
        if (passed) {
            System.out.println("✅ " + testName + " — ПРОЙДЕН");
            passedTests++;
        } else {
            System.out.println("❌ " + testName + " — ПРОВАЛЕН");
            failedTests++;
        }
        System.out.println("Результат: " + collection);
    }
}