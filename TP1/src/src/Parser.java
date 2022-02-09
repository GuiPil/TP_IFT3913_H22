import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Parser {

    Analyser mainAnalyser;

    public Parser(Analyser a) {
       this.mainAnalyser = a;
    }

    public void parse(File f) throws FileNotFoundException {
        File file = f;
        Scanner scanner = new Scanner(file);



        int line_count = 0;
        int comment_count = 0;
        boolean comment_check = false;

        while(scanner.hasNext()){
            String nextline = scanner.nextLine();

            if((nextline.contains("/*") || nextline.contains("/**")) && !comment_check){
                comment_check = true;
            }


            System.out.println(comment_check);

            if(nextline.contains("*/") && comment_check){
                comment_check = false;
                comment_count++;
            }

            if((nextline.contains("//") && !comment_check) || ((nextline.contains("/*") || nextline.contains("/**")) && nextline.contains("*/"))){
                comment_count++;
            }




            if(comment_check){
                comment_count++;
            }
            line_count++;
        }
        System.out.println(file.getName()+" contain "+line_count+" lines");
        System.out.println(file.getName()+" contain "+comment_count+" comments");
        scanner.close();
    }
}