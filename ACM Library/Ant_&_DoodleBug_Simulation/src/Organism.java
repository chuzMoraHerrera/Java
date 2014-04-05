import acm.util.RandomGenerator;

public abstract class Organism
{
	protected World world;
	protected int x;
	protected int y;
	protected boolean simulated;
	protected int breedCounter = 0;
	protected int maxBreedCounter = 3;
	RandomGenerator rgen = new RandomGenerator();
	
	public Organism(World world, int x, int y)
	{
		this.world = world;
		this.x = x;
		this.y = y;
		simulated = true;
	}
	
	//returns the string representation of the organism
	public abstract String toString();
	protected abstract Organism offspringBug(World world, int x, int y);
	
	public void simulate()
	{
		//don't simulate twice in a round
		if(simulated) return;
		simulated = true;
		
		move();
		if(timeToBreed())
		{
		breed();
		breedCounter = 0;
		}
	}

	//indicate that the organism hasn't simulated this round
	public void resetSimulation()
	{
		simulated = false;
	}
	
	protected void move()
	{
		int randomDirection = rgen.nextInt(1, 4);
		int oldX = x;
		int oldY = y;
		
		if(randomDirection == 1) x--; // left
		if(randomDirection == 2) x++; // right
		if(randomDirection == 3) y--; // up
		if(randomDirection == 4) y++; // down
		
		if(world.pointInGrid(x, y) && world.getAt(x, y) == null) 
		{
			world.setAt(x, y, this);
			world.setAt(oldX, oldY, null);
		}
		else
		{
			x = oldX;
			y = oldY;
		}
	}
	
	private boolean timeToBreed()
	{
		breedCounter++;
		return (breedCounter == maxBreedCounter);
	}
	
	protected void breed()
	{
		int[][] pointsAroundMe = new int[][]{{x-1,y},{x+1,y},{x,y-1},{x,y+1}};
		for(int[] point:pointsAroundMe)
		{
			if(world.getAt(point[0], point[1]) == null && world.pointInGrid(point[0], point[1]))
			{
				world.setAt(point[0], point[1], offspringBug(world, point[0], point[1]));
				return;
			}
		}
	}
}