package com.team.sortingapp.test;

import com.team.sortingapp.core.ArrayCustomCollection;
import com.team.sortingapp.core.CustomCollection;
import com.team.sortingapp.core.Student;

public class DomainModelTest {
    private static int passedTests;
    private static int failedTests;

    public static void main(String[] args) {
        runTest("Builder создаёт корректного Student", DomainModelTest::testValidStudent);
        runTest("Builder отклоняет неверную группу", DomainModelTest::testInvalidGroup);
        runTest("Builder отклоняет неверный средний балл", DomainModelTest::testInvalidGrade);
        runTest("Builder отклоняет пустой номер зачётки", DomainModelTest::testInvalidRecordBook);
        runTest("Student корректно реализует equals и hashCode", DomainModelTest::testEqualsAndHashCode);
        runTest("getFieldByIndex возвращает все поля", DomainModelTest::testFieldAccess);
        runTest("ArrayCustomCollection add/get/set/size", DomainModelTest::testCollectionOperations);
        runTest("ArrayCustomCollection проверяет границы", DomainModelTest::testCollectionBounds);
        runTest("ArrayCustomCollection не превышает capacity", DomainModelTest::testCollectionCapacity);
        runTest("ArrayCustomCollection поддерживает for-each", DomainModelTest::testIteration);

        System.out.println("Пройдено: " + passedTests);
        System.out.println("Провалено: " + failedTests);

        if (failedTests > 0) {
            throw new AssertionError("Есть проваленные тесты: " + failedTests);
        }
    }

    private static void testValidStudent() {
        Student student = student(101, 4.5, "A-001");
        assertEquals(101, student.getGroupNumber());
        assertEquals(4.5, student.getAverageGrade());
        assertEquals("A-001", student.getRecordBookNumber());
    }

    private static void testInvalidGroup() {
        expectThrows(IllegalArgumentException.class,
                () -> student(0, 4.0, "A-001"));
    }

    private static void testInvalidGrade() {
        expectThrows(IllegalArgumentException.class,
                () -> student(101, 5.1, "A-001"));
    }

    private static void testInvalidRecordBook() {
        expectThrows(IllegalArgumentException.class,
                () -> student(101, 4.0, "   "));
    }

    private static void testEqualsAndHashCode() {
        Student first = student(101, 4.5, "A-001");
        Student second = student(101, 4.5, "A-001");
        Student other = student(102, 4.5, "A-001");

        assertTrue(first.equals(second));
        assertEquals(first.hashCode(), second.hashCode());
        assertTrue(!first.equals(other));
    }

    private static void testFieldAccess() {
        Student student = student(101, 4.5, "A-001");
        assertEquals(101, student.getFieldByIndex(0));
        assertEquals(4.5, student.getFieldByIndex(1));
        assertEquals("A-001", student.getFieldByIndex(2));
        assertEquals(3, student.getFieldCount());
        expectThrows(IllegalArgumentException.class, () -> student.getFieldByIndex(3));
    }

    private static void testCollectionOperations() {
        CustomCollection<String> collection = new ArrayCustomCollection<>(2);
        assertTrue(collection.isEmpty());
        collection.add("first");
        collection.add("second");
        assertEquals(2, collection.size());
        assertEquals("first", collection.get(0));
        collection.set(0, "changed");
        assertEquals("changed", collection.get(0));
    }

    private static void testCollectionBounds() {
        CustomCollection<String> collection = new ArrayCustomCollection<>(1);
        collection.add("value");
        expectThrows(IndexOutOfBoundsException.class, () -> collection.get(-1));
        expectThrows(IndexOutOfBoundsException.class, () -> collection.get(1));
        expectThrows(IndexOutOfBoundsException.class, () -> collection.set(1, "other"));
    }

    private static void testCollectionCapacity() {
        CustomCollection<String> collection = new ArrayCustomCollection<>(1);
        collection.add("value");
        expectThrows(IllegalStateException.class, () -> collection.add("overflow"));
    }

    private static void testIteration() {
        CustomCollection<Integer> collection = new ArrayCustomCollection<>(3);
        collection.add(1);
        collection.add(2);
        collection.add(3);

        int sum = 0;
        for (int value : collection) {
            sum += value;
        }
        assertEquals(6, sum);
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

    private static void expectThrows(Class<? extends Throwable> expectedType, Runnable action) {
        try {
            action.run();
        } catch (Throwable error) {
            if (expectedType.isInstance(error)) {
                return;
            }
            throw new AssertionError("Ожидалось " + expectedType.getSimpleName() + ", получено " + error.getClass().getSimpleName());
        }
        throw new AssertionError("Ожидалось исключение " + expectedType.getSimpleName());
    }

    private static void assertTrue(boolean condition) {
        if (!condition) {
            throw new AssertionError("Условие не выполнено");
        }
    }

    private static void assertEquals(Object expected, Object actual) {
        if (expected == null ? actual != null : !expected.equals(actual)) {
            throw new AssertionError("Ожидалось " + expected + ", получено " + actual);
        }
    }

    private static void assertEquals(double expected, double actual) {
        if (Double.compare(expected, actual) != 0) {
            throw new AssertionError("Ожидалось " + expected + ", получено " + actual);
        }
    }
}
