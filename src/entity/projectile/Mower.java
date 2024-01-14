package entity.projectile;

import gui.panel.GamePanel;
import util.AnimationController;
import util.Registries;

public class Mower extends Projectile{

	public Mower(Integer row) {
		super(GamePanel.OFFSET_X - GamePanel.tileX + 50, row * GamePanel.tileY, 0, 0, 100000.0);
		this.registry = Registries.MOWER;
		this.animationController = new AnimationController(registry, 10);
	}
	
	@Override
	public void explode() {
		dx = 20;
	}

}
