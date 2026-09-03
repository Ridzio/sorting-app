package FileDataSource;
import com.team.sortingapp.core.*;

public class FileDataSource<T extends Sortable> implements DataSource<T> {

    private final String filePath;

    public FileDataSource(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public CustomCollection<T> load(int length) {
        if (length <= 0) return new ArrayCustomCollection<>(0);

        return new ArrayCustomCollection<>(0);
    }
}
