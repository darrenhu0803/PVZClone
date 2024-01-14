package entity.projectile;

import java.awt.Rectangle;

import entity.Entity;
import entity.character.zombie.Zombie;

public class Lob extends Entity{
    private double startX, startY; // Starting position
    private double targetX, targetY; // Target position
    private double peakHeight;
    private int totalTime;
    private float elapsedTime; // Elapsed time since start
    private final double damage;
    private Zombie target;

    // Constructor
    public Lob(Double startX, Double startY, Double targetX, Double targetY, Double peakHeight, int totalTime, Zombie target, Double damage) {
    	super();
        this.startX = startX;
        this.startY = startY;
        this.targetX = targetX;
        this.targetY = targetY;
        this.peakHeight = startY + peakHeight;
        this.totalTime = totalTime;
        this.target = target;
        // Initialize
        this.x = startX;
        this.y = startY;
        this.elapsedTime = 0;
        this.damage = damage;
		this.boundingBox = new Rectangle((int)x, (int)y, width, height);
		this.enable = true;
		this.width = 64;
		this.height =  64;
		this.hp = 1;
    }

    // Update method to be called in game loop
    public void update() {
        super.update();
        this.elapsedTime += 1.0;

        // Calculate the new position
        this.x += calculateDeltaX();
        this.y -= calculateDeltaY(elapsedTime);

        // Check if projectile has reached target or beyond
        
        if (this.x >= this.targetX) {
            onTargetReached();
        }
        
		if(target.collide(boundingBox)) {
			target.takeDamage(damage);
			dead = true;
		}
		
    }



	private double calculateDeltaX() {
		// Calculate horizontal position
		double velocityX = (targetX - startX) / totalTime;
        return velocityX;
	}

	private double calculateDeltaY(float time) {
        double halfTime = totalTime / 2;
        double initialVelocityY = (peakHeight - startY) * 2 / halfTime;
        double accelerationY = -2 * (peakHeight - startY) / (halfTime * halfTime);

        return initialVelocityY + accelerationY * time;
	}

    private void onTargetReached() {
        
    }
}