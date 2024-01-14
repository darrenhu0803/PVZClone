package entity.projectile;

import java.awt.Rectangle;

import entity.Entity;
import entity.character.zombie.Zombie;

public class Projectile extends Entity{
	protected double dx, dy;
	protected double damage;
	boolean explodeAnimation;
	public Projectile(double x, double y, double dx, double dy, double damage) {
		
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
		this.width = 64;
		this.height =  64;
		this.boundingBox = new Rectangle((int)x, (int)y, width, height);
		this.hp = 1;
		this.enable = true;
		this.damage = damage;
		explodeAnimation = false;
	}
	
	public void update() {
		super.update();
		if(!explodeAnimation) {
			x += dx;
			y += dy;
		}
		for(Zombie zombie : entityManager.getZombies()) {
			if(checkCollide(zombie) && !explodeAnimation) {
				zombie.takeDamage(damage);
				explode();
			}
		}
		if(explodeAnimation && animationController.isAnimationFinished()) {
			dead = true;
		}
	}
	
	public boolean checkCollide(Zombie zombie) {
		return zombie.collide(boundingBox);
	}
	
	public void explode() {
		animationController.play("explode");
		animationController.setSpeed(5);
		explodeAnimation = true;
	}
}
