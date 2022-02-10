import java.io.File;
import java.io.FileNotFoundException;

public class Main {

    public static void main(String args[]) throws FileNotFoundException {

        File root = new File("../TP1/Test_files");
        TreeMetrics tree = new TreeMetrics(root, ".java");
        tree.traverse();
        tree.fetchMetrics();
        tree.toCsv(new File("../TP1/Test_output/"));
        //System.out.println(root.toPath().getFileName());

//        System.out.println(f0.compareTo(f1));
//        System.out.println(f0.compareTo(f2));
//        System.out.println(f1.compareTo(f2));
//        System.out.println(s2.contains(s1));

        FactoryParser parser = new FactoryParser(".java");
        File file = new File("../TP1/Test_files/Package1/testClass.java");
        Parser p = parser.create();
        p.parse(file);



    }
}
