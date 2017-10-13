import java.io.ByteArrayInputStream;
import java.util.Scanner;

public class ClientEvent
{
	ClientEvent(int playerId, String movement, int x , int y)
	{
		m_playerId = playerId;
		m_movement = movement;
	}
	
	ClientEvent(String serialized)
	{
		Scanner scanner = new Scanner(new ByteArrayInputStream(serialized.getBytes()));
		m_messageType =scanner.nextInt();
		if(m_messageType == 1)
		{
			m_playerId = scanner.nextInt();
			m_movement = scanner.next();
			m_x = scanner.nextInt();
			m_y = scanner.nextInt();
			m_range = scanner.nextInt();
			m_bombNumber = scanner.nextInt();
			m_speed = scanner.nextInt();
		}
		else if(m_messageType == 2)
		{
			m_type = scanner.nextInt();
			m_row = scanner.nextInt();
			m_col = scanner.nextInt();
		}
		else if(m_messageType == 3)
		{
			m_playerId = scanner.nextInt();
			m_life = scanner.nextBoolean();
		}
	}
	public int getPlayerId()
	{
		return m_playerId;
	}

	public String getMovement()
	{
		return m_movement;
	}
	
	public int getX()
	{
		return m_x;
	}
	
	public int getY()
	{
		return m_y;
	}
	
	public String toString()
	{
		return m_playerId + " " + m_movement +" " +m_x + " " +m_y;
	}
	
	public int getType()
	{
		return m_type;
	}
	
	public int getRow()
	{
		return m_row;
	}
	
	public int getCol()
	{
		return m_col;
	}
	
	public int getMessageType()
	{
		return m_messageType;
	}
	
	public int getRange()
	{
		return m_range;
	}
	
	public int getBombNumber()
	{
		return m_bombNumber;
	}
	
	public int getSpeed()
	{
		return m_speed;
	}
	
	public boolean getLife()
	{
		return m_life;
	}
	
	private int m_messageType;
	private int m_playerId, m_x, m_y, m_range, m_bombNumber, m_speed;
	private int m_type, m_row, m_col;
	private boolean m_life;
	private String m_movement;
}
