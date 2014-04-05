import java.text.DecimalFormat;
import java.util.Random;
import java.util.Scanner;

public class GuessingGame 
{
	    private final Random rGen = new Random();
	    private final Scanner in = new Scanner(System.in);
	    private static final int MAX_NUMBER = 100;
	    private static final int MIN_NUMBER = 1;
	    private static final int MAX_TRIES = 50;
	    int guessNumber;
	    private int totalGuesses,guessesTried,gameCounter,bestGuess;

	    public GuessingGame() 
	    {
	        this.guessNumber = rGen.nextInt(100) + 1;
	        this.bestGuess = 0;
	        this.guessesTried = 0;
	        this.totalGuesses = 0;
	        this.gameCounter = 0;
	    }

	    public int readInt(String prompt) 
	    {
			System.out.println(prompt); 
	        return in.nextInt();
	    }
	    
	    public String readLine(String prompt) 
	    {
	        System.out.println(prompt);
	        return in.nextLine();
	    }

	    public void playGame() 
	    {
	        int guessNumber;
	        guessesTried = 0;
	        System.out.println("I'm thinking of a number between " + MIN_NUMBER + " and " + MAX_NUMBER  + ".");
	        
	        for (int i = MAX_TRIES; i > 0; i--)
	        {
	            guessNumber = readInt("Your guess? ");
	            System.out.println(this.guessNumber);
	            guessesTried++;

	            if (this.guessNumber == guessNumber) 
	            {
	                System.out.println("You guessed " + guessNumber + ", the correct number, in " + guessesTried + " guesses!");
	                break;
	            } 
	            else if (this.guessNumber > guessNumber) 
	            {
	                System.out.println("It's higher.");
	                guessQuestion();
	            } 
	            else 
	            {
	                System.out.println("It's lower.");
	                guessQuestion();
	            }
	        }
	        totalGuesses += guessesTried;
	        gameCounter++;
	    }

	    public boolean playAgain() 
	    {
	        in.nextLine();
	        String answer = readLine("Play Again? (Y/N): ").toLowerCase();
	        return answer.equals("y");
	    }

	    public void guessQuestion() 
	    {
	        int guessesLeft = MAX_TRIES - guessesTried;
	        if (guessesLeft < 1) 
	        {
	            System.out.println("You lost this round.");
	        } 
	        else 
	        {
	            System.out.println("You have " + guessesLeft + " guesses left!");
	        }
	    }

	    public void yourResults() 
	    {
	        DecimalFormat df = new DecimalFormat("0.00");
	        System.out.println("Your results: ");
	        System.out.println("Total Games: " + gameCounter);
	        System.out.println("Guesses/Game: " + df.format((avgPerGameCalc(totalGuesses, gameCounter))));
	        System.out.println("Best Score: " + bestGame());
	    }

	    public double avgPerGameCalc(int guessesTaken, int gamesPlayed) 
	    {
	        double aveGussesePerGame = (double)guessesTaken/(double)gamesPlayed;
	        return aveGussesePerGame;
	    }

	    public int bestGame() 
	    {
	        bestGuess = guessesTried;

	        if (guessesTried < bestGuess) 
	        {
	            bestGuess = guessesTried;
	        }
	        
	        return bestGuess;
	    }

	    public static void main(String[] args) 
	    {
	    	GuessingGame program = new GuessingGame();
	        do 
	        {
	            program.playGame();
	        } 
	        while (program.playAgain());
	        program.yourResults();
	    }
}