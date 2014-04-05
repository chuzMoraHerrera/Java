import acm.program.GraphicsProgram;
import acm.util.RandomGenerator;
import acm.graphics.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;


@SuppressWarnings("serial")
public class Breakout extends GraphicsProgram 
{
	//Constants Specifications 
	
	public static final int WIDTH = 400;
	public static final int HEIGHT = 600;
	
	private static final int PADDLE_Y_OFFSET = 30;	
	
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;
	
	private static final int BRICK_Y_OFFSET = 70;    
	
	private static final int NUM_BRICKS_PER_COL = 10;	
	
	private static final int NUM_BRICKS_PER_ROW = 10;
	
	private static final int BRICK_SEP = 4;	
	
	private static final int BRICK_HEIGHT = 8;
	
	private static final int BRICK_LENGTH = (WIDTH / NUM_BRICKS_PER_COL) - (BRICK_SEP);
	
	private static final int BALL_RADIUS = 10;
	
	private static final int BALL_START_X = WIDTH/2 - BALL_RADIUS;
	
	private static final int BALL_START_Y = HEIGHT/2 - BALL_RADIUS; 
	
	private static final int NUM_TURNS = 3;	
	
	private static final int DELAY = 10;
	 
	private int totalBricks = NUM_BRICKS_PER_COL * NUM_BRICKS_PER_ROW;
	private int current_Try = NUM_TURNS;
	
	private double vx, vy=3.0;
	
	private RandomGenerator rGen = RandomGenerator.getInstance();
	
	private boolean hitBottom = false;
	private boolean won = false;
	
	private GRect brick;
	private GRect paddle;
	private GOval ball;
	private GLabel remainingAttempts;

    public void run() 
    {
        makeGame();
        playGame();
    }

    //method to set up Game
    private void makeGame() 
    {
    	setSize(WIDTH, HEIGHT);
    	for (int row = 0; row < NUM_BRICKS_PER_ROW; row++) 
    	{
            for (int col = 0; col < NUM_BRICKS_PER_COL; col++) 
            {
            	Color[] brickColor = {Color.red, Color.orange, Color.yellow,Color.green, Color.cyan};
            	makeBricks((BRICK_LENGTH + BRICK_SEP) * row,BRICK_Y_OFFSET + ((BRICK_HEIGHT + BRICK_SEP) * col),BRICK_LENGTH,BRICK_HEIGHT,brickColor[col / 2]);
            }
        }
        makePaddle();
        addKeyListeners();
        makeBall();
        tryCounter();
        waitForClick();
    }

    /* Method to make grid of bricks: 10 columns X 10 rows with color assignment*/
    private void makeBricks(int x, int y, int lenght, int width, Color brickColor) 
    {
    	brick = new GRect(x, y, lenght, width);
    	brick.setFilled(true);
    	brick.setFillColor(brickColor);
    	add(brick);
    }  
    
    /* Method to make paddle */
    private void makePaddle() 
    {   
        paddle = new GRect(WIDTH/2 - PADDLE_WIDTH/2,HEIGHT - PADDLE_Y_OFFSET - PADDLE_HEIGHT,PADDLE_WIDTH,PADDLE_HEIGHT);
        paddle.setFilled(true);
        add(paddle);  
    }
    
    /*Method to use directional keys (<- & ->)*/
    public void keyPressed(KeyEvent e)
    {
 
  	  double x=paddle.getX();
  	  double y=paddle.getY();
  	  
  	  switch (e.getKeyCode())
  	  {
  	  case KeyEvent.VK_RIGHT:
  		  if(x <(WIDTH-PADDLE_WIDTH) || y <(WIDTH-PADDLE_WIDTH))
	  	   {
	  		   paddle.move(PADDLE_WIDTH, 0);
	  	   }
  	  break;
  	  
  	  case KeyEvent.VK_LEFT:
  		  if (x > 0)
  		  	{
  			  paddle.move(-PADDLE_WIDTH, 0);
  		  	}
  	  break;
  	  
  	  default:
  	  break;
  	  }
    }   
    
