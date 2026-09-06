package com.team.sortingapp.test;

import com.team.sortingapp.core.ArrayCustomCollection;
import com.team.sortingapp.core.CustomCollection;
import com.team.sortingapp.core.ElementCounter;
import com.team.sortingapp.core.Student;
import com.team.sortingapp.counter.ElementCounterImpl;

public class ElementCounterTest {
    private static int passedTests;
    private static int failedTests;

    public static void main(String[] args) {
        runTest("подсчёт элементов с остатком при делении на 4", ElementCounterTest::testRemainderChunk);
        runTest("коллекция меньше количества потоков", ElementCounterTest::testSmallCollection);
        runTest("искомый элемент отсутствует", ElementCounterTest::testMissingElement);
        runTest("пустая коллекция", ElementCounterTest::testEmptyCollection);
        runTest("подсчёт одинаковых Student через equals", ElementCounterTest::testStudentEquality);

        System.out.println("Пройдено: " + passedTests);
        System.out.println("Провалено: " + failedTests);

        if (failedTests > 0) {
            throw new AssertionError("Есть проваленные тесты: " + failedTests);
        }
    }

    private static void testRemainderChunk() {
        CustomCollection<Integer> collection = new ArrayCustomCollection<>(10);
        int[] values = {7, 1, 7, 2, 3, 7, 4, 5, 7, 6};
        for (int value : values) {
            collection.add(value);
        }

        ElementCounter<Integer> counter = new ElementCounterImpl<>();
        assertEquals(4L, counter.count(collection, 7));
    }

    private static void testSmallCollection() {
        CustomCollection<Integer> collection = new ArrayCustomCollection<>(3);
        collection.add(5);
        collection.add(5);
        collection.add(1);

        ElementCounter<Integer> counter = new ElementCounterImpl<>();
        assertEquals(2L, counter.count(collection, 5));
    }

    private static void testMissingElement() {
        CustomCollection<Integer> collection = new ArrayCustomCollection<>(4);
        collection.add(1);
        collection.add(2);
        collection.add(3);
        collection.add(4);

        ElementCounter<Integer> counter = new ElementCounterImpl<>();
        assertEquals(0L, counter.count(collection, 9));
    }

    private static void testEmptyCollection() {
        CustomCollection<Integer> collection = new ArrayCustomCollection<>(0);
        ElementCounter<Integer> counter = new ElementCounterImpl<>();
        assertEquals(0L, counter.count(collection, 1));
    }

    private static void testStudentEquality() {
        Student target = student(101, 4.5, "A-001");
        CustomCollection<Student> collection = new ArrayCustomCollection<>(5);
        collection.add(target);
        collection.add(student(102, 3.2, "A-002"));
        collection.add(student(101, 4.5, "A-001"));
        collection.add(student(103, 5.0, "A-003"));
        collection.add(student(101, 4.5, "A-001"));

        ElementCounter<Student> counter = new ElementCounterImpl<>();
        assertEquals(3L, counter.count(collection, target));
    }

    private static Student student(int groupNumber, double averageGrade, String recordBookNumber) {
        return Student.builder()
                .groupNumber(groupNumber)
                .averageGrade(averageGrade)
                .recordBookNumber(recordBookNumber)
                .build();
    }

    private static void runTest(String name, Runnable test) {
        try {
            test.run();
            passedTests++;
            System.out.println("PASS: " + name);
        } catch (Throwable error) {
            failedTests++;
            System.out.println("FAIL: " + name + " - " + error.getMessage());
        }
    }

    private static void assertEquals(long expected, long actual) {
        if (expected != actual) {
            throw new AssertionError("Ожидалось " + expected + ", получено " + actual);
        }
    }
}
