package FileDataSource;

import com.team.sortingapp.core.ArrayCustomCollection;
import com.team.sortingapp.core.CustomCollection;
import com.team.sortingapp.core.DataSource;
import com.team.sortingapp.core.Student;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Stream;

public class ManualDataSource implements DataSource<Student> {

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public CustomCollection<Student> load(int length) {
        if (length <= 0) {
            return new ArrayCustomCollection<>(0);
        }

        System.out.println("Введите данные студентов в формате: 'groupNumber averageGrade recordBookNumber'");
        System.out.println("Для завершения введите пустую строку.");

        List<Student> tempList = Stream.generate(() -> {
                    System.out.print("> ");
                    String line = scanner.nextLine();
                    if (line.trim().isEmpty()) {
                        return Optional.<Student>empty();
                    }
                    return parseStudent(line);
                })
                .takeWhile(Optional::isPresent)
                .limit(length)
                .map(Optional::get)
                .toList();

        System.out.printf("Введено %d студентов%n", tempList.size());

        return tempList.stream()
                .collect(ArrayCustomCollection.toCustomCollection(tempList.size()));
    }

    private Optional<Student> parseStudent(String line) {
        String[] parts = line.trim().split("\\s+");
        if (parts.length < 3) {
            System.err.println("Ошибка: нужно ввести 3 значения, разделённые пробелами.");
            return Optional.empty();
        }

        int groupNumber = -1;
        double averageGrade = -1.0;
        String recordBookNumber = null;

        try {
            groupNumber = Integer.parseInt(parts[0]);
            if (groupNumber <= 0) {
                System.err.println("Ошибка: groupNumber должен быть положительным числом.");
                return Optional.empty();
            }

        } catch (NumberFormatException e) {
            System.err.println("Ошибка: groupNumber должен быть целым числом.");
            return Optional.empty();
        }

        try {
            averageGrade = Double.parseDouble(parts[1]);
            if (averageGrade < 0 || averageGrade > 5.0) {
                System.err.println("Ошибка: averageGrade должен быть от 0 до 5.0.");
                return Optional.empty();
            }
        } catch (NumberFormatException e) {
            System.err.println("Ошибка: averageGrade должен быть числом.");
            return Optional.empty();
        }

        recordBookNumber = parts[2];
        if (!recordBookNumber.matches("\\d+")) {
            System.err.println("Ошибка: recordBookNumber должен содержать только цифры.");
            return Optional.empty();
        }

        Student student = Student.builder()
                .groupNumber(groupNumber)
                .averageGrade(averageGrade)
                .recordBookNumber(recordBookNumber)
                .build();

        return Optional.of(student);
    }
}