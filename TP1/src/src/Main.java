public class Main {

    public static void main(String args[]){

       Analyser dataBase = new Analyser();
       FileRunner runner = new FileRunner(dataBase, "../TP1/Test_files");
       runner.run(runner.initial);

    }
}
