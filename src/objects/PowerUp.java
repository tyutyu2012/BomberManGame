import java.util.Random;

public class PowerUp extends GameObject implements ExplosionListener
{
	PowerUp(int row, int col, int type)
	{
		m_col = col;
		m_row = row;
		m_type = type;
		m_tick = 250;
	}
	public int getCol()
	{
		return m_col;
	}
	
	public int getRow()
	{
		return m_row;
	}
	
	public void setTouch()
	{
		m_touched = true;
	}
	
	public boolean touch()
	{
		return m_touched;
	}
	
	public int type()
	{
		return m_type;
	}
	
	public int tick()
	{
		return m_tick;
	}
	
	public void tickUpdate()
	{
		m_tick --;
	}
	
	public boolean timeOut()
	{
		return m_tick == 0 && !touch();
	}
	
	public boolean explodedByBomb()
	{
		return m_explode;
	}
	
	public void setExplode()
	{
		m_explode = true;
	}
	
	@Override
	public void OnExplosion(ExplosionEvent e)
	{
		if(e.getCol() == this.getCol() && e.getRow() == this.getRow())
		{
			this.setExplode();
		}
	}
	
	private int m_col, m_row, m_type, m_tick;
	private boolean m_touched, m_explode = false;
	private Random rand = new Random();
}
