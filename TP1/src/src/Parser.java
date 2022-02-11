import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;

/**
 * Abstract class for general parser, child of this class will be the different parser for different programming
 * languages
 */
public abstract class Parser {
    private static Scanner scanner;

    abstract int[] parse(File f) throws IOException;

    abstract int getNumMethod(File f) throws IOException;

    abstract int getPredicat(String line, boolean comment);

    abstract boolean isInComment(String line, Matcher m);

    public Scanner getScanner(File f) throws FileNotFoundException {
        return scanner = new Scanner(f);
    }

}

