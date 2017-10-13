import java.io.IOException;

public class Server
{
	public static void main(String[] args) throws IOException
	{
		Game game = new Game(true);
		game.run();
	}
}
