package com.team.sortingapp.test;

import com.team.sortingapp.Main;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class CliIntegrationTest {

    private static int passedTests;
    private static int failedTests;

    public static void main(String[] args) {

        runTest(
                "CLI выполняет основной интеграционный сценарий",
                CliIntegrationTest::testFullCliScenario
        );

        System.out.println();
        System.out.println("=== ИТОГИ CLI ===");
        System.out.println(
                "Пройдено: " + passedTests
        );
        System.out.println(
                "Провалено: " + failedTests
        );

        if (failedTests > 0) {
            throw new AssertionError(
                    "Есть проваленные CLI тесты: "
                            + failedTests
            );
        }
    }

    private static void testFullCliScenario() {

        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;

        Path outputPath =
                Path.of(
                        "cli-integration-test-output.json"
                );

        try {
            Files.deleteIfExists(outputPath);

            /*
             * Сценарий пользователя:
             *
             * 2 - попытаться сортировать
             *     до загрузки данных
             *
             * 1 - заполнить данные
             * 1 - из файла
             * 2 - загрузить двух студентов
             *
             * 2 - сортировать
             * 1 - по номеру группы
             * 2 - Selection Sort
             * 2 - по убыванию
             *
             * 3 - записать результат
             *     в файл
             *
             * 4 - подсчитать вхождения
             *     первого Student
             *
             * 0 - выйти
             */

            String input = String.join(
                    System.lineSeparator(),
                    "2",

                    "1",
                    "1",
                    "2",

                    "2",
                    "1",
                    "2",
                    "2",

                    "3",
                    outputPath.toString(),

                    "4",
                    "101",
                    "4.2",
                    "482716",

                    "0"
            ) + System.lineSeparator();

            ByteArrayInputStream fakeInput =
                    new ByteArrayInputStream(
                            input.getBytes(
                                    StandardCharsets.UTF_8
                            )
                    );

            ByteArrayOutputStream capturedOutput =
                    new ByteArrayOutputStream();

            PrintStream fakeOutput =
                    new PrintStream(
                            capturedOutput,
                            true,
                            StandardCharsets.UTF_8
                    );

            /*
             * ВАЖНО:
             * System.in нужно заменить ДО
             * первого обращения к Main,
             * потому что Main создаёт
             * static final Scanner.
             */
            System.setIn(fakeInput);
            System.setOut(fakeOutput);

            Main.main(new String[0]);

            String output =
                    capturedOutput.toString(
                            StandardCharsets.UTF_8
                    );

            assertContains(
                    output,
                    "Сначала заполните данные"
            );

            assertContains(
                    output,
                    "Загружено из файла: 2 студентов"
            );

            assertContains(
                    output,
                    "Сортировка выполнена"
            );

            assertContains(
                    output,
                    "Selection Sort"
            );

            assertContains(
                    output,
                    "по убыванию"
            );

            assertContains(
                    output,
                    "Данные записаны в файл"
            );

            assertContains(
                    output,
                    "Найдено вхождений: 1"
            );

            assertContains(
                    output,
                    "Выход из программы"
            );

            assertTrue(
                    Files.exists(outputPath)
            );

            assertTrue(
                    Files.size(outputPath) > 0
            );

        } catch (Exception e) {
            throw new AssertionError(
                    "Ошибка интеграционного CLI теста",
                    e
            );

        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);

            try {
                Files.deleteIfExists(
                        outputPath
                );
            } catch (Exception ignored) {
            }
        }
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

    private static void assertContains(
            String text,
            String expected
    ) {
        if (!text.contains(expected)) {
            throw new AssertionError(
                    "В выводе отсутствует: "
                            + expected
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
}