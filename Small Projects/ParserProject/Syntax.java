
/**
 * File: Syntax.java
 * This contains the lexeme and character codes used by the parser as well as the error subprogram
 * Syntax is inherited by the parser and lexical analyzer so they can use these important codes and the error class
 * Created by JacobHertl
 */
public abstract class Syntax {
    //character codes
    static final int unknown = 0;//represents characters that need to be looked up or are unknown
    static final int alpha = 1;//represents characters of the alphabet
    static final int digit = 2;//represents digits of a number
    //Lexeme codes
    static final int num = 3;//represents numbers
    static final int id = 4;//represents identifiers
    static final int add = 5;//represents addition operators
    static final int sub = 6;//represents subtraction operators
    static final int mult = 7;//represents multiplication operators
    static final int div = 8;//represents division operators
    static final int lParen = 9;//represents left parentheses
    static final int rParen = 10;//represents right parentheses
    static final int EOF = -1;//represents the end of the file

    /**
     * Class: Error
     * Error handles errors and identifies what may have caused it
     */
    class Error extends Exception
    {
        Error(){}

        Error (String message)
        {
            super (message);
        }


    }
}
