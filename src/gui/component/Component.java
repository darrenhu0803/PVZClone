package gui.component;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import entity.EntityManager;
import gui.panel.GamePanel;
import resource.ResourceManager;
import util.AnimationController;

public class Component{
	public int x, y;
	String registry;
	public BufferedImage currentImage;
	boolean visible;
	ResourceManager resourceManager = ResourceManager.getInstance();
	EntityManager entityManager = EntityManager.getInstance();
	protected AnimationController animationController;
	
	public Component(String registry) {
		this(registry, 0, 0);
	}
	
	public Component(String registry, int x, int y) {
		this.registry = registry;
		
		this.currentImage = registry == null ? null : resourceManager.getAnimation(registry, "default").getSprite(0);
		this.x = x;
		this.y = y;
		this.visible = true;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public void update() {
		currentImage = resourceManager.getAnimation(registry, "default").getSprite(0);
		
	}
	
	public void draw(Graphics2D g2) {
		if(animationController != null) {
			if(visible) {
				animationController.draw(g2, x, y);
			}
		}else {
			draw(g2, 0, 0);
		}
	}
	
	public void setAnimationController(AnimationController animationController) {
		this.animationController = animationController;
	}
	
	public AnimationController getAnimationController() {
		return animationController;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void draw(Graphics2D g2, int offsetX, int offsetY) {
		if(visible) {
			g2.drawImage(currentImage, x + offsetX, y + offsetY, null);
		}
	}
	
	public void draw(Graphics2D g2, int offsetX, int offsetY, int width, int height) {
		if(visible) {
			g2.drawImage(currentImage, x + offsetX, y + offsetY, width, height, null);
		}
	}
	
	
}
