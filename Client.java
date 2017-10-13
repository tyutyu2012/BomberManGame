import java.io.IOException;

public class Client
{
	public static void main(String[] args) throws IOException
	{
		Game game = new Game(false);
		game.run();
	}
}
