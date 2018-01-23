package exception;


public class PROException extends Exception {

    public PROException(){
        super("Wrong format in input content...");
    }

    public PROException(String name){
        super("There is no production {"+name+"}");
    }
}