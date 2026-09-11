package FileDataSource;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import com.team.sortingapp.core.ArrayCustomCollection;
import com.team.sortingapp.core.CustomCollection;
import com.team.sortingapp.core.DataSource;
import com.team.sortingapp.core.Sortable;
import com.team.sortingapp.core.Student;

import java.io.File;
import java.io.IOException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileDataSource<T extends Sortable> implements DataSource<T> {

    private final String filePath;
    private static final String DEFAULT_PATH = "src/main/resources/default.json";
    private final JsonFactory factory;

    public FileDataSource(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            this.filePath = DEFAULT_PATH;
        } else {
            this.filePath = filePath;
        }
        this.factory = new JsonFactory();
    }

    @Override
    public CustomCollection<T> load(int length) {
        if (length <= 0) return new ArrayCustomCollection<>(0);

        try (JsonParser parser = factory.createParser(new File(filePath))) {

            if (parser.nextToken() != JsonToken.START_ARRAY) {
                throw new RuntimeException("Ожидается JSON-массив в файле: " + filePath);
            }

            List<T> tempList = Stream.generate(() -> {
                        try {

                            if (parser.nextToken() == JsonToken.END_ARRAY) {
                                return null;
                            }
                            return parseStudent(parser);

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }).takeWhile(opt -> opt != null)
                    .limit(length)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(student -> (T) student)
                    .collect(Collectors.toList());

            ArrayCustomCollection<T> collection = new ArrayCustomCollection<>(tempList.size());
            tempList.forEach(collection::add);
            return collection;

        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении файла: " + filePath, e);
        }
    }

    private Optional<Student> parseStudent(JsonParser parser) throws IOException {
        if (parser.currentToken() != JsonToken.START_OBJECT) {
            if (parser.currentToken() == JsonToken.END_ARRAY) {
                return Optional.empty();
            }
            parser.skipChildren();
            return Optional.empty();
        }

        int groupNumber = -1;
        double averageGrade = 0.0;
        String recordBookNumber = null;
        boolean isObjectValid = true;

        while (parser.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = parser.getCurrentName();
            parser.nextToken();

            switch (fieldName) {
                case "groupNumber":
                    try {
                        groupNumber = parser.getIntValue();
                        if (groupNumber <= 0) {
                            System.err.println("Ошибка: groupNumber <= 0 (" + groupNumber + ")");
                            isObjectValid = false;
                        }
                    } catch (IOException e) {
                        System.err.println("Ошибка: groupNumber не является числом");
                        isObjectValid = false;
                    }
                    break;

                case "averageGrade":
                    try {
                        averageGrade = parser.getDoubleValue();
                        if (averageGrade < 0 || averageGrade > 5.0) {
                            System.err.println("Ошибка: averageGrade не корректен (" + averageGrade + ")");
                            isObjectValid = false;
                        }
                    } catch (IOException e) {
                        System.err.println("Ошибка: averageGrade не является числом");
                        isObjectValid = false;
                    }
                    break;

                case "recordBookNumber":
                    try {
                        recordBookNumber = parser.getText();
                        if (recordBookNumber.isEmpty()) {
                            System.err.println("Ошибка: recordBookNumber не должен быть пустым");
                            isObjectValid = false;
                        }else if (!recordBookNumber.matches("\\d+")) {
                            System.err.println("Ошибка: recordBookNumber должен содержать только цифры");
                            isObjectValid = false;
                        }
                    } catch (IOException e) {
                        System.err.println("Ошибка: recordBookNumber не является строкой");
                        isObjectValid = false;
                    }
                    break;

                default:
                    parser.skipChildren();
                    break;
            }
        }

        if (isObjectValid &&
                groupNumber > 0 &&
                recordBookNumber != null) {

            Student student = Student.builder()
                    .groupNumber(groupNumber)
                    .averageGrade(averageGrade)
                    .recordBookNumber(recordBookNumber)
                    .build();
            return Optional.of(student);
        }

        return Optional.empty();
    }
}
