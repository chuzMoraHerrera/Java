 import java.awt.event.MouseEvent;

import acm.graphics.GImage;
import acm.program.GraphicsProgram;
import acm.util.RandomGenerator;

//the world the bugs live in
public class World extends GraphicsProgram
{
	public static final int GRID_SIZE_ROW = 10;
	public static final int GRID_SIZE_COL = 15;
	
	//a two d array that holds the set of organisms in the world
	private Organism[][] bugs = new Organism[GRID_SIZE_ROW][GRID_SIZE_COL];
	
	public void run()
	{
		setSize(750, 500);
		RandomGenerator rgen = RandomGenerator.getInstance();
	
		//randomly generates ants and doodlebugs and places them in the world
		for(int row = 0; row< GRID_SIZE_ROW; row++)
		{	
			for(int col = 0; col < GRID_SIZE_COL; col++)
			{
				int randNum = rgen.nextInt(0,100);
				
				//approx 50% the spots will be heald by ants
				if(randNum > 50)
				{
					bugs[row][col] = new Ant(this, row, col);
				}
				//approx 5% of the spots will be heald by doodlebugs
				else if(randNum < 5)
				{
					bugs[row][col] = new Doodlebug(this, row, col);
				}
			}
		}
		
		//displays the world of doodlebugs and ants with GImages
		display();
		addMouseListeners();
	}
	
	//each time the mouse is clicked the world is resimulated
	public void mouseClicked(MouseEvent e)
	{
		simulateOneStep();
	}
	
	public void simulateOneStep()
	{
		resetSimulation();		//this avoids a bug being simulated more than once
		simulate("doodlebug");	//simulates all the doodlebugs first
		simulate("ant");		//then simulates the ants
		
		display();				//then displays the resulting world to the screen
	}
	
	//sets all the bugs to say they haven't been simulated
	public void resetSimulation()
	{
		for(int row = 0; row< GRID_SIZE_ROW; row++)
		{	
			for(int col = 0; col < GRID_SIZE_COL; col++)
			{
				if(bugs[row][col] != null)
				{
					bugs[row][col].resetSimulation();
				}
			}
		}
	}
	
	//simulates the specified type of bug
	public void simulate(String type)
	{
		for(int row = 0; row < GRID_SIZE_ROW; row++)
		{	
			for(int col = 0; col < GRID_SIZE_COL; col++)
			{
				if(bugs[row][col] != null && bugs[row][col].toString().equals(type))
				{
					bugs[row][col].simulate();
				}
			}
		}
	}
	
	//displays all the bugs to the screen
	public void display()
	{
		removeAll();	//clears the screen
		
		double y = 0;
		
		for(int row = 0; row < GRID_SIZE_ROW; row++)
		{
			double x = 0;
			
			for(int col = 0; col < GRID_SIZE_COL; col++)
			{
				Organism bug = bugs[row][col];
				
				if(bug != null)
				{
					//draws the specified bug to the screen
					GImage bImage = new GImage("images/" + bug.toString() + ".png", x, y);
					add(bImage);
				}
				x += 50;
			}
			y += 50;
		}
	}
	
	//returns the bug at the given array coordinates in the world
	public Organism getAt(int x, int y)
	{
		if(!pointInGrid(x, y))return null;
		
		return bugs[x][y];
	}
	
	//sets the bug at the given array coordinates in the world
	public void setAt(int x, int y, Organism bug)
	{
		bugs[x][y] = bug;
	}
	
	//returns true if the given coordinates are in the grid and false otherwise
	public boolean pointInGrid(int x, int y)
	{
		if(x >= GRID_SIZE_ROW || x < 0 || y >= GRID_SIZE_COL || y < 0) return false;
		
		return true;
	}
}