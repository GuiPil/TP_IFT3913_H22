import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ParserJava is parse file .java with the adequate commenting format
 */
public class ParserJava extends Parser{

    @Override
    int[] parse(File f) throws IOException {

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
        int[] metric = new int[3];
        metric[0] = line_count;
        metric[1] = comment_count;
        metric[2] = getWMC(f);
        return metric;
    }

    @Override
    int getWMC(File f) throws IOException {

        String stringFile = "";
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);
        String line = "";
        int complexity = 0;

        while( (line = br.readLine()) != null){
           stringFile = stringFile + line + "\n";
        }

        Pattern methodPattern = Pattern.compile("(public|protected|private|static) +[\\w\\<\\>\\[\\]]+\\s+(\\w+) *\\([^\\)]*\\) *(\\{?|[^;])");
        Matcher methodMatcher = methodPattern.matcher(stringFile);

        while(methodMatcher.find()){
            System.out.println(methodMatcher.toString());
            complexity++;
        }
        return complexity;
    }

}

