public class PowerUpEvent
{
	PowerUpEvent(PowerUp power, int row, int col, int type)
	{
		m_powerup = power;
		m_row = row;
		m_col = col;
		m_type = type;
	}
	public PowerUp getPower()
	{
		return m_powerup;
	}
	
	public int getRow()
	{
		return m_row;
	}
	
	public int getCol()
	{
		return m_col;
	}
	
	public int type()
	{
		return m_type;
	}
	
	public boolean explodeByBomb()
	{
		return m_powerup.explodedByBomb();
	}
	
	public void setType(int type)
	{
		m_type = type;
	}

	private PowerUp m_powerup;
	private int m_row, m_col, m_type;
}
