import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data structure corresponding to the directory to parse. Each node is a Folder (Node) with childrens or a file (leaf)
 * with no children
 */
public class TreeMetrics {
    Node root;
    static File output;
    public static Parser parser;

    /**
     * Constructor
     *
     * @param rootF root file of the Tree.
     */
    public TreeMetrics(File rootF, String ext) {
        if (rootF.isDirectory()) {
            walk(rootF);
            parser = new FactoryParser(ext).create();
        } else {
            throw new InvalidPathException(rootF.getPath(), "Path should be a folder directory");
        }
    }

    /**
     * Reset tree root to null
     */
    private void deleteTree() {
        root = null;
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
        if (current == null) root = new Node(f); //first package
        else if (current.children.size() == 0) current.children.add(new Node(f)); //first package in child
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
            current.children.add(new Node(f));
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

    private void traverse(Node current) {
        System.out.println(current);
        for (Node child : current.children) {
            traverse(child);
        }
    }

    public void toCsv(File out){
        if (out.isFile()){
            output = out;
        }
        if (root != null){
            try{
                FileWriter fileWriter = new FileWriter(output);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

/**
 * Node of the TreeMetrics
 */
class Node {
    File file;
    int loc;
    int cloc;
    float dc;
    List<Node> children = new ArrayList<>();

    public Node(File f) {
        file = f;
    }

    private boolean isFile() {
        return children.size() == 0 && file.isFile();
    }

    private boolean isPackage() {
        return file.isDirectory();
    }

    public void updateMetric() {
        if (isFile()) {
            loc = (int) (Math.random() * 10); //TreeMetrics.parser.parseLoc();
            cloc = (int) (Math.random() * 10); //TreeMetrics.parser.parsecCloc();
            dc = cloc / loc;
        } else {
            if (children.size() == 0) {
                loc = 0;
                cloc = 0;
                dc = 0.0f;
            }
            for (Node child : children) {
                loc += child.loc;
                cloc += child.cloc;
            }
            dc = loc / cloc;
        }
    }

    private String ToCsv(){
        String sep = ",";
        String endLine = "\n";
        String row = file.getPath() + sep
                + file.getName() + sep
                + loc + sep
                + cloc + sep
                + dc + sep
                + endLine;
        return row;
    }

    public void writeToCsv() {
        if (TreeMetrics.output != null && TreeMetrics.output.isFile()){
            try{
                FileWriter writer = new FileWriter(TreeMetrics.output);
                writer.write(ToCsv());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        return "file=" + file;
    }


}
