package com.team.sortingapp.test;

import com.team.sortingapp.core.ArrayCustomCollection;
import com.team.sortingapp.core.CustomCollection;
import com.team.sortingapp.core.Student;

import io.FileDataSource;
import io.ManualDataSource;
import io.OutputDataSource;
import io.RandomDataSource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class IoLayerTest {

    private static int passedTests;
    private static int failedTests;

    public static void main(String[] args) {
        runTest(
                "FileDataSource читает валидные данные и пропускает невалидные",
                IoLayerTest::testFileDataSource
        );

        runTest(
                "FileDataSource соблюдает ограничение length",
                IoLayerTest::testFileDataSourceLength
        );

        runTest(
                "FileDataSource возвращает пустую коллекцию при length <= 0",
                IoLayerTest::testFileDataSourceZeroLength
        );

        runTest(
                "RandomDataSource создаёт корректное количество валидных студентов",
                IoLayerTest::testRandomDataSource
        );

        runTest(
                "ManualDataSource читает корректный ручной ввод",
                IoLayerTest::testManualDataSource
        );

        runTest(
                "ManualDataSource отклоняет невалидный ввод",
                IoLayerTest::testManualDataSourceInvalidInput
        );

        runTest(
                "OutputDataSource записывает и дополняет файл",
                IoLayerTest::testOutputDataSource
        );

        System.out.println();
        System.out.println("=== ИТОГИ I/O ===");
        System.out.println("Пройдено: " + passedTests);
        System.out.println("Провалено: " + failedTests);

        if (failedTests > 0) {
            throw new AssertionError(
                    "Есть проваленные I/O тесты: " + failedTests
            );
        }
    }

    private static void testFileDataSource() {
        Path file = null;

        try {
            file = Files.createTempFile(
                    "sorting-app-input-",
                    ".json"
            );

            String json = """
                    [
                      {
                        "groupNumber": 101,
                        "averageGrade": 4.5,
                        "recordBookNumber": "100001"
                      },
                      {
                        "groupNumber": 102,
                        "averageGrade": 9.0,
                        "recordBookNumber": "100002"
                      },
                      {
                        "groupNumber": 103,
                        "averageGrade": 3.8,
                        "recordBookNumber": "100003"
                      }
                    ]
                    """;

            Files.writeString(file, json);

            FileDataSource<Student> dataSource =
                    new FileDataSource<>(file.toString());

            CustomCollection<Student> collection =
                    dataSource.load(10);

            assertEquals(2, collection.size());

            assertEquals(
                    101,
                    collection.get(0).getGroupNumber()
            );

            assertEquals(
                    103,
                    collection.get(1).getGroupNumber()
            );

        } catch (Exception e) {
            throw new AssertionError(
                    "Ошибка теста FileDataSource",
                    e
            );
        } finally {
            deleteQuietly(file);
        }
    }

    private static void testFileDataSourceLength() {
        Path file = null;

        try {
            file = Files.createTempFile(
                    "sorting-app-length-",
                    ".json"
            );

            String json = """
                    [
                      {
                        "groupNumber": 101,
                        "averageGrade": 4.1,
                        "recordBookNumber": "200001"
                      },
                      {
                        "groupNumber": 102,
                        "averageGrade": 4.2,
                        "recordBookNumber": "200002"
                      },
                      {
                        "groupNumber": 103,
                        "averageGrade": 4.3,
                        "recordBookNumber": "200003"
                      }
                    ]
                    """;

            Files.writeString(file, json);

            FileDataSource<Student> dataSource =
                    new FileDataSource<>(file.toString());

            CustomCollection<Student> collection =
                    dataSource.load(2);

            assertEquals(2, collection.size());

        } catch (Exception e) {
            throw new AssertionError(
                    "Ошибка проверки length",
                    e
            );
        } finally {
            deleteQuietly(file);
        }
    }

    private static void testFileDataSourceZeroLength() {
        FileDataSource<Student> dataSource =
                new FileDataSource<>(
                        "файл_не_должен_читаться.json"
                );

        CustomCollection<Student> collection =
                dataSource.load(0);

        assertTrue(collection.isEmpty());
    }

    private static void testRandomDataSource() {
        RandomDataSource<Student> dataSource =
                new RandomDataSource<>();

        CustomCollection<Student> collection =
                dataSource.load(25);

        assertEquals(25, collection.size());

        for (Student student : collection) {

            assertTrue(
                    student.getGroupNumber() > 0
            );

            assertTrue(
                    student.getAverageGrade() >= 0.0
                            && student.getAverageGrade() <= 5.0
            );

            assertTrue(
                    student.getRecordBookNumber()
                            .matches("\\d+")
            );
        }
    }

    private static void testManualDataSource() {
        InputStream originalIn = System.in;

        try {
            String input = """
                    101 4.5 300001
                    102 3.8 300002
                    """;

            System.setIn(
                    new ByteArrayInputStream(
                            input.getBytes(
                                    StandardCharsets.UTF_8
                            )
                    )
            );

            ManualDataSource dataSource =
                    new ManualDataSource();

            CustomCollection<Student> collection =
                    dataSource.load(2);

            assertEquals(2, collection.size());

            assertEquals(
                    101,
                    collection.get(0).getGroupNumber()
            );

            assertEquals(
                    "300002",
                    collection.get(1)
                            .getRecordBookNumber()
            );

        } finally {
            System.setIn(originalIn);
        }
    }

    private static void testManualDataSourceInvalidInput() {
        InputStream originalIn = System.in;

        try {
            String input =
                    "101 9.0 400001\n";

            System.setIn(
                    new ByteArrayInputStream(
                            input.getBytes(
                                    StandardCharsets.UTF_8
                            )
                    )
            );

            ManualDataSource dataSource =
                    new ManualDataSource();

            CustomCollection<Student> collection =
                    dataSource.load(1);

            assertTrue(collection.isEmpty());

        } finally {
            System.setIn(originalIn);
        }
    }

    private static void testOutputDataSource() {
        Path file = null;

        try {
            file = Files.createTempFile(
                    "sorting-app-output-",
                    ".json"
            );

            Student first =
                    student(201, 4.1, "500001");

            Student second =
                    student(202, 4.2, "500002");

            Student third =
                    student(203, 4.3, "500003");

            CustomCollection<Student> firstCollection =
                    new ArrayCustomCollection<>(2);

            firstCollection.add(first);
            firstCollection.add(second);

            OutputDataSource<Student> writer =
                    new OutputDataSource<>(
                            file.toString()
                    );

            writer.writeAppend(
                    firstCollection,
                    file.toString()
            );

            assertTrue(Files.size(file) > 0);

            CustomCollection<Student> secondCollection =
                    new ArrayCustomCollection<>(2);

            secondCollection.add(second);
            secondCollection.add(third);

            writer.writeAppend(
                    secondCollection,
                    file.toString()
            );

            FileDataSource<Student> reader =
                    new FileDataSource<>(
                            file.toString()
                    );

            CustomCollection<Student> result =
                    reader.load(10);

            /*
             * second добавляется второй раз,
             * но OutputDataSource использует
             * LinkedHashSet, поэтому дубликат
             * не должен появиться повторно.
             */
            assertEquals(3, result.size());

            assertEquals(first, result.get(0));
            assertEquals(second, result.get(1));
            assertEquals(third, result.get(2));

        } catch (Exception e) {
            throw new AssertionError(
                    "Ошибка теста OutputDataSource",
                    e
            );
        } finally {
            deleteQuietly(file);
        }
    }

    private static Student student(
            int groupNumber,
            double averageGrade,
            String recordBookNumber
    ) {
        return Student.builder()
                .groupNumber(groupNumber)
                .averageGrade(averageGrade)
                .recordBookNumber(recordBookNumber)
                .build();
    }

    private static void runTest(
            String name,
            Runnable test
    ) {
        try {
            test.run();
            passedTests++;
            System.out.println(
                    "PASS: " + name
            );
        } catch (Throwable error) {
            failedTests++;
            System.out.println(
                    "FAIL: "
                            + name
                            + " - "
                            + error.getMessage()
            );
        }
    }

    private static void assertTrue(
            boolean condition
    ) {
        if (!condition) {
            throw new AssertionError(
                    "Условие не выполнено"
            );
        }
    }

    private static void assertEquals(
            Object expected,
            Object actual
    ) {
        if (expected == null
                ? actual != null
                : !expected.equals(actual)) {

            throw new AssertionError(
                    "Ожидалось "
                            + expected
                            + ", получено "
                            + actual
            );
        }
    }

    private static void deleteQuietly(Path path) {
        if (path == null) {
            return;
        }

        try {
            Files.deleteIfExists(path);
        } catch (Exception ignored) {
        }
    }
}