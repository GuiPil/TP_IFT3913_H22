import java.io.File;

/**
 * Factory Node
 */
public class FactoryNode {

    /**
     * Create TreeMetrics Node based on file
     * @param f File used to create a node.
     * @return CodeFile if f is a File, else returns a Package instance.
     */
    public Node create(File f) {
        if (f.isFile()) {
            return new CodeFile(f);
        } else {
            return new Package(f);
        }
    }
}

