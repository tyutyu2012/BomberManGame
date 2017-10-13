public class ExplosionEvent
{
	// todo: fill this in
	ExplosionEvent(Bomb bomb, int row, int col)
	{
		m_bomb = bomb;
		m_row = row;
		m_col = col;
	}
	
	public int getRow()
	{
		return m_row;
	}
	
	public int getCol()
	{
		return m_col;
	}
	
	public Bomb getBomb()
	{
		return m_bomb;
	}
	
	private int m_row, m_col;
	private Bomb m_bomb;
}
