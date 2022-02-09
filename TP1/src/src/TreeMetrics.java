import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class TreeMetrics {
    Node root;
    String fileExt;

    public TreeMetrics(File rootF, String ext){
        fileExt = ext;
        walk(rootF);
    }

    private void deleteTree(){
        root = null;
    }

    private void walk(File current) {
        addNode(current);
        if(current.isDirectory()){
//            System.out.println(target+" is a package");
            String[] subs = current.list();
            for(int i=0; i<subs.length; i++){
                walk(new File(current, subs[i]));
            }
        }
    }

    public void addNode(File f) {
        addNode(root, f);
    }

    private void addNode(Node current, File f) {
        if (current == null) root = new Node(f); //first package
        else if (current.children.size() == 0) current.children.add(new Node(f)); //first package in child
        else{
            // check to see if file needs to be added deeper
            for (Node child: current.children) {
                //est un folder et f est dans folder
                if (child.file.isDirectory() && f.getPath().contains(child.file.getPath())) {
                    addNode(child, f);
                    break;
                }
            }
            // sinon on ajoute dans le current
            current.children.add(new Node(f));
        }

    }

    public void traverse(){
        if (root != null){
            traverse(root);
        }else {
            System.out.println("tree is empty");
        }
    }
    private void traverse(Node current){
        System.out.println(current);
        for (Node child: current.children) {
            if(child.children.size()==0){
                System.out.println(child);
            }else{
                traverse(child);
            }
        }
    }

    public static void main(String[] args) {
        String s0 = "../TP1/Test_files";
        String s1 = "../TP1/Test_files/Package1";
        String s2 = "../TP1/Test_files/Package1/testClass.java";
        String s3 = "../TP1/Test_files/testClass2.java";
        String s4 = "../TP1/Test_files/Package1/Package2";
        String s5 = "../TP1/Test_files/Package0";

        File f0 = new File(s0);
        File f1 = new File(s1);
        File f2 = new File(s2);
        File f3 = new File(s3);
        File f4 = new File(s4);
        File f5 = new File(s5);

        TreeMetrics treeMetrics = new TreeMetrics(f0, ".java");
        treeMetrics.traverse();
        treeMetrics.deleteTree();
        treeMetrics.traverse();
    }
}


class Node {
    File file;
    Node parent;
    List<Node> children = new ArrayList<>();

    public Node(File f) {
        file = f;

    }

    @Override
    public String toString() {
        return  "file=" + file;
    }
}


//class Package extends Node{
//
//}