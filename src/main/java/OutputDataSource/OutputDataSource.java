package OutputDataSource;

import FileDataSource.*;
import FileDataSource.FileDataSource;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team.sortingapp.core.*;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class OutputDataSource<T extends Sortable> implements DataWriter<T> {

    private static final String DEFAULT_PATH = "src/main/resources/defaultoutput.json";
    private final String Path;
    private final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    public OutputDataSource(String filePath) {
        this.Path = (filePath == null || filePath.isEmpty()) ? DEFAULT_PATH : filePath;
    }

    @Override
    public void writeAppend(CustomCollection<T> collection, String filePath) {
        if (collection == null || collection.isEmpty()) return;

        String targetPath = (filePath == null || filePath.isEmpty()) ? Path : filePath;

        File file = new File(targetPath);

        List<T> result = new ArrayList<>();

        if (file.exists() && file.length() > 0) {

            DataSource<T> dataSource = new FileDataSource<T>(targetPath);

            CustomCollection<T> oldCollection = dataSource.load(Integer.MAX_VALUE);

            for (T item : oldCollection) {
                result.add(item);
            }
        }

        for (T item : collection) {
            result.add(item);
        }

        try {
            mapper.writeValue(file, result);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка записи в файл: " + targetPath, e);
        }
    }
}
