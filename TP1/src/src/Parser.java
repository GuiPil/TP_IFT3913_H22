import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Parser {

    Analyser mainAnalyser;

    public void parse(File f) throws FileNotFoundException {
        File file = f;
        Scanner scanner = new Scanner(file);
        int line_count = 0;
        int comment_count = 0;
        boolean comment_check = false;

        while(scanner.hasNext()){
            String nextline = scanner.nextLine();


            for(int i = 0; i < nextline.length(); i++){


                if(i < nextline.length()-2){

                    if((""+nextline.charAt(i)+nextline.charAt(i+1)).equals("//")){
                        comment_count++;
                    }
                    if((""+nextline.charAt(i)+nextline.charAt(i+1)).equals("/*")){
                        System.out.println("Yeah");
                        comment_check =true;
                    }
                    if((""+nextline.charAt(i)+nextline.charAt(i+1)).equals("*/")){
                        comment_check =false;
                    }
                }
                if(i< nextline.length()-3){
                    if(""+nextline.charAt(i)+nextline.charAt(i+1)+nextline.charAt(i+2) == "/**" ){
                        comment_check = true;
                        comment_count++;
                    }
                }

                // Avant de close il faut appeler l'analyser pour lui transmettre les stats GP
            }
            if(comment_check){
                comment_count++;
            }
            line_count++;
        }
        System.out.println(file.getName()+" contain "+line_count+" lines");
        System.out.println(file.getName()+" contain "+comment_count+" comments");
        scanner.close();
    }
}