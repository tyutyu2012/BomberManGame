public class ControlEvent
{
	// todo: fill this in
	ControlEvent(String key, boolean pressed, boolean firstTimePressed)
	{
		m_key = key;
		m_pressed = pressed;
		m_firstTimePressed = firstTimePressed;
	}
	
	public String Key()
	{
		return m_key;
	}
	
	public boolean pressed()
	{
		return m_pressed;
	}
	
	public boolean firstTimePressed()
	{
		return m_firstTimePressed;
	}
	
	private String m_key;
	private boolean m_pressed, m_firstTimePressed;
}
