
public class Analyser {

    public static Analyser instance;


    public static Analyser getInstance() {
        if (instance != null){
            instance = new Analyser();
        }
        return instance;
    }
}
