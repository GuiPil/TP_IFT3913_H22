import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class ParserPython extends Parser {
    @Override
    int[] parse(File f) throws FileNotFoundException {
        Scanner scanner = getScanner(f);
        int line_count = 0;
        int comment_count = 0;
        boolean comment_check = false;

        while(scanner.hasNext()){
            String nextline = scanner.nextLine();

            if((nextline.contains("#") && !comment_check)){
                comment_count++;
            }

            if(comment_check){
                comment_count++;
            }
            line_count++;
        }
        System.out.println(f.getName()+" contain "+line_count+" lines");
        System.out.println(f.getName()+" contain "+comment_count+" comments");
        scanner.close();
        int[] metric = new int[2];
        metric[0] = line_count;
        metric[1] = comment_count;
        return metric;
    }

    @Override
    int getWMC(File f) throws IOException {
        return 0;
    }
}
