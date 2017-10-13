// todo: make this implement ExplosionListener

public class Bomb extends GameObject implements ExplosionListener
{
	private int m_ticks;
	private int m_range;
	private static int m_bombNumber;
	
	public Bomb(int x, int y, int ticks, int range, int bombNumber)
	{
		super(x, y);
		m_ticks = ticks;
		m_range = range;
		m_bombNumber = bombNumber;
	}
	
	public int ticks()
	{
		return m_ticks;
	}
	
	public int range()
	{
		return m_range;
	}
	
	public int bombNumber()
	{
		return m_bombNumber;
	}
	
	public void tick()
	{
		--m_ticks;
	}
	public void setTick(int tick)
	{
		m_ticks = tick;
	}
	
	public boolean exploded()
	{
		return m_ticks == 0;
	}
	
	public void reduceBombNumber()
	{
		m_bombNumber --;
	}
	
	//ExplosionListener
	@Override
	
	public void OnExplosion(ExplosionEvent e)
	{
		int row = this.getY() / Board.TILE_SIZE;
		int col = this.getX() / Board.TILE_SIZE;
		if(e.getCol() == col && e.getRow() == row)
		{
			setTick(2);
		}
	}
}
