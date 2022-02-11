import java.io.File;

public class FactoryNode {

    public Node create(File f) {
        if (f.isFile()) {
            return new CodeFile(f);
        } else {
            return new Package(f);
        }
    }
}

