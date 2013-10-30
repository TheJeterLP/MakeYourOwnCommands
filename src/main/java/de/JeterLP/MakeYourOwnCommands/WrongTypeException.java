package de.JeterLP.MakeYourOwnCommands;

/**
 * @author TheJeterLP
 */
public class WrongTypeException extends Exception {

    public WrongTypeException(String msg) {
        super(msg);
    }
    
    public WrongTypeException() {
        super();
    }
}
