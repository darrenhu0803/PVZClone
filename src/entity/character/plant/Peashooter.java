package entity.character.plant;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import entity.EntityManager;
import entity.character.zombie.Zombie;
import entity.projectile.Cabbage;
import entity.projectile.Pea;
import gui.panel.GamePanel;
import util.Registries;

public class Peashooter extends Plant {

	public Peashooter(Integer row, Integer column) {
		//super(row, column, 64, 112, 128, 128, 200);
		super(new Plant.Builder()
				.row(row)
				.column(column)
				.rootX(64)
				.rootY(112)
				.width(90)
				.height(110)
				.hpMax(200)
				.cost(100)
				.registry(Registries.PEASHOOTER)
				.attackInterval(60));
		this.range = new Rectangle((int)x, (int)y, 1200, 36);
	}
	

	@Override
	void checkAndAttack(List<Zombie> zombies) {
		
		List<Zombie> zombiesInRange = new ArrayList<>();
		
		for(Zombie zombie : zombies) {

			if(zombie.collide(range)) {
				zombiesInRange.add(zombie);
			}
		}
		if(zombiesInRange.size() != 0) {
			Collections.sort(zombiesInRange, (a, b) -> (int) (a.getX() - b.getX()));
			attackingTarget = zombiesInRange.get(0);
			lastAttackFrame = gamePanel.timer;
			attacking = true;
	        animationController.play("attack");
		}
		
		
		
	}
	
	@Override
	public void update() {
		super.update();
		range = new Rectangle((int)(x - gamePanel.cameraX) + GamePanel.tileX - 20, (int)y, 10000, 36);
	}
	
	@Override
	public void draw(Graphics2D g2) {
		super.draw(g2);
		g2.draw(range);
	}
	
	@Override
	void attack(EntityManager entityManager) {
		entityManager.spawnEntity(Pea.class, (double)(x) + GamePanel.tileX - 20, (double)y, 9.0, 0.0);
    }

}
