package com.team.sortingapp;

import io.FileDataSource;
import io.OutputDataSource;
import io.RandomDataSource;
import io.ManualDataSource;

import com.team.sortingapp.core.*;

import java.util.Scanner;

/**
 * Точка входа. Реализация CLI-цикла — задача модуля "CLI + интеграция".
 */
public class Main {
    public static void main(String[] args) {

        Scanner scannerSelection = new Scanner(System.in);
        boolean isRunning = true;

        // TODO: разработчику CLI-модуля — реализовать меню-цикл
        System.out.println("Sorting App — TODO: implement CLI menu loop");

        while (isRunning) {

            printMenu();
            String input = scannerSelection.nextLine();

            switch (input) {
                case "1" -> {
                    DataSource<Student> dataSource = new ManualDataSource();
                    CustomCollection<Student> collection = dataSource.load(1);
                    System.out.println("Данные для добавления:");
                    for (Student student : collection) {
                        System.out.println(student);
                    }
                    DataWriter<Student> writer = new OutputDataSource<Student>(null);
                    writer.writeAppend(collection, null);
                }
                case "2" -> {
                    System.out.println("Выбор 2");
                    DataSource<Student> dataSource = new RandomDataSource<Student>();
                    CustomCollection<Student> collection = dataSource.load(5);
                    for (Student student : collection) {
                        System.out.println(student);
                    }
                }
                case "3" -> {
                    System.out.println("Выбор 3");
                    DataSource<Student> dataSource = new FileDataSource<Student>(null);
                    CustomCollection<Student> collection = dataSource.load(100);
                    for (Student student : collection) {
                        System.out.println(student);
                    }
                }
                case "0" -> {
                    System.out.println("Выход из программы");
                    isRunning = false;
                }
                default -> System.out.println("Неверный ввод. Пожалуйста, выберите пункт из меню.");
            }


        }


    }
    // Ранний внешний вариант Меню
    private static void printMenu() {
        System.out.println("---".repeat(6));
        System.out.println("-Главное меню-");
        System.out.println("1. xxx");
        System.out.println("2. xxx");
        System.out.println("3. xxx");
        System.out.println("0. Выйти");
        System.out.print("Ваш выбор: ");
    }
}
