import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.LinkedList;
import java.util.Scanner;

public class ClientSystem extends GameSystem implements ControlListener, ClientListener, GameObjectListener
{
	private Game m_game;
	private Scanner scanner;
	private PrintWriter writer;
	private Socket client;
	private int clientId = 0;
	private List<Character> m_players = new LinkedList<>();
	private Board m_board = null;
	
	ClientSystem(Game game) throws UnknownHostException, IOException
	{
		m_game = game;
		setPlayers();
		setClient();
	}
	
	//initialize the client and starts the thread. when a message come in, call the clientMover function
	public void setClient() throws UnknownHostException, IOException
	{
		client = new Socket("localHost" , 2000);
		//get input from server
		scanner = new Scanner(client.getInputStream());
		//sent input to server
		writer = new PrintWriter(client.getOutputStream());
		
		clientId = scanner.nextInt();
		scanner.nextLine();
		
		Thread thread = new Thread(new Runnable()
		{
		    @Override
		    public void run()
		    {
		    	while(scanner.hasNextLine())
		    	{
		    		String message = scanner.nextLine();
		    		//System.out.println(message);
		    		// call the clientEvent to set the variable
		    		ClientEvent e = new ClientEvent(message);
		    		ClientListenerMove(e);
		    	}
		    }
		});
		thread.start();
	}
	
	public void setPlayers()
	{
		m_players.add(new Character(m_game, 32 , 32 ,"player1"));
		m_players.add(new Character(m_game, 416 , 32 ,"player2"));
		m_players.add(new Character(m_game, 32 , 352 ,"player3"));
		m_players.add(new Character(m_game, 416 , 352 ,"player4"));
		// tell the players that you are clients, uses a static variable on character class
		m_players.get(0).ServerOrClient(false);
		// add players to the game
		for(Character player: m_players)
			m_game.add(player);
	}

	@Override
	public void ClientListenerMove(ClientEvent e)
	{	
		// receives 3 types of message
		int messageType = e.getMessageType();
		
		// control message, current position,  players statistic
		if(messageType == 1)
		{
			int playerId = e.getPlayerId();
			String movement = e.getMovement();

			m_players.get(playerId).setRange(e.getRange());
			m_players.get(playerId).setBombNumber(e.getBombNumber());
			m_players.get(playerId).setSpeed(e.getSpeed());
			m_players.get(playerId).setX(e.getX());
			m_players.get(playerId).setY(e.getY());
			
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
		// powerup message, powerup type, row and col
		else if (messageType == 2)
		{
			int type = e.getType();
			int row = e.getRow();
			int col = e.getCol();
			m_game.add(new PowerUp(row, col, type));

		}
		
		// player life message, determine if a player is alive
		else if (messageType == 3)
		{
			int playerId = e.getPlayerId();
			boolean Die = e.getLife();
			if(Die)
			{
				m_players.get(playerId).clientDie();
			}
		}
	}

	// sent server message
	public void OnControlPressed(ControlEvent e)
	{
		//System.out.println("call keys");
		if(e.Key().equals("left"))
		{
			writer.println(clientId+ " leftT");
			writer.flush();
		}
		else if(e.Key().equals("right"))
		{
			writer.println(clientId+ " rightT");
			writer.flush();
		}
		else if(e.Key().equals("up"))
		{
			writer.println(clientId+ " upT");
			writer.flush();
		}
		else if(e.Key().equals("down"))
		{
			writer.println(clientId+ " downT");
			writer.flush();
		}
		else if(e.Key().equals("a"))
		{
			writer.println(clientId+ " bombT");
			writer.flush();
		}
	}
	
	//sent server message
	@Override
	public void OnControlReleased(ControlEvent e)
	{
		if(e.Key().equals("left"))
		{
			writer.println(clientId+ " leftF");
			writer.flush();	
		}
		else if(e.Key().equals("right"))
		{
			writer.println(clientId+ " rightF");
			writer.flush();	
		}
		else if(e.Key().equals("up"))
		{
			writer.println(clientId+ " upF");
			writer.flush();	
		}
		else if(e.Key().equals("down"))
		{
			writer.println(clientId+ " downF");
			writer.flush();	
		}
		else if(e.Key().equals("a"))
		{
			writer.println(clientId+ " bombF");
			writer.flush();	
		}
	}

	@Override
	public void gameObjectAdded(GameObjectEvent e)
	{
		if(e.object() instanceof PowerUp)
		{
			PowerUp p = (PowerUp) e.object();
			int type = p.type();
			int row = p.getRow();
			int col = p.getCol();
			if(type == 0)
				m_board.currentAnim(row, col, "powerUpFire");
			else if(type == 1)
				m_board.currentAnim(row, col, "powerUpBomb");
			else if(type == 2)
				m_board.currentAnim(row, col, "powerUpSpeed");
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
