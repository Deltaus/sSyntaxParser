package exception;

public class GRAException extends Exception {

    public GRAException() {
        super("An ambiguous grammar...");
    }
}