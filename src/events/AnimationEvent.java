public class AnimationEvent
{
	private Sprite m_sprite;
	private String m_name;
	private boolean m_looped;
	
	public AnimationEvent(Sprite sprite, String name, boolean looped)
	{
		m_sprite = sprite;
		m_name = name;
		m_looped = looped;
	}
	
	public Sprite sprite()
	{
		return m_sprite;
	}
	
	public String name()
	{
		return m_name;
	}
	
	public boolean looped()
	{
		return m_looped;
	}
}
