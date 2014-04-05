
public class Ant extends Organism
{
	public Ant(World world, int x, int y)
	{
		super(world, x, y);
	}
	
	//returns a string representation of the ant
	public String toString()
	{
		return "ant";
	}

	@Override
	protected Organism offspringBug(World world, int x, int y) 
	{
		return new Ant(world, x, y);
	}
}