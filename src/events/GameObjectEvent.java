public class GameObjectEvent
{
	private GameObject m_object;
	
	public GameObjectEvent(GameObject object)
	{
		m_object = object;
	}
	
	public GameObject object()
	{
		return m_object;
	}
}
