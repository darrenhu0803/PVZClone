package entity.projectile;

import util.AnimationController;
import util.Registries;

public class Pea extends Projectile{

	public Pea(Double x, Double y, Double dx, Double dy) {
		super(x, y, dx, dy, 10);
		this.registry = Registries.PEA;
		this.animationController = new AnimationController(registry, 10);
		this.damage = 10.0;
	}

}
