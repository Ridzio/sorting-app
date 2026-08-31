package com.team.sortingapp;

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
                    // Временные варианты ответов
                    System.out.println("Выбор 1");
                }
                case "2" -> {
                    System.out.println("Выбор 2");
                }
                case "3" -> {
                    System.out.println("Выбор 3");
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
