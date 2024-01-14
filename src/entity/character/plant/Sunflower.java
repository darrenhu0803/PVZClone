package entity.character.plant;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import entity.EntityManager;
import entity.character.zombie.Zombie;
import entity.projectile.Sun;
import gui.panel.GamePanel;
import util.AnimationController;
import util.Registries;

public class Sunflower extends Plant{

	public Sunflower(Integer row, Integer column){
		super(new Plant.Builder()
				.row(row)
				.column(column)
				.rootX(64)
				.rootY(124)
				.width(90)
				.height(110)
				.hpMax(200)
				.cost(50)
				.registry(Registries.SUNFLOWER)
				.attackInterval(600));
	}
	
	@Override
	void checkAndAttack(List<Zombie> zombies) {
		lastAttackFrame = gamePanel.timer;
		attacking = true;
        animationController.play("attack");
        animationController.setSpeed(60);
	}

	@Override
	void attack(EntityManager entityManager) {
		int tempY = (int) y + (int) (Math.random() * 40)  - (int) (Math.random() * 60);
		entityManager.spawnEntity(Sun.class, (int) x + (int) (Math.random() * 40)  - (int) (Math.random() * 40), tempY, tempY + 50);
        animationController.setSpeed(10);
    }
}
