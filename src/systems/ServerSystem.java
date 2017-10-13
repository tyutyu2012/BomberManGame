import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ServerSystem extends GameSystem implements ServerListener,GameObjectListener
{
	private Game m_game;
	private Board m_board = null;
	private ServerSocket server = null;
	private List<Socket> m_clients = new LinkedList<>();
	private List<PrintWriter> m_writers = new LinkedList<>();
	private List<Scanner> m_scanners = new LinkedList<>();
	private List<Character> m_players = new ArrayList<>();
	private List<PowerUp> m_powers = new LinkedList<>(); 
	private Random rand = new Random();
	
	ServerSystem(Game game) throws IOException
	{
		m_game = game;
		setPlayers();
		setServer();
	}
	
	public void setServer() throws IOException
	{
		server = new ServerSocket(2000);
		// thread for waiting for client
		Thread thread1 = new Thread(new Runnable()
		{
			public void run()
			{
				try
				{
					for (int i = 0; i < 4; i++)
					{
						m_clients.add(server.accept());
						// get input from client
						m_scanners.add(new Scanner(m_clients.get(i).getInputStream()));
						// sent input to client
						m_writers.add(new PrintWriter(m_clients.get(i).getOutputStream()));
						
						// sent player id to the client
						m_writers.get(i).println(i);
						m_writers.get(i).flush();
												
						// thread for starting a new client
						Thread thread2 = new Thread(new Runnable()
						{
						    int currentClient = m_clients.size() -1;
						    @Override
						    public void run()
						    {
						    	// server gets the input from clients, and transfer into movements
						    	while(m_scanners.get(currentClient).hasNextLine())
						    	{
						    		// server gets serverEvent and sent the clientEvent to the clients
						    		String message = m_scanners.get(currentClient).nextLine();
						    		ServerEvent e = new ServerEvent(message);
						    		
						    		// sent the info, player1id, x, y, alive, player2id, x, y, alive
						    		// message type 1;
						    		message ="1 " + message 
						    				+ " "+ m_players.get(e.getPlayerId()).getX()
						    				+ " "+ m_players.get(e.getPlayerId()).getY()
						    				+ " "+m_players.get(e.getPlayerId()).getRange()
						    				+ " "+m_players.get(e.getPlayerId()).getBombNumber()
						    				+ " "+m_players.get(e.getPlayerId()).getSpeed(); 
						    		
									for(PrintWriter w : m_writers)
									{
										w.println(message);
										w.flush();
									}
									
						    		ServerListenerMove(e);
						    		
						    		// type 3 message
						    		for(PrintWriter w : m_writers)
						    		{
						    			for(int i = 0; i < m_players.size(); i++)
						    				w.println("3 " + i + " " + m_players.get(i).getLife()  );
						    		}
						    	}
						    }
						});
						thread2.start();
					}
				}
				catch(IOException e)
				{
					System.out.println("Server IOException");
				}
			}
			
		});
		thread1.start();
	}

	public void setPlayers()
	{
		m_players.add(new Character(m_game, 32 , 32 ,"player1"));
		m_players.add(new Character(m_game, 416 , 32 ,"player2"));
		m_players.add(new Character(m_game, 32 , 352 ,"player3"));
		m_players.add(new Character(m_game, 416 , 352 ,"player4"));
		m_players.get(0).ServerOrClient(true);
		for(Character player: m_players)
			m_game.add(player);
	}
	
	@Override
	public void ServerListenerMove(ServerEvent e)
	{
		int playerId = e.getPlayerId();
		String movement = e.getMovement();
		
		switch (movement)
		{
        	case "leftT": 
    			m_players.get(playerId).setLeft(true);
        		break;
        	case "leftF":
        		m_players.get(playerId).setLeft(false);
        		break;
        	case "rightT": 
    			m_players.get(playerId).setRight(true);
        		break;
        	case "rightF":
        		m_players.get(playerId).setRight(false);
        		break;
        	case "upT": 
    			m_players.get(playerId).setUp(true);
        		break;
        	case "upF":
        		m_players.get(playerId).setUp(false);
        		break;
        	case "downT": 
    			m_players.get(playerId).setDown(true);
        		break;
        	case "downF":
        		m_players.get(playerId).setDown(false);
        		break;
        	case "bombT": 
    			m_players.get(playerId).setBomb(true);
        		break;
        	case "bombF":
        		m_players.get(playerId).setBomb(false);
        		break;
		}
	}


	@Override
	public void gameObjectAdded(GameObjectEvent e)
	{
		if(e.object() instanceof PowerUp)
		{
			PowerUp p = (PowerUp) e.object();
			int type =p.type();
			int row = p.getRow();
			int col = p.getCol();
			if(type == 0)
				m_board.currentAnim(row, col, "powerUpFire");
			else if(type == 1)
				m_board.currentAnim(row, col, "powerUpBomb");
			else if(type == 2)
				m_board.currentAnim(row, col, "powerUpSpeed");
			
			// sent the type information to client
			if(type <3)
			{
				for(PrintWriter w : m_writers)
				{
					// type 2 message
					w.println("2 " + type +" "+ row +" " + col);
					w.flush();
				}
			}
		}
		if(e.object() instanceof Board)
		{
			m_board = (Board)e.object();
		}
	}
	
	@Override
	public void gameObjectRemoved(GameObjectEvent e)
	{
		if(e.object().equals(m_board))
			m_board = null;
	}

}
