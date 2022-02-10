public class FactoryParser {
    String ext;
    public FactoryParser(String ext){
        this.ext = ext;
    }

    public Parser create(){
        switch (ext){
            case ".java":
                return new ParserJava();

        }
        return null;
    }
}
