import java.awt.Graphics;

public interface Renderable
{
	void render(Graphics g);
	int zIndex();
}
