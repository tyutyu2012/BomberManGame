import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class SpriteSheet
{
	private static Map<String, SpriteSheet> SHEETS = new HashMap<>();
	public static SpriteSheet SheetNamed(String file)
	{
		if(!SHEETS.containsKey(file))
			SHEETS.put(file, new SpriteSheet(file));
		return SHEETS.get(file);
	}
	
	private List<Frame> frames = new LinkedList<>();
	private Map<String, List<AnimFrame>> anims = new HashMap<>();
	private String defaultAnim;
	
	private SpriteSheet(String file)
	{
		try
		{
			Scanner scanner = new Scanner(new File("./resources/" + file + ".txt"));
			
			String imageFile = scanner.nextLine();
			BufferedImage image = ImageIO.read(new File("./resources/" + imageFile));
			
			int frameCount = scanner.nextInt();
			for(int i = 0; i < frameCount; ++i)
			{
				int x = scanner.nextInt();
				int y = scanner.nextInt();
				int width = scanner.nextInt();
				int height = scanner.nextInt();
				int dx = scanner.nextInt();
				int dy = scanner.nextInt();
				frames.add(new Frame(image.getSubimage(x, y, width, height), dx, dy));
			}
			
			int animCount = scanner.nextInt();
			for(int i = 0; i < animCount; ++i)
			{
				String name = scanner.next();
				anims.put(name, new LinkedList<AnimFrame>());
				
				int animFrameCount = scanner.nextInt();
				for(int j = 0; j < animFrameCount; ++j)
				{
					int frame = scanner.nextInt();
					int delay = scanner.nextInt();
					anims.get(name).add(new AnimFrame(frames.get(frame), delay));
				}
				
				if(i == 0)
					defaultAnim = name;
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public String getDefaultAnim()
	{
		return defaultAnim;
	}
	
	public Map<String, List<AnimFrame>> getAnims()
	{
		return anims;
	}
}
