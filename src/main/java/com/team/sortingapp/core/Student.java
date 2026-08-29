package com.team.sortingapp.core;

import java.util.Objects;

/**
 * Пример доменной сущности с паттерном Builder.
 * Реализацию тела методов (валидацию, equals/hashCode и т.д.)
 * дописывает разработчик, ответственный за domain-модель.
 * Сигнатура класса и Builder — зафиксированный контракт, не менять
 * без согласования с командой.
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
        // TODO: разработчику domain-модуля — вернуть groupNumber / averageGrade
        // / recordBookNumber в зависимости от fieldIndex (0, 1, 2)
        throw new UnsupportedOperationException("TODO: implement");
    }

    @Override
    public int getFieldCount() {
        return 3;
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
            // TODO: разработчику domain-модуля — добавить валидацию полей
            // (например: groupNumber > 0, averageGrade в диапазоне 0..5,
            // recordBookNumber не пустой и соответствует формату)
            return new Student(this);
        }
    }
}
