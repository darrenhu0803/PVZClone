package entity.projectile;

import entity.character.zombie.Zombie;
import util.AnimationController;
import util.Registries;

public class Cabbage extends Lob{

	public Cabbage(Double startX, Double startY, Double targetX, Double targetY, Zombie target) {
		super(startX, startY, targetX, targetY, 600.0, 40, target, 10.0);
		this.registry = Registries.CABBAGE;
		this.animationController = new AnimationController(registry, 10);
	}

}
