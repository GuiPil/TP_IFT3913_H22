import java.io.File;
import java.io.FileNotFoundException;

public class FileRunner {
    /**
     * traverse all the file calling the parser when it stuble a java file, it
     * call the parser and it have access to the analyser for stats purpose
     *
     */

    Analyser analyser;
    Parser parser = new Parser(analyser);
    File initial;

    public FileRunner(Analyser dataBase, String ini_path){
        initial = new File(ini_path);
    }

   public void run(File path) throws FileNotFoundException {
        File target = path;
       //create node and add to tree.
       System.out.println(target.getPath());
        if(target.isDirectory()){
//            System.out.println(target+" is a package");
            String[] subs = target.list();

            for(int i=0; i<subs.length; i++){
                run(new File(target, subs[i]));
            }
        }

        if(target.isFile()){
            if(target.getName().endsWith(".java")){ // a remplacer avec .java et autres GP

                parser.parse(target);
            }
        }
   }
}
