package util;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Animation {
	BufferedImage[] sprites;
	int count, x, y;
	int[] offsetX, offsetY, widths, heights;
	String name, path, nextAnimation;
	
	
	public Animation(BufferedImage[] sprites, int[] offsetX, int[] offsetY, String name, boolean loop) {
		this.sprites = sprites;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.name = name;
	}
	

    public Animation(String name, String path, int count, int x, int y,
                           int[] widths, int[] heights, 
                           int[] offsetX, int[] offsetY, String nextAnimation) {
        this.name = name;
        this.path = path;
        this.count = count;
        this.x = x;
        this.y = y;
        this.widths = widths;
        this.heights = heights;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.nextAnimation = nextAnimation;
        sprites = new BufferedImage[count];
        registerSprites();
    }
    
    public void registerSprites() {
    	BufferedImage spriteSheet = null;
    	System.out.println(path);
    	try {
            spriteSheet = ImageIO.read(getClass().getResourceAsStream(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    	int x1 = x;
    	int y1 = y;
		for(int i = 0; i < count; i++) {
			sprites[i] = spriteSheet.getSubimage(x1, y1, widths[i], heights[i]);
			x1 += widths[i];
		}
	}

	
	public BufferedImage[] getSprites() {
		return sprites;
	}
	
	public BufferedImage getSprite(int index) {
		return sprites[index];
	}

	public int[] getOffsetX() {
		return offsetX;
	}

	public int[] getOffsetY() {
		return offsetY;
	}

	public String getName() {
		return name;
	}
	
	public String getNextAnimation() {
		return nextAnimation;
	}
}
