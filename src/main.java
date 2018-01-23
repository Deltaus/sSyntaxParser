import exception.GRAException;
import parser.SParser;

public class main {

    public static void main(String[] args){
        SParser parser = SParser.getParser();
        try {
            parser.getInput();
            parser.compile();
            parser.analyze();
        } catch (GRAException e) {
            e.printStackTrace();
        }

    }
}