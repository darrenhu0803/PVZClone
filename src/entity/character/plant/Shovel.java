package entity.character.plant;

import java.util.List;

import entity.EntityManager;
import entity.character.zombie.Zombie;
import entity.projectile.Sun;
import gui.panel.GamePanel;
import util.Registries;

public class Shovel extends Plant{
	public Shovel(Integer row, Integer column){
		super(new Plant.Builder()
				.row(row)
				.column(column)
				.rootX(0)
				.rootY(0)
				.width(0)
				.height(0)
				.hpMax(200)
				.cost(0)
				.registry(Registries.SHOVEL)
				.attackInterval(9999));
	}
	
	
	
	@Override
	public void plant() {
		
		for(int i = 0; i < entityManager.getPlants().size(); i++) {
			if(entityManager.getPlants().get(i).row == row && entityManager.getPlants().get(i).column == column) {
				entityManager.getPlants().get(i).takeDamage(100000);
			}
		}
		takeDamage(10000);
	}
	
	@Override
	public void update() {
		super.update();
		animationController.setAlpha(0);	
	}
	
	@Override
	public boolean validPos(int row, int col) {
		return true;
	}
}
