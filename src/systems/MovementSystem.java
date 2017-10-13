import java.util.List;
import java.util.LinkedList;

public class MovementSystem extends GameSystem implements GameObjectListener, UpdateListener
{
	private static final int FUZZY_EDGES = 10;
	
	private List<Character> m_characters = new LinkedList<>();
	private Board m_board = null;
	
	@Override
	public void gameObjectAdded(GameObjectEvent e)
	{
		if(e.object() instanceof Character)
			m_characters.add((Character) e.object());
		if(e.object() instanceof Board)
			m_board = (Board) e.object();
	}
	
	@Override
	public void gameObjectRemoved(GameObjectEvent e)
	{
		m_characters.remove(e.object());
		if(e.object() == m_board)
			m_board = null;
	}
	
	@Override
	public void update(UpdateEvent e)
	{
		for(Character c : m_characters)
		{
			int x = c.getX();
			int y = c.getY();
			
			int tLeft	= x / m_board.TILE_SIZE;
			int tRight	= (x + c.width() - 1) / m_board.TILE_SIZE;
			int tTop	= y / m_board.TILE_SIZE;
			int tBottom	= (y + c.height() - 1) / m_board.TILE_SIZE;
			
			int dy = c.velocityY();
			y += dy;
			
			int ny = (y + (dy > 0 ? c.height() : 0)) / m_board.TILE_SIZE;
			if((dy < 0 && ny != tTop) || (dy > 0 && ny != tBottom))
			{
				boolean left = m_board.emptyAt(ny, tLeft);
				boolean right = m_board.emptyAt(ny, tRight);
				
				if(!left && m_board.emptyAt(ny, (x + FUZZY_EDGES) / m_board.TILE_SIZE))
					++x;
				if(!right && m_board.emptyAt(ny, (x + c.width() - FUZZY_EDGES) / m_board.TILE_SIZE))
					--x;
				if(!left || !right)
					y = (y / m_board.TILE_SIZE + 1) * m_board.TILE_SIZE - (dy > 0 ? c.height() : 0);
			}
			
			int dx = c.velocityX();
			x += dx;
			
			int nx = (x + (dx > 0 ? c.width() : 0)) / m_board.TILE_SIZE;
			if((dx < 0 && nx != tLeft) || (dx > 0) && nx != tRight)
			{
				boolean top = m_board.emptyAt(tTop, nx);
				boolean bottom = m_board.emptyAt(tBottom, nx);
				
				if(!top && m_board.emptyAt((y + FUZZY_EDGES) / m_board.TILE_SIZE, nx))
					++y;
				if(!bottom && m_board.emptyAt((y + c.height() - FUZZY_EDGES) / m_board.TILE_SIZE, nx))
					--y;
				if(!top || !bottom)
					x = (x / m_board.TILE_SIZE + 1) * m_board.TILE_SIZE - (dx > 0 ? c.width() : 0);
			}
			
			c.setX(x);
			c.setY(y);
		}
	}
}
