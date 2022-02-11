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
    public static Parser parser;
    static File outputDir;
    Package root;
    FactoryNode nodeFactory;
    String ext;


    /**
     * Constructor
     *
     * @param rootF root file of the Tree.
     */
    public TreeMetrics(File rootF, String ext) {
        if (rootF.isDirectory()) {
            nodeFactory = new FactoryNode();
            this.ext = ext;
            walk(rootF);
            parser = new FactoryParser().create(ext);
        } else {
            throw new InvalidPathException(rootF.getPath(), "Path should be a folder directory");
        }
    }

    /**
     * Evaluate Weighted Complexity based on the subtree of current node.
     *
     * @param current
     * @return the Weighted Complexity of the file or the package depending on the current nodes type.
     */
    public static int pkgWcp(Node current) {
        int complexity = 0;
        if (current.isFile()) return current.getComplexity();
        if (current.isPackage()) {
            for (Node child : current.children) {
                complexity += pkgWcp(child);
            }
        }
        return complexity;
    }

    /**
     * Recursively walk the file system from the current file and create a node for each file and folder.
     *
     * @param current
     */
    private void walk(File current) {
        addNode(current);
        if (current.isDirectory()) {
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
        if (f.isFile() && !f.getPath().endsWith(ext)) return; // skipping file
        if (current == null) root = (Package) nodeFactory.create(f); //first package
        else if (current.isPackage() && current.children.size() == 0) current.children.add(nodeFactory.create(f));
        else {
            // check to see if file needs to be added deeper
            for (Node child : current.children) {
                // is file deeper than current node
                if (child.file.isDirectory() && f.getPath().contains(child.file.getPath())) {
                    addNode(child, f);
                    return;
                }
            }
            // insert in current node
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
        if (current.isPackage()) {
            for (Node child : current.children) {
                traverse(child);
            }
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
        if (current.isPackage()) {
            for (Node child : current.children) {
                fetchFileMetrics(child);
            }
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
        if (current.isPackage()) {
            for (Node child : current.children) {
                if (child.isPackage()) {
                    fetchPackageMetrics(child);
                }
            }
        }
    }

    /**
     * Writing tree to csv node by node from root.
     *
     * @param folder
     */
    public void toCsv(File folder) {
        if (!folder.exists()) folder.mkdirs();
        if (root != null) {
            outputDir = folder;
            toCsv(root);
        }
    }

    /**
     * Writing the current node to csv
     *
     * @param current
     */
    private void toCsv(Node current) {
        current.writeToCsv();
        if (current.isPackage()) {
            for (Node child : current.children) {
                toCsv(child);
            }
        }
    }
}

/**
 * Abstract Node of the TreeMetrics
 */
abstract class Node {

    /**
     * None empty line of code
     */
    int loc;

    /**
     * Comment line of code
     */
    int cloc;

    /**
     * Density of code  => cloc/loc
     */
    float dc;

    /**
     * Degree of comment => dc/complexity  : comlexity is either wmc(CodeFile) or wcp(Package)
     */
    float bc;

    /**
     * File object corresponding to the path
     */
    File file;

    /**
     * List of Node : Null if CodeFile
     */
    List<Node> children;

    /**
     * @param f File object containing the file Node path.
     */
    public Node(File f) {
        file = f;
    }

    /**
     * Check if it's a CodeFile instance
     *
     * @return true if instance is of type CodeFile false otherwise
     */
    public boolean isFile() {
        return this instanceof CodeFile;
    }

    /**
     * Check if it's a Package instance
     *
     * @return true if instance is of type Package false otherwise
     */
    public boolean isPackage() {
        return this instanceof Package;
    }

    /**
     * Abstract methode to get the complexity
     *
     * @return metric to evaluate the complexity
     */
    abstract int getComplexity();

    /**
     * Abstract method to convert the node to a string with properties seperated by commas.
     *
     * @return Csv String with every values
     */
    abstract String toCsv();

    /**
     * Evaluate the node metrics
     */
    abstract public void updateMetric();

    /**
     * Write one row in csv format
     */
    abstract public void writeToCsv();

    /**
     * Getter for the header used in the file
     *
     * @return ArrayList<String> of the header in the csv
     */
    abstract ArrayList<String> getHeader();

    /**
     * Format the header values in csv format
     *
     * @return String of the header in csv format
     */
    String csvHeader() {
        String row = "";
        ArrayList<String> header = getHeader();
        for (int i = 0; i < header.size(); i++) {
            if (i == header.size() - 1) {
                row += header.get(i) + "\n";
            } else {
                row += header.get(i) + ",";
            }
        }
        return row;
    }

    @Override
    public String toString() {
        return "file=" + file;
    }
}

/**
 * Node corresponding to a File with code
 */
class CodeFile extends Node {
    /**
     * Header to used for the csv export
     */
    private static final ArrayList<String> HEADER = new ArrayList<>(Arrays.asList("chemin", "class", "classe_LOC", "classe_CLOC", "classe_DC", "WMC", "classe_BC"));
    /**
     * Weighted Methods per Class (complexity metric for a class)
     */
    int wmc;

    /**
     * Constructor of a CodeFile.
     *
     * @param f File object containing the relative path to the file.
     */
    public CodeFile(File f) {
        super(f);
    }

    /**
     * Complexity metric for the file
     *
     * @return
     */
    @Override
    int getComplexity() {
        return wmc;
    }

    /**
     * Read the file with the TreeMetric parser and evaluate the metrics for the file.
     */
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

    /**
     * Convert every property in a comma seperated String corresponding to a row.
     */
    @Override
    String toCsv() {
        String endLine = "\n";
        String row = file.getPath() + "," + file.toPath().getFileName() + "," + loc + "," + cloc + "," + dc + "," + wmc + "," + bc + endLine;
        return row;
    }

    /**
     * Return the header property
     *
     * @return
     */
    @Override
    ArrayList<String> getHeader() {
        return HEADER;
    }

    /**
     * If the file is new, writes the header first, otherwise just add the node to the file.
     */
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

/**
 * Node corresponding to a directory, package or module.
 */
class Package extends Node {
    /**
     * Header to used for the csv export
     */
    private static final ArrayList<String> HEADER = new ArrayList<>(Arrays.asList("chemin", "paquet", "paquet_LOC", "paquet_CLOC", "paquet_DC", "WCP", "paquet_BC"));
    /**
     * Weighted Classes per Package (Complexity metric for the package)
     */
    int wcp;

    /**
     * Package constructor
     *
     * @param f File object containing the relative path to the directory.
     */
    public Package(File f) {
        super(f);
        children = new ArrayList<>();
    }

    /**
     * Getter for the complexity metric of the package
     *
     * @return Weighted Classes per Package
     */
    @Override
    int getComplexity() {
        return wcp;
    }

    /**
     * Evaluate the metrics for the package.
     */
    @Override
    public void updateMetric() {
        for (Node child : children) {
            // metrics is based on files in package only
            if (child.isFile()) {
                loc += child.loc;
                cloc += child.cloc;
            }
        }
        wcp = TreeMetrics.pkgWcp(this); //fetching for file, and subpackage
        dc = loc == 0 ? 0 : cloc / (float) loc;
        bc = wcp == 0 ? 0 : dc / (float) wcp;
    }

    /**
     * If the package is new, writes the header first, otherwise just add the node to the file.
     */
    @Override
    public void writeToCsv() {
        if (TreeMetrics.outputDir != null) {
            try {
                String path = TreeMetrics.outputDir.getPath() + "/paquets.csv";
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

    /**
     * Convert every property in a comma seperated String corresponding to a row.
     */
    @Override
    String toCsv() {
        String endLine = "\n";
        String row = file.getPath() + "," + file.toPath().getFileName() + "," + loc + "," + cloc + "," + dc + "," + wcp + "," + bc + endLine;
        return row;
    }

    /**
     * Return the header property
     *
     * @return
     */
    @Override
    ArrayList<String> getHeader() {
        return HEADER;
    }
}
