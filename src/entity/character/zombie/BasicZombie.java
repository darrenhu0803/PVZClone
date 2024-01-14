package entity.character.zombie;

import java.awt.Rectangle;

import gui.panel.GamePanel;
import util.AnimationController;
import util.Registries;

public class BasicZombie extends Zombie{

	public BasicZombie(Integer row, Integer col){
		super(row, col, 80, 160, 240);
		this.registry = Registries.BASIC_ZOMBIE;
		this.width = 140;
		this.height = 192;
		this.animationController = new AnimationController(registry, 25, "attack");
		this.weight = 1;
		this.range = new Rectangle((int)(x - gamePanel.cameraX) + 15, (int)y + 50, 40, 80);
	}
	
	@Override
	public void update() {
		super.update();
		range = new Rectangle((int)(x - gamePanel.cameraX) + 15, (int)y + 50, 40, 80);
	}
}
