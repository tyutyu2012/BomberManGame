import java.util.LinkedList;
import java.util.List;

public class PowerUpSystem extends GameSystem implements UpdateListener, GameObjectListener
{
	private Game m_game;
	private Board m_board = null;
	private List<PowerUpListener> m_listeners = new LinkedList<>();
	private List<PowerUp> m_powers = new LinkedList<>(); 
	
	PowerUpSystem(Game game)
	{
		m_game = game;
	}
	
	public void addListener(PowerUpListener p)
	{
		m_listeners.add(p);
	}

	@Override
	public void gameObjectAdded(GameObjectEvent e)
	{
		if(e.object() instanceof PowerUp)
		{
			m_powers.add((PowerUp) e.object());
		}
		if(e.object() instanceof PowerUpListener)
		{
			m_listeners.add((PowerUpListener) e.object());
		}
		if(e.object() instanceof Board)
		{
			m_board = (Board)e.object();
		}
	}

	@Override
	public void gameObjectRemoved(GameObjectEvent e)
	{
		m_powers.remove(e.object());
		m_listeners.remove(e.object());
		if(e.object().equals(m_board))
			m_board = null;
	}

	@Override
	public void update(UpdateEvent e)
	{
		for(PowerUp power : m_powers)
		{
			power.tickUpdate();
			checkTouch(power, power.getRow(), power.getCol(), power.type());
			if(power.touch() && !power.explodedByBomb())
			{
				m_board.currentAnim(power.getRow(), power.getCol(), "empty");
				m_game.remove(power);
			}
			else if(power.timeOut() && !power.explodedByBomb())
			{
				m_board.currentAnim(power.getRow(), power.getCol(), "explosion");
				m_game.remove(power);
			}
		}		
	}
	
	public void checkTouch(PowerUp power, int row, int col, int type)
	{
		for (PowerUpListener listener : m_listeners)
		{
			// checking each condition
			//System.out.println("calling type");
			PowerUpEvent e = new PowerUpEvent(power, row, col, type);
			listener.powerUpRange(e);
			listener.powerUpBombNumber(e);
			listener.powerUpSpeed(e);
		}
	}

}
