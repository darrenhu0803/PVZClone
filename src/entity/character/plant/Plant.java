package entity.character.plant;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.List;

import entity.Entity;
import entity.EntityManager;
import entity.character.zombie.Zombie;
import entity.projectile.Pea;
import gui.panel.GamePanel;
import util.AnimationController;

public class Plant extends Entity{
	int row, column;
	int rootX, rootY;
	Rectangle range;
	boolean attacking;
	double attackInterval;
	int lastAttackFrame;
	int cost;
	Zombie attackingTarget;
	
	public Plant(Builder builder) {
		super();
        this.row = builder.row;
        this.column = builder.column;
        this.rootX = builder.rootX;
        this.rootY = builder.rootY;
        this.width = builder.width;
        this.height = builder.height;
        this.hpMax = builder.hpMax;
        this.registry = builder.registry;
        this.attackInterval = builder.attackInterval;
        this.cost = builder.cost;

		this.animationController = new AnimationController(registry, 10);
        this.x = column * GamePanel.tileX + GamePanel.OFFSET_X + 125 - rootX;
        this.y = row * GamePanel.tileY + GamePanel.OFFSET_Y + 185 - rootY;
        this.hp = hpMax;
        this.boundingBox = new Rectangle((int)x, (int)y, width, height);
        this.attacking = false;
		this.range = new Rectangle((int)x, (int)y, 1, 1);
    }
	
	public void plant() {
		this.enable = true;
		this.animationController.setAlpha(1);
		GamePanel.sun -= cost;
	}
	
	public void move(int row, int column) {
		this.row = row;
		this.column = column;
		this.x = column * GamePanel.tileX + GamePanel.OFFSET_X + 125 - rootX;
		this.y = row * GamePanel.tileY + GamePanel.OFFSET_Y + 185 - rootY;
	}
	
	public void draw(Graphics2D g2) {
		if(validPos()) {
			this.animationController.draw(g2, (int)this.x - gamePanel.cameraX, (int)this.y);
		}
		g2.draw(boundingBox);
	}
	
	public boolean validPos() {
		return validPos(row, column);
	}
	
	public boolean validPos(int row, int col) {
		for(Plant plant : entityManager.getPlants()) {
			if(plant.row == row && plant.column == col && !plant.equals(this) && !plant.getClass().equals(Shovel.class)) {
				return false;
			}
		}
		return row >= 0 && row < 5 && col >= 0 && col < 9;
	}

	void checkAndAttack(List<Zombie> zombies) {

		for(Zombie zombie : zombies) {

			if(zombie.collide(range)) {

				lastAttackFrame = gamePanel.timer;
				attacking = true;
		        animationController.play("attack");
			}
		}
	}
	
	public void update() {
		super.update();
		if(gamePanel.timer - lastAttackFrame >= attackInterval) {
			checkAndAttack(entityManager.getZombies());
		}
		if (attacking && animationController.isAnimationFinished()) {
            attack(entityManager);
            attacking = false;
        }
	}
	
	void attack(EntityManager entityManager) {
		
    }
	
	public void clicked() {
		
	}
	
	public static class Builder {
        private Integer row;
        private Integer column;
        private Integer rootX;
        private Integer rootY;
        private Integer width;
        private Integer height;
        private Integer hpMax;
        private Integer animationSpeed = 10;
        private Integer cost;
        private Integer attackInterval = 60;
        private String registry;

        public Builder row(Integer row) {
            this.row = row;
            return this;
        }

        public Builder column(Integer column) {
            this.column = column;
            return this;
        }

        public Builder rootX(Integer rootX) {
            this.rootX = rootX;
            return this;
        }

        public Builder rootY(Integer rootY) {
            this.rootY = rootY;
            return this;
        }

        public Builder width(Integer width) {
            this.width = width;
            return this;
        }

        public Builder height(Integer height) {
            this.height = height;
            return this;
        }

        public Builder hpMax(Integer hpMax) {
            this.hpMax = hpMax;
            return this;
        }

        public Builder cost(Integer cost) {
            this.cost = cost;
            return this;
        }

        public Builder attackInterval(Integer attackInterval) {
            this.attackInterval = attackInterval;
            return this;
        }
        
        public Builder registry(String registry) {
        	this.registry = registry;
        	return this;
        }
        
        public Builder animationSpeed(Integer animationSpeed) {
        	this.animationSpeed = animationSpeed;
        	return this;
        }

        public Plant build() {
            return new Plant(this);
        }
    }
	
}
