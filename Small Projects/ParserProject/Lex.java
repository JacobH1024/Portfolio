import java.io.PrintWriter;
import java.util.Scanner;


/**
 * File: Lex.java
 *Lexical analyzer that
 * Created by JacobHertl
 */
public class Lex extends Syntax{
    //Variables
    private int charClass;//represents type of character
    String lexeme = "";//Holds the current lexeme
    private char nextChar;//hold the next char to be evaluated
    int nextToken;//represents the type of lexeme
    Parser pars;//the parser
    private Scanner reader;//reader that reads from front.in
    private PrintWriter writer;//writer that writes to front.out

    public Lex(Parser parser, Scanner scanner, PrintWriter fWriter)
    {
        pars = parser;
        reader = scanner;
        writer = fWriter;
    }

    /**
     * lookup operators and parentheses and return the token
     * @param ch is the character that needs to be looked up
     * @return nextToken is the type of the next token
     */
    public int lookup(char ch)
    {
        lexeme += nextChar;
        switch (ch)
        {
            case '(':
                nextToken = lParen;
                break;
            case ')':
                nextToken = rParen;
                break;
            case '+':
                nextToken = add;
                break;
            case '-':
                nextToken = sub;
                break;
            case '*':
                nextToken = mult;
                break;
            case '/':
                nextToken = div;
                break;
            default:
                nextToken = EOF;
                break;
        }//end of switch
        return nextToken;
    }//end of fx lookup

    /**
     * gets the next character of input and determine its character class
     */
    public void getChar()
    {
        if(reader.hasNext() )
        {
            nextChar = reader.next().charAt(0);//gets next character
            if(Character.isLetter(nextChar))
            {
                charClass = alpha;
            }
            else if (Character.isDigit(nextChar))
            {
                charClass = digit;
            }
            else
            {
                charClass = unknown;
            }
        }
        else//if reader does not have next it must be end of file
        {
            charClass = EOF;
        }
    }//end of fx getChar

    /**
     * lexical analysis
     * @return nextToken is the type of the next lexeme
     */
    public int lex()
    {
        lexeme = "";
        while (Character.isWhitespace(nextChar))//continues to pass through extra space
        {
            getChar();
        }
        switch (charClass)
        {
            case alpha://continues to get all character of lexeme
                lexeme += nextChar;
                getChar();
                while(charClass == alpha ||charClass == digit)
                {
                    lexeme += nextChar;
                    getChar();
                }
                nextToken = id;
                break;
            case digit://continues to get all digits of a number
                lexeme += nextChar;
                getChar();
                while (charClass == digit)
                {
                    lexeme += nextChar;
                    getChar();
                }
                nextToken = num;
                break;
            case unknown: //parentheses and operators
                lookup(nextChar);
                getChar();
                break;
            case EOF:
                nextToken = EOF;
                lexeme = "EOF";
                break;
        }//End of switch
        System.out.println("Next token is: " + nextToken + " next lexeme is " + lexeme );
        writer.println("Next token is: " + nextToken + " next lexeme is " + lexeme );
        return nextToken;
    }//End of function lex


}
