package de.JeterLP.MakeYourOwnCommands;

/**
 * @author TheJeterLP
 */
public class WrongTypeException extends Exception {

    public WrongTypeException(String msg) {
        super(msg);
    }

    public WrongTypeException(Exception e) {
        super(e);
    }

    public WrongTypeException() {
        super();
    }
}
