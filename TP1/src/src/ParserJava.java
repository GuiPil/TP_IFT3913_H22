import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * ParserJava is parse file .java with the adequate commenting format
 */
public class ParserJava extends Parser{

    @Override
    int[] parse(File f) throws FileNotFoundException {

        int line_count = 0;
        int comment_count = 0;
        boolean comment_check = false;

        Scanner scanner = getScanner(f);
        while(scanner.hasNext()){
            String nextline = scanner.nextLine();

            if((nextline.contains("/*") || nextline.contains("/**")) && !comment_check){
                comment_check = true;
            }

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
//        System.out.println(f.getName()+" contain "+line_count+" lines");
//        System.out.println(f.getName()+" contain "+comment_count+" comments");
        scanner.close();
        int[] metric = new int[2];
        metric[0] = line_count;
        metric[1] = comment_count;
        return metric;
    }

    @Override
    boolean isMethod(String s) {
        return false;
    }
}

