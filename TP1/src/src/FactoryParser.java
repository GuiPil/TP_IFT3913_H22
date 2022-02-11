/**
 * Factory Parser
 */
public class FactoryParser {

    /**
     * Create a parser instance based on the file extension to parse.
     *
     * @return
     */
    public Parser create(String ext) {
        switch (ext) {
            case ".java":
                return new ParserJava();

            case ".py":
                return new ParserPython();
        }
        return null;
    }
}
