import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Data structure corresponding to the directory to parse. Each node is a Folder (Node) with childrens or a file (leaf)
 * with no children
 */
public class TreeMetrics {
    Package root;
    FactoryNode nodeFactory;
    static File outputDir;
    public static Parser parser;


    /**
     * Constructor
     *
     * @param rootF root file of the Tree.
     */
    public TreeMetrics(File rootF, String ext) {
        if (rootF.isDirectory()) {
            nodeFactory = new FactoryNode();
            walk(rootF);
            parser = new FactoryParser(ext).create();
        } else {
            throw new InvalidPathException(rootF.getPath(), "Path should be a folder directory");
        }
    }

    /**
     * Recursively walk the file system from the current file and create a node for each file and folder.
     *
     * @param current
     */
    private void walk(File current) {
        addNode(current);
        if (current.isDirectory()) {
//            System.out.println(target+" is a package");
            String[] subs = current.list();
            for (int i = 0; i < subs.length; i++) {
                walk(new File(current, subs[i]));
            }
        }
    }

    /**
     * Adding a node from the root.
     *
     * @param f file to add
     */
    public void addNode(File f) {
        addNode(root, f);
    }

    /**
     * Recursively walk the tree structure until it adds the first node, or finds a root with no children. Else, walk
     * the tree in the direction that is included in the file to add.
     *
     * @param current
     * @param f
     */
    private void addNode(Node current, File f) {
        if (current == null) root = (Package) nodeFactory.create(f); //first package
        else if (current.children.size() == 0) current.children.add(nodeFactory.create(f));
        else {
            // check to see if file needs to be added deeper
            for (Node child : current.children) {
                //est un folder et f est dans folder
                if (child.file.isDirectory() && f.getPath().contains(child.file.getPath())) {
                    addNode(child, f);
                    return;
                }
            }
            // sinon on ajoute dans le current
            current.children.add(nodeFactory.create(f));
        }

    }

    /**
     * Size from the root
     *
     * @return int : size from the root
     */
    public int size() {
        return size(root);
    }

    /**
     * Get the size from a Node
     *
     * @param current
     * @return int : size from the node
     */
    private int size(Node current) {
        if (current.children.size() == 0) return 1;
        else {
            int sum = 1;
            for (Node child : current.children) {
                sum += size(child);
            }
            return sum;
        }
    }

    /**
     * print all node and leafs from the tree.
     */
    public void traverse() {
        if (root != null) {
            traverse(root);
        } else {
            System.out.println("tree is empty");
        }
    }

    /**
     * Recursively traverse from the current node and print the node.
     *
     * @param current Node
     */
    private void traverse(Node current) {
        System.out.println(current);
        for (Node child : current.children) {
            traverse(child);
        }
    }

    /**
     * Update the metrics tree
     *
     * @throws IOException
     */
    public void fetchMetrics() throws IOException {
        if (root != null) {
            fetchFileMetrics(root); //walks the tree to update files
            fetchPackageMetrics(root); //walks the tree to update the package
        } else {
            System.out.println("tree is empty");
        }
    }

    /**
     * Walks the tree and update the metrics for everyfiles
     *
     * @param current
     */
    private void fetchFileMetrics(Node current) {
        if (current.isFile()) current.updateMetric();
        for (Node child : current.children) {
            fetchFileMetrics(child);
        }
    }

    /**
     * Walks the tree and update the metrics for every package
     * make sure files have been updated first.
     *
     * @param current
     */
    private void fetchPackageMetrics(Node current) {
        if (current.isPackage()) current.updateMetric();
        for (Node child : current.children) {
            if (child.isPackage()) {
                System.out.println("update Package");
                fetchPackageMetrics(child);
            }
        }
    }

    /**
     * Evaluate Weighted Complexity based on the subtree of current node.
     *
     * @param current
     * @return the Weighted Complexity of the file or the package depending on the current nodes type.
     */
    public static int pkgWmc(Node current) {
        if (current.children.size() == 0) return current.getComplexity();
        int complexity = 0;
        for (Node child : current.children) {
            complexity += pkgWmc(child);
        }
        return complexity;
    }

    public void toCsv(File folder) {
        if (!folder.exists()) folder.mkdirs();
        if (root != null) {
            outputDir = folder;
            toCsv(root);
        }

    }

