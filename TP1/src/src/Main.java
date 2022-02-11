import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.InvalidPathException;

public class Main {

    public static void main(String args[]) {
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        if (args.length < 2){
            args = new String[] {"../TP1/Test_files", ".java", "../TP1/Test_output"};
        }
        File root = new File(args[0]);
        try {
            TreeMetrics tree = new TreeMetrics(root, args[1]);
            tree.traverse();
            tree.fetchMetrics();
            tree.toCsv(new File(args[2]));
        } catch (InvalidPathException | IOException e){
            e.printStackTrace();
        }

        //System.out.println(root.toPath().getFileName());

//        System.out.println(f0.compareTo(f1));
//        System.out.println(f0.compareTo(f2));
//        System.out.println(f1.compareTo(f2));
//        System.out.println(s2.contains(s1));


FactoryParser parser = new FactoryParser(".java");
File file = new File("../TP1/Test_files/Package1/testClass.java");
Parser p = parser.create();
p.parse(file);
//System.out.println(p.getNumMethod(file)); // doit retuner ~68


        System.out.println("Working Directory = " + System.getProperty("user.dir"));



    }
}
