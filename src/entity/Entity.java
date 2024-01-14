package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import gui.panel.GamePanel;
import resource.ResourceManager;
import util.AnimationController;

public class Entity{
	protected double x, y;
	protected Rectangle boundingBox;
	protected AnimationController animationController;
	protected double hp;
	protected double hpMax;
	protected int attack;
	protected boolean enable;
	protected String registry;
	protected GamePanel gamePanel = GamePanel.getInstance();
	protected EntityManager entityManager = EntityManager.getInstance();
	protected ResourceManager resourceManager = ResourceManager.getInstance();
	protected boolean dead;
	
	public int width;
	public int height;
	
	public Entity() {
		enable = true;
		dead = false;
	}
	
	public void update() {
		if(hp <= 0) {
			die();
		}
		if(enable) {
			boundingBox = new Rectangle((int)(x - gamePanel.cameraX), (int)y, width, height);
		}else {
			animationController.setAlpha((float) 0.5);
		}
	}
	
	public void takeDamage(double damage) {
		hp -= damage;
	}
	
	public void moveX(double dx) {
		x += dx;
	}
	
	public void die() {
		dead = true;
	}
	
	public boolean isDead() {
		return dead;
	}
	
	public boolean isOutbound() {
		return x <= -1000 || x >= 10000 || y <= -1000 || y >= 3000;
	}

	public boolean collide(Rectangle rec) {
		return this.boundingBox.intersects(rec);
	}
	
	public void draw(Graphics2D g2) {
		this.animationController.draw(g2, (int)this.x - gamePanel.cameraX, (int)this.y);
		g2.draw(boundingBox);
	}
	
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	
	public String getRegistry() {
		return registry;
	}
	
	public double getX() {
		return this.x;
	}
	
	public double getY() {
		return this.y;
	}
}
