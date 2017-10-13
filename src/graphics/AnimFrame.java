public class AnimFrame
{
	private static final int DELAY_SCALE = 2;
	
	private Frame frame;
	private int delay;
	
	public AnimFrame(Frame frame, int delay)
	{
		this.frame = frame;
		this.delay = delay * DELAY_SCALE;
	}
	
	public Frame getFrame()
	{
		return frame;
	}
	
	public int getDelay()
	{
		return delay;
	}
}
