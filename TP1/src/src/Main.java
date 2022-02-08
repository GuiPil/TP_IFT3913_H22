import java.io.FileNotFoundException;

public class Main {

    public static void main(String args[]) throws FileNotFoundException {

       Analyser dataBase = new Analyser();
       FileRunner runner = new FileRunner(dataBase, "./repo/TP1/Test_files");
       runner.run(runner.initial);

    }
}