    private void toCsv(Node current) {
        current.writeToCsv();
        for (Node child : current.children) {
            toCsv(child);
        }

    }

}

/**
 * Node of the TreeMetrics
 */
abstract class Node {
    final String SEP = ",";
    int loc;
    int cloc;
    float dc;
    float bc;
    File file;
    List<Node> children = new ArrayList<>();

    public Node(File f) {
        file = f;
    }

    public boolean isFile() {
        return this instanceof CodeFile;
    }

    public boolean isPackage() {
        return this instanceof Package;
    }

    abstract int getComplexity();

    abstract String toCsv();

    abstract public void updateMetric();

    abstract public void writeToCsv();

    abstract ArrayList<String> getHeader();

    String csvHeader() {
        String row = "";
        ArrayList<String> header = getHeader();
        for (int i = 0; i < header.size(); i++) {
            if (i == header.size() - 1) {
                row += header.get(i) + "\n";
            } else {
                row += header.get(i) + SEP;
            }
        }
        return row;
    }

    @Override
    public String toString() {
        return "file=" + file;
    }
}

class CodeFile extends Node {
    int wmc;

    private static ArrayList<String> HEADER =
            new ArrayList<>(Arrays.asList("chemin", "class", "classe_LOC", "classe_CLOC", "classe_DC", "WMC", "classe_BC"));

    public CodeFile(File f) {
        super(f);
    }

    @Override
    int getComplexity() {
        return wmc;
    }

    @Override
    public void updateMetric() {
        try {
            int[] data = TreeMetrics.parser.parse(file);
            loc = data[0];
            cloc = data[1];
            wmc = data[2];
            dc = loc == 0 ? 0 : cloc / (float) loc;
            bc = wmc == 0 ? 0 : dc / (float) wmc;
        } catch (IOException e) {
            System.out.println("Could not parse the metrics in file " + file.getName());
            e.printStackTrace();
        }
    }

    @Override
    String toCsv() {
        String endLine = "\n";
        String row = file.getPath() + SEP
                + file.toPath().getFileName() + SEP
                + loc + SEP
                + cloc + SEP
                + dc + SEP
                + wmc + SEP
                + bc + endLine;
        return row;
    }

    @Override
    ArrayList<String> getHeader() {
        return HEADER;
    }

    @Override
    public void writeToCsv() {
        if (TreeMetrics.outputDir != null) {
            try {
                String path = TreeMetrics.outputDir.getPath() + "/classes.csv";
                FileWriter writer = new FileWriter(path, true);
                if (new File(path).length() == 0) {
                    writer.write(csvHeader());
                }
                writer.write(toCsv());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

class Package extends Node {
    int wcp;
    private static final ArrayList<String> HEADER =
            new ArrayList<>(Arrays.asList("chemin", "paquet", "paquet_LOC", "paquet_CLOC", "paquet_DC", "WCP", "paquet_BC"));

    public Package(File f) {
        super(f);
    }

    @Override
    int getComplexity() {
        return wcp;
    }

    @Override
    public void updateMetric() {
        System.out.println("In updateMetricS from package  " + file.getName());
        for (Node child : children) {
            // metrics is based on files in package only
            if (child.isFile()) {
                System.out.println("YUE " + child.loc);
                loc += child.loc;
                cloc += child.cloc;
            }
        }
        wcp = TreeMetrics.pkgWmc(this); //fetching for file, and subpackage
        dc = loc == 0 ? 0 : cloc / (float) loc;
        bc = wcp == 0 ? 0 : dc / (float) wcp;
    }

    @Override
    public void writeToCsv() {
        if (TreeMetrics.outputDir != null) {
            try {
                String path = TreeMetrics.outputDir.getPath() + "/paquet.csv";
                FileWriter writer = new FileWriter(path, true);
                if (new File(path).length() == 0) {
                    writer.write(csvHeader());
                }
                writer.write(toCsv());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    String toCsv() {
        String endLine = "\n";
        String row = file.getPath() + SEP
                + file.toPath().getFileName() + SEP
                + loc + SEP
                + cloc + SEP
                + dc + SEP
                + wcp + SEP
                + bc + endLine;
        return row;
    }

    @Override
    ArrayList<String> getHeader() {
        return HEADER;
    }
}
