import java.io.ByteArrayInputStream;
import java.util.Scanner;

public class ServerEvent
{
	ServerEvent(int playerId, String movement)
	{
		m_playerId = playerId;
		m_movement = movement;
	}
	
	ServerEvent(String serialized)
	{
		Scanner scanner = new Scanner(new ByteArrayInputStream(serialized.getBytes()));
		m_playerId = scanner.nextInt();
		m_movement = scanner.next();
	}
	public int getPlayerId()
	{
		return m_playerId;
	}

	public String getMovement()
	{
		return m_movement;
	}
	
	public String toString()
	{
		return m_playerId + " " + m_movement;
	}
	
	private int m_playerId;
	private String m_movement;
}
