package FileDataSource;

import com.team.sortingapp.core.ArrayCustomCollection;
import com.team.sortingapp.core.CustomCollection;
import com.team.sortingapp.core.DataSource;
import com.team.sortingapp.core.Sortable;
import com.team.sortingapp.core.Student;

import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class RandomDataSource<T extends Sortable> implements DataSource<T> {

    private final Random random = new Random();

    @Override
    public CustomCollection<T> load(int length) {
        if (length <= 0) {
            return new ArrayCustomCollection<>(0);
        }

        List<T> tempList = Stream.generate(this::generate)
                .limit(length)
                .map(student -> (T) student)
                .toList();

        return tempList.stream()
                .collect(ArrayCustomCollection.toCustomCollection(tempList.size()));
    }

    private Student generate() {
        int groupNumber = random.nextInt(99999) + 1;
        double averageGrade = Math.floor((random.nextDouble() * 5.0) * 100) / 100.0;
        String recordBookNumber = String.valueOf(random.nextInt(99999) + 1);

        return Student.builder()
                .groupNumber(groupNumber)
                .averageGrade(averageGrade)
                .recordBookNumber(recordBookNumber)
                .build();
    }
}
