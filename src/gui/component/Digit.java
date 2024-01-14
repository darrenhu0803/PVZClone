package gui.component;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import util.Registries;

public class Digit extends Component {

	public int number;
	
	public Digit(int x, int y, int number) {
		super(Registries.FONT_DIGIT, x, y);
		this.number = number;
	}
	
	public void setNumber(int number) {
		this.number = number;
	}
	
	public int getNumber() {
		return number;
	}
	
	@Override
	public void draw(Graphics2D g2) {
		drawNumber(x, y, g2);
	}
	
	public void drawNumber(int x, int y, Graphics2D g2) {
        String numberStr = Integer.toString(number);
        for (int i = 0; i < numberStr.length(); i++) {
            int digit = Character.digit(numberStr.charAt(i), 10);
            BufferedImage digitImage = resourceManager.getAnimation(registry, "default").getSprite(digit);
            g2.drawImage(digitImage, x + (i * digitImage.getWidth()), y, null);
        }
    }
	
}
