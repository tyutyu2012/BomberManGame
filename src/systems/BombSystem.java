import java.util.List;
import java.util.Random;
import java.util.LinkedList;

// todo: make BombSystem implement GameObjectListener
// bomb will not overlap each other and maximum bomb number is 3

public class BombSystem extends GameSystem implements UpdateListener, GameObjectListener
{
	private Game m_game;
	private Board m_board = null;
	private List<Bomb> m_bombs = new LinkedList<>();
	// todo: make a list of ExplosionListeners here
	private List<ExplosionListener> m_listeners = new LinkedList<>();
	//additional stuff
	private Random rand = new Random();	
	private boolean isServer;
	public BombSystem(Game game, boolean server)
	{
		m_game = game;
		isServer = server;
	}
		
	@Override
	public void gameObjectAdded(GameObjectEvent e)
	{
		if(e.object() instanceof Bomb)
		{
			int row = e.object().getY() / m_board.TILE_SIZE;
			int col = e.object().getX() / m_board.TILE_SIZE;
			if(!m_board.hasBomb(row, col) && m_bombs.size() < Character.MAXIMUM_BOMB_NUMBER & m_bombs.size() < Character.m_bombMaxCurrent)
			{
				m_bombs.add((Bomb) e.object());
				m_board.currentAnim(row, col, "bomb");
			}
		}
		if(e.object() instanceof ExplosionListener)
		{
			m_listeners.add((ExplosionListener) e.object());
		}
		if(e.object() instanceof Board)
		{
			m_board = (Board)e.object();
		}
	}
	
	@Override
	public void gameObjectRemoved(GameObjectEvent e)
	{
		m_bombs.remove(e.object());
		m_listeners.remove(e.object());
		if(e.object().equals(m_board))
			m_board = null;
	}
	
	@Override
	public void update(UpdateEvent e)
	{
		for(Bomb bomb : m_bombs)
		{
			bomb.tick();
			if(bomb.exploded())
			{
				int row = bomb.getY() / m_board.TILE_SIZE;
				int col = bomb.getX() / m_board.TILE_SIZE;
				
				explode(bomb, row, col, 0, 0);
				for(int i = 1; i <= bomb.range() && explode(bomb, row, col, -i,  0); ++i); // top
				for(int i = 1; i <= bomb.range() && explode(bomb, row, col,  i,  0); ++i);
				for(int i = 1; i <= bomb.range() && explode(bomb, row, col,  0, -i); ++i);
				for(int i = 1; i <= bomb.range() && explode(bomb, row, col,  0,  i); ++i);
				
				bomb.reduceBombNumber();
				m_game.remove(bomb);		
			}
		}
	}
	
	
	public boolean explode(Bomb bomb, int row, int col, int dy, int dx)
	{
		if(!m_board.inBounds(row + dy, col + dx))
			return false;
		
		// todo: notify all ExplosionListeners that an explosion occurred at row+dy, col+dx
		// for every explosion, check all the bombs
		for (ExplosionListener listener : m_listeners)
		{
			listener.OnExplosion(new ExplosionEvent(bomb, row+dy, col+dx));
		}
		
		if(dx == 0 && dy == 0)
		{
			m_board.currentAnim(row + dy, col + dx, "middleExplode");
			return true;
		}
		
		if(m_board.emptyAt(row + dy, col + dx))
		{
			if(dy != 0)
			{
				if(Math.abs(dy) < bomb.range())
					m_board.currentAnim(row + dy, col + dx, "verticalExplode");
				else if(dy > 0)
					m_board.currentAnim(row + dy, col + dx, "bottomEndExplode");
				else
					m_board.currentAnim(row + dy, col + dx, "topEndExplode");
			}
			
			if(dx != 0)
			{
				if(Math.abs(dx) < bomb.range())
					m_board.currentAnim(row + dy, col + dx, "horizontalExplode");
				else if(dx > 0)
					m_board.currentAnim(row + dy, col + dx, "rightEndExplode");
				else
					m_board.currentAnim(row + dy, col + dx, "leftEndExplode");
			}
			
			return true;
		}
		// if explode the block, has chance to get powerups or null
		else if(m_board.currentAnim(row + dy, col + dx) == "block")
		{
			m_board.currentAnim(row + dy, col + dx, "explodingBlock");
			// create a powerup object;
			if(isServer)
			{
				int type = rand.nextInt(4);
				if(type<3)
					m_game.add(new PowerUp(row + dy, col + dx, type));
			}
			
		}
		return false;
	}
}
