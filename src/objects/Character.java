// todo: make this implement ExplosionListener

import java.awt.Color;
import java.awt.Graphics;

public class Character extends Sprite implements UpdateListener, AnimationListener, ExplosionListener, PowerUpListener
{
    private Game m_game = null;
    
    private int m_velocityX = 0, m_velocityY = 0, m_speed = 2; // speed of the character
    private int m_width = 25, m_height = 25;
    
    private boolean m_left	= false;
    private boolean m_right	= false;
    private boolean m_up	= false;
    private boolean m_down	= false;
    private boolean m_bomb	= false;
    private boolean m_dead	= false;
    
    private int m_bombTicks = 100;
    private int m_bombRange = 1;
    
    public static int m_bombMaxCurrent = 1;
    public static final int MAXIMUM_BOMB_NUMBER = 1;
    private final int MAXIMUM_SPEED = 3;
    public final int MAXIMUM_RANGE = 1;
    
    private static boolean isServer;
    
    public Character(Game game, int x, int y)
    {
        super(x, y, 1, "player1");
        
        // save the game so we can add bombs to it
        m_game = game;
    }
    
    public Character(Game game, int x, int y, String spriteSheet)
    {
        super(x, y, 1, spriteSheet);
        m_game = game;
    }
    
    public void die()
    {
        if(!m_dead &&isServer)
        {
            m_dead = true;
            addAnimationListener(this);
            currentAnim("die");
        }
    }
    
    public void clientDie()
    {
        if(!m_dead)
        {
            m_dead = true;
            addAnimationListener(this);
            currentAnim("die");
        }
    }
    public void ServerOrClient(boolean Server)
    {
        this.isServer = Server;
    }
    
    // Setters and getters
    public synchronized void setLeft(boolean left)
    {
        m_left = left;
    }
    
    public synchronized void setRight(boolean right)
    {
        m_right = right;
    }
    
    public synchronized void setUp(boolean up)
    {
        m_up = up;
    }
    
    public synchronized void setBomb(boolean bomb)
    {
        m_bomb = bomb;
    }
    
    public synchronized void setDown(boolean down)
    {
        m_down = down;
    }
    
    public boolean getLeft()
    {
        return m_left;
    }
    
    public boolean getRight()
    {
        return m_right;
    }
    
    public boolean getUp()
    {
        return m_up;
    }
    
    public boolean getDown()
    {
        return m_down;
    }
    
    public boolean getBomb()
    {
        return m_bomb;
    }
    
    public int velocityX()
    {
        return m_velocityX;
    }
    
    public void velocityX(int direction)
    {
        m_velocityX = m_speed * direction;
    }
    
    public int velocityY()
    {
        return m_velocityY;
    }
    
    public void velocityY(int direction)
    {
        m_velocityY = m_speed * direction;
    }
    
    public boolean getLife()
    {
        return m_dead;
    }
    
    public void setLife(boolean life)
    {
        m_dead = life;
    }
    
    public int getSpeed()
    {
        return m_speed;
    }
    
    public int getRange()
    {
        return m_bombRange;
    }
    
    public int getBombNumber()
    {
        return m_bombMaxCurrent;
    }
    
    public void setSpeed(int speed)
    {
        m_speed = speed;
    }
    
    public void setRange(int range)
    {
        m_bombRange = range;
    }
    
    public void setBombNumber(int number)
    {
        m_bombMaxCurrent = number;
    }
    
    public int width()
    {
        return m_width;
    }
    
    public int height()
    {
        return m_height;
    }
    
    @Override
    public int dx()
    {
        return (m_width - SCALE * image().getWidth()) / 2;
    }
    
    @Override
    public int dy()
    {
        return -SCALE * image().getHeight() / 2;
    }
    
    @Override
    public void update(UpdateEvent e)
    {
        super.update(e);
        
        if(m_dead)
        {
            velocityX(0);
            velocityY(0);
            return;
        }
        
        if(m_left && !m_right)
            velocityX(-1);
        else if(m_right && !m_left)
            velocityX(1);
        else
            velocityX(0);
        
        if(m_up && !m_down)
            velocityY(-1);
        else if(m_down && !m_up)
            velocityY(1);
        else
            velocityY(0);
        
        if(velocityY() > 0)
            currentAnim("walkDown");
        else if(velocityX() < 0)
            currentAnim("walkLeft");
        else if(velocityX() > 0)
            currentAnim("walkRight");
        else if(velocityY() < 0)
            currentAnim("walkUp");
        else
        {
            switch(currentAnim())
            {
                case "walkLeft":
                    currentAnim("idleLeft");
                    break;
                case "walkRight":
                    currentAnim("idleRight");
                    break;
                case "walkUp":
                    currentAnim("idleUp");
                    break;
                case "walkDown":
                    currentAnim("idleDown");
                    break;
            }
        }
        
        if(m_bomb)
        {
            // todo: add a new Bomb to m_game at the appropriate location
            m_game.add(new Bomb(this.getX(), this.getY(), this.m_bombTicks, this.m_bombRange, this.m_bombMaxCurrent));
            m_bomb = false;
        }
    }
    
    @Override
    public void animationStarted(AnimationEvent e)
    {}
    
    @Override
    public void animationEnded(AnimationEvent e)
    {
        if(e.name() == "die")
            m_game.remove(this);
    }
    
    //explsion event
    @Override
    public void OnExplosion(ExplosionEvent e)
    {
        int row = this.getY() / Board.TILE_SIZE;
        int col = this.getX() / Board.TILE_SIZE;
        if(e.getCol() == col && e.getRow() == row)
            die();
    }
    
    @Override
    public void powerUpRange(PowerUpEvent e)
    {
        if(e.type() == 0 && !e.explodeByBomb())
        {
            int row = this.getY() / Board.TILE_SIZE;
            int col = this.getX() / Board.TILE_SIZE;
            if(e.getCol() == col && e.getRow() == row)
            {
                if(m_bombRange < MAXIMUM_RANGE)
                    m_bombRange ++;
                e.getPower().setTouch();
            }
        }
    }
    
    @Override
    public void powerUpBombNumber(PowerUpEvent e)
    {
        if(e.type() == 1 && !e.explodeByBomb())
        {
            int row = this.getY() / Board.TILE_SIZE;
            int col = this.getX() / Board.TILE_SIZE;
            if(e.getCol() == col && e.getRow() == row)
            {
                if(m_bombMaxCurrent <= MAXIMUM_BOMB_NUMBER)
                    m_bombMaxCurrent ++;
                e.getPower().setTouch();
            }
        }
    }
    
    @Override
    public void powerUpSpeed(PowerUpEvent e)
    {
        // make sure is a speedup power
        if(e.type() == 2 && !e.explodeByBomb())
        {
            int row = this.getY() / Board.TILE_SIZE;
            int col = this.getX() / Board.TILE_SIZE;
            if(e.getCol() == col && e.getRow() == row)
            {
                if(m_speed <= MAXIMUM_SPEED)
                    m_speed ++;
                e.getPower().setTouch();
            }
        }
    }
    
    @Override
    public void powerUpType(PowerUpEvent e)
    {
        //nothing here
    }
    
}
