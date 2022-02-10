public class FactoryParser {

    String ext;
    public FactoryParser(String ext){
        this.ext = ext;
    }

    public Parser create(){
        switch (ext){
            case ".java":
                return new ParserJava();

            case ".py":
                return new ParserPython();
}
        return null;
    }
}
