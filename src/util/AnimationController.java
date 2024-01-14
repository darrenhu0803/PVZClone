package util;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import entity.Entity;
import gui.panel.GamePanel;
import resource.ResourceManager;

public class AnimationController{

	private ResourceManager resourceManager = ResourceManager.getInstance();
	private String registry;
	private Animation defaultAnimation;
	private Animation currentAnimation;
	private int animationSpeed;
	private int spriteIndex;
	private int animationCount;
	private float alpha;
	public boolean animationFinished;

	public AnimationController(String registry, int animationSpeed) {
		this(registry, animationSpeed, 1, "default");
	}

	public AnimationController(String registry, int animationSpeed, String defaultAnimation) {
		this(registry, animationSpeed, 1, defaultAnimation);
	}

	public AnimationController(String registry, int animationSpeed, float alpha, String defaultAnimation) {
		this.registry = registry;
		this.defaultAnimation = resourceManager.getAnimation(registry, defaultAnimation);
		this.currentAnimation = this.defaultAnimation;
		this.animationSpeed = animationSpeed;
		this.spriteIndex = 0;
		this.animationCount = 0;
		this.alpha = alpha;
		this.animationFinished = false;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	public void setSpeed(int speed) {
		animationSpeed = speed;
	}
	
	public void draw(Graphics2D g2, int x, int y) {
		
		if(spriteIndex >= currentAnimation.getSprites().length) {
			currentAnimation = defaultAnimation;
		}
		
		BufferedImage currentSprite = currentAnimation.getSprite(spriteIndex);
		
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		g2.drawImage(currentSprite, x + currentAnimation.getOffsetX()[spriteIndex], y + currentAnimation.getOffsetY()[spriteIndex], currentSprite.getWidth(), currentSprite.getHeight(), null);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		
		if(GamePanel.state == GamePanel.RUN) {
			this.animationCount++;
		}
		if (this.animationCount >= animationSpeed) {
			this.spriteIndex += 1;
			if (this.spriteIndex == currentAnimation.getSprites().length) {
				this.spriteIndex = 0;
				animationFinished = true;
				currentAnimation = resourceManager.getAnimation(registry, currentAnimation.getNextAnimation());
			} else {
				animationFinished = false;
			}

			this.animationCount = 0;
		}
	}

	public boolean isAnimationFinished() {
		return animationFinished;
	}

	public String getCurrentAnimation() {
		return currentAnimation.getName();
	}
	
	public int getSpriteIndex() {
		return spriteIndex;
	}
	
	public int getAnimationSpeed() {
		return animationSpeed;
	}

	public void play(String animation) {
		if(!animation.equals(getCurrentAnimation())) {
			this.currentAnimation = resourceManager.getAnimation(registry, animation);
			this.spriteIndex = 0;
			this.animationFinished = false;
		}
	}
	
	public void setIndex(int index) {
		spriteIndex = index;
	}

}
