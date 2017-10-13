import java.awt.image.BufferedImage;

public class Frame
{
	private BufferedImage image;
	private int m_dx, m_dy;
	
	public Frame(BufferedImage image, int dx, int dy)
	{
		this.image = image;
		m_dx = dx;
		m_dy = dy;
	}
	
	public BufferedImage getImage()
	{
		return image;
	}
	
	public int dx()
	{
		return m_dx;
	}
	
	public int dy()
	{
		return m_dy;
	}
}
