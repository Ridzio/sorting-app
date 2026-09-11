package com.team.sortingapp.core;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;

/**
 * Пример доменной сущности с паттерном Builder.
 * Реализацию тела методов (валидацию, equals/hashCode и т.д.)
 * дописывает разработчик, ответственный за domain-модель.
 */
public class Student implements Sortable {

    private final int groupNumber;
    private final double averageGrade;
    private final String recordBookNumber;

    private Student(Builder builder) {
        this.groupNumber = builder.groupNumber;
        this.averageGrade = builder.averageGrade;
        this.recordBookNumber = builder.recordBookNumber;
    }

    public static Builder builder() {
        return new Builder();
    }

    public int getGroupNumber() {
        return groupNumber;
    }

    public double getAverageGrade() {
        return averageGrade;
    }

    public String getRecordBookNumber() {
        return recordBookNumber;
    }

    @Override
    public Comparable<?> getFieldByIndex(int fieldIndex) {
        // Логика для Роли 2 (Стратегии сортировки)
        return switch (fieldIndex) {
            case 0 -> groupNumber;       // Integer реализует Comparable
            case 1 -> averageGrade;      // Double реализует Comparable
            case 2 -> recordBookNumber;  // String реализует Comparable
            default -> throw new IllegalArgumentException("Неверный индекс поля: " + fieldIndex);
        };
    }

    @JsonIgnore
    @Override
    public int getFieldCount() {
        return 3;
    }

    // Переопределяем для тестов (Роль 4) и вывода (Роль 3)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return groupNumber == student.groupNumber &&
                Double.compare(student.averageGrade, averageGrade) == 0 &&
                Objects.equals(recordBookNumber, student.recordBookNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupNumber, averageGrade, recordBookNumber);
    }

    @Override
    public String toString() {
        return "Student{" +
                "groupNumber=" + groupNumber +
                ", averageGrade=" + averageGrade +
                ", recordBookNumber='" + recordBookNumber + '\'' +
                '}';
    }

    public static class Builder {
        private int groupNumber;
        private double averageGrade;
        private String recordBookNumber;

        public Builder groupNumber(int groupNumber) {
            this.groupNumber = groupNumber;
            return this;
        }

        public Builder averageGrade(double averageGrade) {
            this.averageGrade = averageGrade;
            return this;
        }

        public Builder recordBookNumber(String recordBookNumber) {
            this.recordBookNumber = recordBookNumber;
            return this;
        }

        public Student build() {
            // Валидация полей согласно ТЗ
            if (groupNumber <= 0) {
                throw new IllegalArgumentException("Номер группы должен быть больше 0");
            }
            if (averageGrade < 0.0 || averageGrade > 5.0) {
                throw new IllegalArgumentException("Средний балл должен быть в диапазоне от 0.0 до 5.0");
            }
            if (recordBookNumber == null || recordBookNumber.isBlank()) {
                throw new IllegalArgumentException("Номер зачётки не может быть пустым");
            }

            return new Student(this);
        }
    }
}
