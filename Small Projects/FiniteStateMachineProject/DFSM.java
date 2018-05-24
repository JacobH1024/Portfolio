/**
 Jacob Hertl
 CS 410
 10/30/17
 This program determines whether a given string is accepted in the language
 which the language consisting of ones and zeros that contains '110000' as a substring
 This program models a how a deterministic finite state machine runs
 This DFSM assume that states take a finite amount of time and only transition to the state state upon the
    previous state's completion.
 The DFSM object is the DFSM and the main method serves to take input from the user for a DFSM object to process
 It is assumes that only valid characters are entered (1's and 0's)
 Invalid characters will be ignored as if they are not there
 The addInput method takes a string input and processes it into the input queue
 Additional input can be adding while the machine is still running, but once it is completed it restarts
    to its initial state
 For this project the states provide almost no functionality so they are completed immediately but,
    in a scenario where it takes sometime to run a state it may be beneficial to be able to feed it input as
    it runs
 The program is fairly simple and the rest should be self explanatory
 */
import java.util.LinkedList;
import java.util.Scanner;

public class DFSM {
public enum state
{
    Initial,S1,S11,S110,S1100,S11000,Accepted
}
    private state currentState = state.Initial;
    private boolean accepted = false;
    private boolean running = false;
    private LinkedList<Character> queue = new LinkedList<>();
    private char currentInput;

    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        String input = "";
        while(true)
        {
            System.out.println("Enter a string consisting of 1's and 0's to determine its validity.");
            System.out.println("Invalid characters will be completely ignored. Enter n to quit.");
            input = scanner.nextLine();
            if (input.equals("n") || input.equals("N"))
                break;
            DFSM machine1 = new DFSM();
            machine1.addInput(input);
        }
    }

    public void addInput(String input)
    {
        for (int i = 0; i < input.length(); i++)
        {
            queue.add(input.charAt(i));
        }
        if (!running)
        {
            running = true;
            runState();
            while (!queue.isEmpty()) {
                currentInput = queue.removeFirst();
                stateTransition();
                runState();
            }
            if (accepted)
                System.out.println("The input string is accepted in the language");
            else
                System.out.println("The input string is NOT accepted in the language");
            running = false;
            currentState = state.Initial;
            accepted = false;
        }
    }

    private void runState()
    {
        switch(currentState)
        {
            case Initial:
                break;
            case S1:
                break;
            case S11:
                break;
            case S110:
                break;
            case S1100:
                break;
            case S11000:
                break;
            case Accepted:
                accepted = true;
                break;
            default:
                break;
        }
    }

    private void stateTransition()
    {
        switch(currentState)
        {
            case Initial:
                if (currentInput == '0')
                    currentState = state.Initial;
                else if (currentInput == '1')
                    currentState = state.S1;
                break;
            case S1:
                if (currentInput == '0')
                    currentState = state.Initial;
                else if (currentInput == '1')
                    currentState = state.S11;
                break;
            case S11:
                if (currentInput == '0')
                    currentState = state.S110;
                else if (currentInput == '1')
                    currentState = state.S11;
                break;
            case S110:
                if (currentInput == '0')
                    currentState = state.S1100;
                else if (currentInput == '1')
                    currentState = state.S1;
                break;
            case S1100:
                if (currentInput == '0')
                    currentState = state.S11000;
                else if (currentInput == '1')
                    currentState = state.S1;
                break;
            case S11000:
                if (currentInput == '0')
                    currentState = state.Accepted;
                else if (currentInput == '1')
                    currentState = state.S1;
                break;
            case Accepted:
                if (currentInput == '0')
                    currentState = state.Accepted;
                else if (currentInput == '1')
                    currentState = state.Accepted;
                break;
            default:
                break;
        }
    }
}
