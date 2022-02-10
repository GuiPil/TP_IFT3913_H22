import java.io.File;
import java.io.FileNotFoundException;

public class Main {

    public static void main(String args[]) throws FileNotFoundException {
//        String s0 = "../TP1/Test_files";
//        String s1 = "../TP1/Test_files/Package1";
//        String s2 = "../TP1/Test_files/Package1/testClass.java";
//        String s3 = "../TP1/Test_files/testClass2.java";
//        String s4 = "../TP1/Test_files/Package1/Package2";
//        String s5 = "../TP1/Test_files/Package0";
//
//        File f0 = new File(s0);
//        File f1 = new File(s1);
//        File f2 = new File(s2);
//        File f3 = new File(s3);
//        File f4 = new File(s4);
//        File f5 = new File(s5);
//
//        System.out.println(f4.getPath().contains(f1.getPath()));
//        System.out.println(f4.getPath().contains(f5.getPath()));


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
