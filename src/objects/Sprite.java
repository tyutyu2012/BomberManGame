import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Sprite extends GameObject implements Renderable, UpdateListener
{
	public static final int SCALE = 2;
	
	private List<AnimationListener> m_listeners = new CopyOnWriteArrayList<>();
	
	private SpriteSheet m_spriteSheet;
	private String m_currentAnim;
	private int m_currentFrame = 0;
	private int m_currentDelay = 0;
	private int m_zIndex = 0;
	
	public Sprite(int x, int y, int zIndex, String spriteSheet)
	{
		super(x, y);
		m_spriteSheet = SpriteSheet.SheetNamed(spriteSheet);
		m_currentAnim = m_spriteSheet.getDefaultAnim();
		m_zIndex = zIndex;
	}
	
	public void addAnimationListener(AnimationListener listener)
	{
		m_listeners.add(listener);
	}
	
	public void removeAnimationListener(AnimationListener listener)
	{
		m_listeners.remove(listener);
	}
	
	public boolean isOver()
	{
		return m_spriteSheet.getAnims().get(m_currentAnim).get(m_currentFrame).getDelay() == 0;
	}
	
	public String currentAnim()
	{
		return m_currentAnim;
	}
	
	public void currentAnim(String currentAnim)
	{
		if(m_currentAnim != currentAnim || isOver())
		{
			m_currentAnim = currentAnim;
			m_currentFrame = m_currentDelay = 0;
			
			for(AnimationListener listener : m_listeners)
				listener.animationStarted(new AnimationEvent(this, m_currentAnim, false));
		}
	}
	
	public Frame currentFrame()
	{
		return m_spriteSheet.getAnims().get(m_currentAnim).get(m_currentFrame).getFrame();
	}
	
	public BufferedImage image()
	{
		return currentFrame().getImage();
	}
	
	public int dx()
	{
		return 0;
	}
	
	public int dy()
	{
		return 0;
	}
	
	@Override
	public void render(Graphics g)
	{
		Frame frame = currentFrame();
		BufferedImage image = frame.getImage();
		g.drawImage(image,
			getX() + dx() + frame.dx(), getY() + dy() + frame.dy(),
			SCALE * image.getWidth(), SCALE * image.getHeight(), null
		);
	}
	
	@Override
	public int zIndex()
	{
		return m_zIndex;
	}
	
	@Override
	public void update(UpdateEvent e)
	{
		if(isOver())
			return;
		
		if(++m_currentDelay >= m_spriteSheet.getAnims().get(m_currentAnim).get(m_currentFrame).getDelay())
		{
			m_currentDelay = 0;
			if(++m_currentFrame >= m_spriteSheet.getAnims().get(m_currentAnim).size())
			{
				m_currentFrame = 0;
				for(AnimationListener listener : m_listeners)
					listener.animationStarted(new AnimationEvent(this, m_currentAnim, true));
			}
		}
		
		if(isOver())
		{
			for(AnimationListener listener : m_listeners)
				listener.animationEnded(new AnimationEvent(this, m_currentAnim, false));
		}
	}
}
