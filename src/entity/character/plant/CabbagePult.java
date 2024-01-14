package entity.character.plant;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import entity.EntityManager;
import entity.character.zombie.Zombie;
import entity.projectile.Cabbage;
import gui.panel.GamePanel;
import util.AnimationController;
import util.Registries;

public class CabbagePult extends Plant {

	public CabbagePult(Integer row, Integer column) {
		super(new Plant.Builder()
				.row(row)
				.column(column)
				.rootX(64)
				.rootY(112)
				.width(80)
				.height(110)
				.hpMax(200)
				.cost(50)
				.registry(Registries.CABBAGE_PULT));
		this.range = new Rectangle((int)x - 50, (int)y, 10000, 36);
		this.attackInterval = 60 * 1;
		
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
		range = new Rectangle((int)(x - gamePanel.cameraX) - 50 + GamePanel.tileX - 20, (int)y, 10000, 36);
	}
	
	@Override
	public void draw(Graphics2D g2) {
		super.draw(g2);
		g2.draw(range);
	}
	
	@Override
	void attack(EntityManager entityManager) {
		entityManager.spawnEntity(Cabbage.class, x + GamePanel.tileX - 80, y - 140, attackingTarget.getX(), attackingTarget.getY(), attackingTarget);
    }
	
}
