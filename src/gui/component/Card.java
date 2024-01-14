package gui.component;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import entity.Entity;
import entity.character.plant.Plant;
import entity.character.plant.Shovel;
import gui.panel.GamePanel;

public class Card extends Button{

	public boolean clicked;
	int slot;
	Class<? extends Plant> plant;
	int cost;
	int cooldown;
	int lastUsedFrame;
	BufferedImage available, unavailable;
	
	public <T extends Plant> Card(String registry, Class<T> plant, int cost, int cooldown) {
		super(registry);
		this.state = "card";
		this.clicked = false;
		this.plant = plant;
		this.cost = cost;
		this.cooldown = cooldown;
		available = resourceManager.getAnimation(registry, "card").getSprite(0);
		unavailable = resourceManager.getAnimation(registry, "card_cooldown").getSprite(0);
	}
	

	public void update() {
		super.update();
		if(clicked) {
			lastUsedFrame = GamePanel.timer;
			clicked = false;
		}
	}
	
	@Override
	public void draw(Graphics2D g2) {
		double ratio = (double) (GamePanel.timer - lastUsedFrame) / (double) cooldown;
		ratio = Math.min(ratio, 1.0);
		if(visible) {
			int dy = (int) (currentImage.getHeight() * ratio);
			g2.drawImage(available, x, y, null);
			g2.drawImage(unavailable, x, y, currentImage.getWidth() + x, y + currentImage.getHeight() - dy, 0, 0, currentImage.getWidth(), currentImage.getHeight() - dy, null);
		}
	}
	
	public void action(int mouseX, int mouseY) {
		if(clicked) {
			entityManager.removePendingPlant();
			clicked = false;
		}else {
			if(GamePanel.sun >= cost && GamePanel.timer - lastUsedFrame > cooldown) {
				Plant p = (Plant) entityManager.spawnEntity(plant, -1, -1);
				p.setEnable(false);
				entityManager.addPendingPlant(p);
				if(plant.equals(Shovel.class)) {
					entityManager.setOffset(8, 67);
				}else {
					entityManager.setOffset(mouseX - x, mouseY - y);
				}
				
				clicked = true;
			}
		}
		
	}
	
}
