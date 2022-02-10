import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * Abstract class for general parser, child of this class will be the different parser for different programming
 * languages
 */

public abstract class  Parser {
  private static Scanner scanner;

  abstract int[] parse(File f) throws IOException;
  abstract int getWMC(File f) throws IOException;

  public Scanner getScanner(File f) throws FileNotFoundException {
    return scanner = new Scanner(f);
  }

}

