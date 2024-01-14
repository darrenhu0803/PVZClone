package entity.character.zombie;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import entity.Entity;
import entity.character.plant.Plant;
import gui.panel.GamePanel;

public class Zombie extends Entity {
	int row;
	int rootX, rootY;
	protected Rectangle range;
	Plant attackingPlant;
	int weight;
	double speed;

	public Zombie(int row, int col, int rootX, int rootY, double hpMax) {
		super();
		this.row = row;
		this.rootX = rootX;
		this.rootY = rootY;
		this.hpMax = hpMax;
		this.hp = hpMax;
		this.x = col * GamePanel.tileX + GamePanel.OFFSET_X + 95 - rootX;
		this.y = row * GamePanel.tileY + GamePanel.OFFSET_Y + 155 - rootY;
		this.boundingBox = new Rectangle((int) x, (int) y, width, height);
		this.attackingPlant = null;
		this.speed = Math.random() * 0.1 + 1;
	}

	public void update() {
		super.update();
		boolean flag = true;
		for(Plant plant : entityManager.getPlants()) {
			if(plant.collide(range)) {
				flag = false;
				attack(plant);
			}
		}
		if(flag) {
			attackingPlant = null;
			if(hp <= hpMax / 2) {
				animationController.play("injured");
			}else {
				animationController.play("default");
			}
			
		}
		if (attackingPlant != null && animationController.isAnimationFinished()) {
            attackingPlant.takeDamage(10);
            animationController.animationFinished = false;
        }
		
		Double[][] move = resourceManager.getMovement(registry, animationController.getCurrentAnimation());
		double dx = 0;
		// double dy = 0;
		if (move != null) {
			dx = resourceManager.getMovement(registry, animationController.getCurrentAnimation())[animationController.getSpriteIndex()][0] / animationController.getAnimationSpeed()
					* (1 + 0.5 / animationController.getAnimationSpeed());
			
			// dy = resourceManager.getMovement(registry, currentAnimation)[spriteIndex][1]
			// / animationSpeed * (1 + 0.5 / animationSpeed);
		}
		moveX(dx * speed);
		
		
	}
	
	public int getRow() {
		return row;
	}
	
	public int getWeight() {
		return weight;
	}

	public void attack(Plant plant) {
		attackingPlant = plant;
		if (!animationController.getCurrentAnimation().equals("attack")) {
			animationController.play("attack");
		}

	}

	@Override
	public void draw(Graphics2D g2) {
		super.draw(g2);
		g2.draw(range);
	}
}
