import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Abstract class for general parser, child of this class will be the different parser for different programming
 * languages
 */

public abstract class  Parser {
  private static Scanner scanner;

  abstract int[] parse(File f) throws FileNotFoundException;
  abstract boolean isMethod(String s);

  public Scanner getScanner(File f) throws FileNotFoundException {
    return scanner = new Scanner(f);
  }

}