    //method to make ball
    private void makeBall() 
    {
        vx = rGen.nextDouble(1.0, 3.0);

        if (rGen.nextBoolean(0.5)) 
        {
            vx = -vx;
        }
        ball = new GOval(BALL_START_X,BALL_START_Y,BALL_RADIUS * 2,BALL_RADIUS * 2);
        ball.setFilled(true);
        add(ball);
    }
    
    //Counter for number of attempts available to play game
    private void tryCounter() 
    {
    	remainingAttempts = new GLabel("Number of attempts: " + current_Try);
    	remainingAttempts.setColor(Color.RED);
        Font myAttemptFont = new Font("Serif", Font.BOLD,24);
        remainingAttempts.setFont(myAttemptFont);
        add(remainingAttempts,((WIDTH - remainingAttempts.getWidth())/2),BRICK_Y_OFFSET/2);
    }

    //method to play game
    private void playGame() 
    {
        while (gameStart()) 
        {
            moveBall();
            testBallContact();
            pause(DELAY);
        }
        gameEnd();
    }

    private void moveBall() 
    {
        ball.move(vx, vy);
    }

    //method to test ball contact
    private void testBallContact() 
    {
    	testWallContact();
        GObject collider = grabReboundingObject();

        if (collider == paddle) 
        {
            paddleRebound();
        } 
        else if (collider != null) 
        {
            totalBricks--;
            remove(collider);
            vy = -vy;
        }
    }

    //Method to check if ball contacts walls
    private void testWallContact() 
    {
        double x = ball.getX();
        double y = ball.getY();
        
        
        double difference; 
        
        //Top edge
        if (y < 0) 
        {
            vy = -vy;
            ball.move(0, -2 * y);
        }

        //Bottom edge
        difference = HEIGHT - (y + BALL_RADIUS * 2); 
        if (difference <= 0) 
        {
            hitBottom = true;
        }

        //Left edge
        if (x < 0)
        {
            vx = -vx;
            ball.move(-2 * x, 0);
        }

        //Right edge
        difference = WIDTH - (x + BALL_RADIUS * 2); 
        if (difference <= 0) 
        {
            vx = -vx;
            ball.move(2 * difference, 0);
        }
    }

    //method to get objects
    private GObject grabReboundingObject() 
    {
        double x = ball.getX();
        double y = ball.getY();
        GObject obj = null;

        if ((obj = getElementAt(x, y)) != null) 
        {
            return obj;
        } 
        else if ((obj = getElementAt(x + BALL_RADIUS * 2, y)) != null) 
        {
            return obj;
        } 
        else if ((obj = getElementAt(x, y + BALL_RADIUS * 2)) != null) 
        {
            return obj;
        } 
        else 
        {
            return getElementAt(x + BALL_RADIUS * 2, y + BALL_RADIUS * 2);
        }
    }

    //method to use when ball contacts paddle
    private void paddleRebound() 
    {
        double dy = (ball.getY() + BALL_RADIUS * 2) - paddle.getY();
        ball.move(0, -dy);

        vy = -vy;
    }

    //method to start game
    private boolean gameStart() 
    {
        if (hitBottom) 
        {
            current_Try--;
            reviseTryCounter();
            hitBottom = false;
			    if (current_Try == 0) 
			    {
			        return false;
			    } 
			    else 
			    {
			        remove(ball);
			        makeBall();
			        pause(1000);
			        return true;
			    }
        }
        
        if (totalBricks == 0) 
        {
            won = true;
            return false;
        }
        return true;
    }

    //method to display messages when game ends
    private void gameEnd() 
    {
        removeAll();
        GLabel myLabel;
        if (won) 
        {
        	myLabel = new GLabel("Success! You're a wonderful player! :-)");
        } 
        else 
        {
        	myLabel = new GLabel("Failure.Try again tomorrow! :-(");
        }

        myLabel.setColor(Color.BLUE);
        Font myFontLabel = new Font("Arial", Font.BOLD,20);
        myLabel.setFont(myFontLabel);
        add(myLabel, ((WIDTH - myLabel.getWidth())/2), HEIGHT/2);
    }

    //method to update try counter
    private void reviseTryCounter() 
    {
    	remainingAttempts.setLabel("Attempts left: " + current_Try);
    }
}