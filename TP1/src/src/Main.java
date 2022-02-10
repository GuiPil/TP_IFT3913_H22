import java.io.File;
import java.io.FileNotFoundException;

public class Main {

    public static void main(String args[]) throws FileNotFoundException {

        File root = new File("../TP1/Test_files");
        TreeMetrics tree = new TreeMetrics(root, ".java");
        tree.traverse();
        tree.fetchMetrics();
        tree.toCsv(new File("../TP1/Test_output/"));
        System.out.println(root.toPath().getFileName());


    }
}
