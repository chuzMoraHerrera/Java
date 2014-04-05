public class Doodlebug extends Organism
{
	private int timeSinceDinner = 0;
	
	public Doodlebug(World world, int x, int y)
	{
		super(world, x, y);
		super.maxBreedCounter = 8;
	}
	
	public String toString()
	{
		return "doodlebug";
	}
	
	@Override
	protected Organism offspringBug(World world, int x, int y)
	{
		return new Doodlebug(world, x, y);
	}	
	
	@Override
	public void simulate()
	{
		super.simulate();
		if(die()) return;
		eat();
	}
	
	protected void eat()
	{
		int[][] pointsAroundMe = new int[][]{{x-1,y},{x+1,y},{x,y-1},{x,y+1}};
		for(int[] point:pointsAroundMe)
		{
			if(world.getAt(point[0], point[1]) != null && world.getAt(point[0], point[1]).toString().equals("ant") && world.pointInGrid(point[0], point[1]))
			{
				world.setAt(point[0], point[1], this);
				world.setAt(x, y, null);
				this.x = point[0];
				this.y = point[1];
				timeSinceDinner = 0;
				return;
			}
		}
		timeSinceDinner++;
	}
	
	protected boolean die()
	{
		if(timeSinceDinner != 3) return false;
		world.setAt(x, y, null);
		
		return true;
	}
}