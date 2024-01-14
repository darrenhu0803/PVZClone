package gui.component;

public class Button extends Component{
	String state;
	
	public Button(String registry) {
		this(registry, 0, 0);
	}
	
	public Button(String registry, int x, int y) {
		super(registry, x, y);
		this.state = "default";
	}
	
	public void update() {
		currentImage = resourceManager.getAnimation(registry, state).getSprite(0);
	}
	
	public boolean contains(int x, int y) {
        
		if(x > this.x && x < (this.x + this.currentImage.getWidth()) && y > this.y && y < (this.y + currentImage.getHeight())) {
			return currentImage.getRGB((int)(x - this.x), (int)(y - this.y)) >> 24 != 0;
		}
		
        return false;
    }
	
	public void action() {
		
	}
}
