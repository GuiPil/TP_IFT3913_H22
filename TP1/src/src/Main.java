import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;

public class Main {

    public static void main(String[] args) {
        if (args.length < 2) {
            args = new String[]{"../TP1/Test_project", ".java", "../Analyse/"};
        }
        File root = new File(args[0]);
        try {
            TreeMetrics tree = new TreeMetrics(root, args[1]);
            tree.traverse();
            tree.fetchMetrics();
            tree.toCsv(new File(args[2]));
        } catch (InvalidPathException | IOException e) {
            e.printStackTrace();
        }
    }
}
