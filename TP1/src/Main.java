import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.concurrent.TimeUnit;

public class Main {
    // the args should be { path of file to analyse, extension, path to put the results (csv files)
    public static void main(String[] args) throws InterruptedException {
        if (args.length < 3){
            System.out.println("Wrong number of argument (3 needed), default has been use");
            TimeUnit.MILLISECONDS.sleep(5000);
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