import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * File:Parser.java
 * Created by JacobHertl
 */
public class Parser extends Syntax{
    private File inF;//the file to be read
    private File outF;//file to be written to
    private Scanner reader;
    private PrintWriter writer;
    Lex lexA;//the lexical analyzer

    /**
     * creates the paser and assigns necessary files
     * @param args there are no arguments needed for this program
     */
    public static void main(String[] args)
    {
        Parser pars = new Parser();
        //OPen the input file and process its contents
        pars.inF = new File("front.in");
        pars.outF = new File("front.out");
        System.out.println("files opened");
        pars.startParsing();
    }

    /**
     * opens both files to be read and written to
     * starts the parsing and closes the files when the parsing is finished
     */
    public void startParsing()
    {
        try
        {
            reader = new Scanner(new BufferedReader(new FileReader(inF)));
            reader.useDelimiter("");
            writer = new PrintWriter(outF, "UTF-8");
        }
        catch (Exception e){
            System.out.println("Error reading file " + e);
        }
        lexA = new Lex(this, reader, writer);
        lexA.getChar();
        while(lexA.nextToken != EOF)
        {
            lexA.lex();
            expr();
        }
        reader.close();
        writer.close();
    }

    /**
     * parses expressions by following the rule:
     * <expr> -> <term> {(+|-)<term>}
     */
    public void expr()
    {
        System.out.println("Enter <expr>");
        writer.println("Enter <expr>");
        term();//parses the first term
        while ((lexA.nextToken == add)||(lexA.nextToken == sub))
        {
            lexA.lex();
            term();
        }
        System.out.println("Exit <expr>");
        writer.println("Exit <expr>");
    }// end of fx expr

    /**
     * parses terms by following the rule:
     * <term> -> <factor> {(*|/)<factor>}
     */
    public void term()
    {
        System.out.println("Enter <term>");
        writer.println("Enter <term>");
        factor();//parses the first factor
        while ((lexA.nextToken == mult)|| (lexA.nextToken == div))
        {
            lexA.lex();
            factor();
        }
        System.out.println("Exit <term>");
        writer.println("Exit <term>");
    }//end of fx term

    /**
     * parses factors by following the rule:
     * <factor> -> id | int_constant | (<expr>)
    */
    public void factor()
    {
        System.out.println("Enter <factor>");
        writer.println("Enter <factor>");
        if((lexA.nextToken == id) || (lexA.nextToken == num))
        {
            lexA.lex();
        }
        //if the RHS is (<expr>), call lex to pass over the left parenthesis, call expr,
        //and check for the right parenthesis
        else
        {
            try
            {
                if(lexA.nextToken == lParen)
                {
                    lexA.lex();
                    expr();
                    if(lexA.nextToken == rParen)
                    {
                        lexA.lex();
                    }
                    else//
                    {
                        throw new Error("Parenthesis expected before " + lexA.lexeme);
                    }
                }
                //it was not an id, an integer literal, or a left parenthesis
                else//it was not a parenthesis
                {
                    throw new Error("Expression surrounded by parentheses expected before " + lexA.lexeme);
                }
            }
            catch (Error e)
            {
                System.out.println(e);
            }
        }//end of else right_paren
        System.out.println("Exit <factor>");
        writer.println("Exit <factor>");
    }//end of fx factor




}
