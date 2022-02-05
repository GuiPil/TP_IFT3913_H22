import com.sun.source.util.SourcePositions;

import java.io.File;
import java.net.SocketOption;

public class FileRunner {
    /**
     * traverse all the file calling the parser when it stuble a java file, it
     * call the parser and it have access to the analyser for stats purpose
     *
     */

    Analyser analyser;
    Parser parser = new Parser();
    File initial;

    public FileRunner( Analyser a, String ini_path ){
        this.analyser = a;
        this.initial = new File(ini_path);
    }

   public void run(File path){
        File target = path;

        if(target.isDirectory()){
            System.out.println(target+" is a package");

            String[] subs = target.list();

            for(int i=0; i<subs.length; i++){
                run(new File(target, subs[i]));
            }
        }

        if(target.isFile()){
            if(target.getName().endsWith(".txt")){ // a remplacer avec .java et autres GP
                System.out.println(target.getName()+" is a file"); // ici on pourrait parser GP
            }
        }
   }
}
