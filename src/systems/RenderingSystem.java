import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;

public class RenderingSystem extends GameSystem implements GameObjectListener, UpdateListener
{
	private List<Renderable> m_renderables = new CopyOnWriteArrayList<>();
	
	private JFrame m_frame;
	private JPanel m_panel = new JPanel()
	{
		{
			setPreferredSize(new Dimension(480, 416));
		}
		
		@Override
		public void paintComponent(Graphics g)
		{
			for(Renderable r : m_renderables)
				r.render(g);
		}
	};
	
	public RenderingSystem(JFrame frame)
	{
		m_frame = frame;
		m_frame.setTitle("Bomberman");
		m_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		m_frame.setFocusable(true);
		m_frame.add(m_panel);
		m_frame.setVisible(true);
		m_frame.pack();
	}
	
	@Override
	public void gameObjectAdded(GameObjectEvent e)
	{
		if(e.object() instanceof Renderable)
		{
			for(int i = 0; i < m_renderables.size(); ++i)
			{
				if(((Renderable) e.object()).zIndex() < m_renderables.get(i).zIndex())
				{
					m_renderables.add(i, (Renderable) e.object());
					return;
				}
			}
			m_renderables.add((Renderable) e.object());
		}
	}
	
	@Override
	public void gameObjectRemoved(GameObjectEvent e)
	{
		m_renderables.remove(e.object());
	}
	
	@Override
	public void update(UpdateEvent e)
	{
		m_panel.repaint();
	}
}
