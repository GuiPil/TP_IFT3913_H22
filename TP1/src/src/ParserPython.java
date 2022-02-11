import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;

/**
 * ########## Not Implemented ##########
 *
 *Just to show that i could be adapt to other languages
 *
 */
public class ParserPython extends Parser {


    @Override
    int[] parse(File f) throws IOException {
        return new int[0];
    }

    @Override
    int getNumMethod(File f) throws IOException {
        return 0;
    }

    @Override
    int getPredicat(String line, boolean comment) {
        return 0;
    }

    @Override
    boolean isInComment(String line, Matcher m) {
        return false;
    }
}
