public class GameObject
{
	private int m_x, m_y;
	
	public GameObject()
	{
		this(0, 0);
	}
	
	public GameObject(int x, int y)
	{
		m_x = x;
		m_y = y;
	}
	
	public int getX()
	{
		return m_x;
	}
	
	public void setX(int x)
	{
		m_x = x;
	}
	
	public int getY()
	{
		return m_y;
	}
	
	public void setY(int y)
	{
		m_y = y;
	}
}
