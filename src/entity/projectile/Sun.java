package entity.projectile;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import entity.Entity;
import gui.panel.GamePanel;
import util.AnimationController;
import util.Registries;

public class Sun extends Entity{
	
	int targetY;
	boolean clicked;
	int c;

	public Sun(Integer x, Integer y){
		this(x, 0, y);
		c = 200;
	}
	
	public Sun(Integer x, Integer startY, Integer targetY){
		super();
		this.x = x;
		this.y = startY;
		this.targetY = targetY;
		this.registry = Registries.SUN;
		this.width = 104;
		this.height = 104;
		this.animationController = new AnimationController(registry, 10);
		this.animationController.setAlpha((float) 0.76);
		this.dead = false;
		this.hpMax = 1;
		this.hp = hpMax;
		this.clicked = false;
        this.boundingBox = new Rectangle((int)x, (int)y, width, height);
        this.c = 10;
	}
	
	@Override
	public void update() {
		super.update();
		
		if(clicked) {
			double dx = GamePanel.cameraX - x;
			double dy = -y;
			double l = Math.sqrt(dx * dx + dy * dy);
			if(l <= 10) {
				dead = true;
				GamePanel.sun += 50;
			}else {
				double s = 1 + Math.log(l + 1);
				x += dx / l * s * 3;
				y += dy / l * s * 3;
			}
			
		}else {
			if(targetY - y >= 200) {
				y += 2;
			}else if(targetY - y >= 0){
				y += 1 + (targetY - y) / c;
			}
		}
	}
	
	public static double[] unitVector(double x, double y, double targetX, double targetY) {
        // Step 1: Calculate the vector pointing from (x, y) to (targetX, targetY)
        double dx = targetX - x;
        double dy = targetY - y;

        // Step 2: Calculate the length of this vector
        double length = Math.sqrt(dx * dx + dy * dy);
        
        // Step 3: Normalize the vector to have a length of 1
        double[] unitVector = {dx / length, dy / length};

        return unitVector;
    }
	
	@Override
	public void draw(Graphics2D g2) {
		super.draw(g2);
	}
	

	public boolean collide(Rectangle rec) {
		return this.boundingBox.intersects(rec);
	}
	
	public void action() {
		clicked = true;
	}
}
