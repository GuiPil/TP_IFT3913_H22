import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ParserJava is parse file .java with the adequate commenting format
 */
public class ParserJava extends Parser {

    /**
     * will extract the metric necessary for analysing, by parsing the document
     *
     * @param f file to be read
     * @return an int table contiaining [Loc,Cloc,Complexity]
     * @throws IOException
     */

    @Override
    int[] parse(File f) throws IOException {

        int line_count = 0;
        int comment_count = 0;
        boolean comment_check = false;
        int predicat = 0;

        Scanner scanner = getScanner(f);
        while (scanner.hasNext()) {
            String nextline = scanner.nextLine();

            //is multiline comment
            if ((nextline.contains("/*") || nextline.contains("/**")) && !comment_check) {
                comment_check = true;
            }
            //is /* */ or /** */
            if (nextline.contains("*/") && comment_check) {
                comment_check = false;
                comment_count++;
            }

            // single line comment with
            if ((nextline.contains("//") && !comment_check)) {
                comment_count++;
            }

            // between multiline comment
            if (comment_check) {
                comment_count++;
            }

            predicat += getPredicat(nextline, comment_check);

            if (!nextline.isEmpty()) line_count++;
        }
//        System.out.println(f.getName()+" contain "+line_count+" lines");
//        System.out.println(f.getName()+" contain "+comment_count+" comments");
        scanner.close();
        int[] metric = new int[3];
        metric[0] = line_count;
        metric[1] = comment_count;
        metric[2] = getNumMethod(f) + predicat;
        System.out.println(metric[0]);
        System.out.println(predicat);
        System.out.println(metric[2]);

        return metric;
    }

    /**
     * will extract the number of method in the class files
     *
     * @param f file to be read
     * @return number of method in the files
     * @throws IOException
     */

    @Override
    int getNumMethod(File f) throws IOException {

        String stringFile = "";
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);
        String line = "";
        int numMethod = 0;

        while ((line = br.readLine()) != null) {
            stringFile = stringFile + line + "\n";
        }

        // Pattern and regex  inspired by this stackoverflow post : https://stackoverflow.com/questions/68633/regex-that-will-match-a-java-method-declaration
        Pattern methodPattern = Pattern.compile("(public|protected|private|static)\\s+[\\w\\<\\>\\[\\]]+\\s+(\\w+)\\s*\\([^\\)]*\\) *(\\{?|[^;])");
        Matcher methodMatcher = methodPattern.matcher(stringFile);

        while (methodMatcher.find()) {
            //System.out.println(methodMatcher.toString());
            numMethod++;
        }
        return numMethod;
    }

    /**
     * will extract the total number of predicat (if, while,...) in the file
     *
     * @param line the current line in the parse method
     * @param comment a bool indicating if is in a multi line comment
     * @return a estimate number of the predicat in a file
     */
    private int getPredicat(String line, boolean comment) {
        int numPredicat = 0;
        if (!comment) {

            Pattern predicatPattern = Pattern.compile("(else\\s+if\\s*\\(|if\\s*\\(|while\\s*\\(|for\\s*\\(|do\\s*\\{|case\\s*.+:)");
            Matcher predicatMatcher = predicatPattern.matcher(line);

            while(predicatMatcher.find()) {
                System.out.println(predicatMatcher.toString());

                if(isInComment(line,predicatMatcher)){
                    return numPredicat;
                }
                numPredicat = 1;
                return numPredicat;
            }
        }
        return numPredicat;
    }

    /**
     * indicate if a predicat is in a comment or not
     *
     * @param line current line being parse
     * @param m Matcher containing the predicat
     * @return True if the predicat is part of a comment, false otherwise
     */
    private boolean isInComment(String line, Matcher m){

        // the toString method give too much information so we isolate the predicate by splitig at keyword
        String[] found = m.toString().split("lastmatch=");
        // same as above but with blanks
        String[] temp = found[1].split("\\s+");
        String predicat= temp[0];

        //we split on the predicat tho obtain a before and after
        String[] inComment = line.split(predicat, 2);
        boolean cond1 = inComment[0].contains("//");
        boolean cond2 = (inComment[0].contains("/*") && inComment[1].contains("*/"));
        boolean cond3 = (inComment[0].contains("/**") && inComment[1].contains("*/"));;

        if(cond1 || cond2 || cond3){
            return true;
        }
        return false;
    }
}
