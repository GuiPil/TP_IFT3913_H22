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
        int predicat = 0;

        Scanner scanner = getScanner(f);
        while(scanner.hasNext()){
            String nextline = scanner.nextLine();

            //is multiline comment
            if((nextline.contains("/*") || nextline.contains("/**")) && !comment_check){
                comment_check = true;
            }
            //is /* */ or /** */
            if(nextline.contains("*/") && comment_check){
                comment_check = false;
                comment_count++;
            }

            // single line comment with
            if((nextline.contains("//") && !comment_check)){
                comment_count++;
            }

            // between multiline comment
            if(comment_check){
                comment_count++;
            }
            if(!nextline.isEmpty()) line_count++;
        }
//        System.out.println(f.getName()+" contain "+line_count+" lines");
//        System.out.println(f.getName()+" contain "+comment_count+" comments");
        scanner.close();
        int[] metric = new int[3];
        metric[0] = line_count;
        metric[1] = comment_count;
        metric[2] = getNumMethod(f);
        System.out.println(metric[0]);
        return metric;
    }

    @Override
    int getNumMethod(File f) throws IOException {

        String stringFile = "";
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);
        String line = "";
        int complexity = 0;

        while( (line = br.readLine()) != null){
           stringFile = stringFile + line + "\n";
        }

        // Pattern and regex  inspired by this stackoverflow post : https://stackoverflow.com/questions/68633/regex-that-will-match-a-java-method-declaration
        Pattern methodPattern = Pattern.compile("(public|protected|private|static|\\s)\\s+[\\w\\<\\>\\[\\]]+\\s+(\\w+)\\s*\\([^\\)]*\\) *(\\{?|[^;])");
        Matcher methodMatcher = methodPattern.matcher(stringFile);

        while(methodMatcher.find()){
            System.out.println(methodMatcher.toString());
            complexity++;
        }
        return complexity;
    }

    private int getComplexity(){
        return 0;
    }

    private int getPredicat(String line, boolean comment) {
        int numPredicat = 0;
        if (!comment) {

           String[] commentTable = line.split("//|/\\*|/\\*/");


        }
        return numPredicat;
    }
}

