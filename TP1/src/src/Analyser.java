import com.sun.source.tree.Tree;

public class Analyser {

    public static Analyser instance;
    private Tree data;

    public static Analyser getInstance() {
        if (instance != null){
            instance = new Analyser();
        }
        return instance;
    }


    // methode qui cree un node

    public void addNode(int LOC, int CLOC){
        
    }
}
