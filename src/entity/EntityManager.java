package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import entity.character.plant.Plant;
import entity.character.plant.Shovel;
import entity.character.zombie.Zombie;
import entity.projectile.Mower;
import entity.projectile.Projectile;
import entity.projectile.Sun;
import gui.panel.GamePanel;
import handler.KeyHandler;
import handler.MouseHandler;
import resource.ResourceManager;

public class EntityManager{
	private static EntityManager instance;
	private Plant pendingPlant;
	private int offsetX, offsetY;
	private List<Entity> entities;
	
	ResourceManager resourceManager = ResourceManager.getInstance();
    
	public boolean plantFlag;
	
    private EntityManager() {
		this.pendingPlant = null;
		this.plantFlag = false;
		this.entities = new ArrayList<>();
    }

    public static synchronized EntityManager getInstance() {
        if (instance == null) {
            instance = new EntityManager();
        }
        return instance;
    }
    
    
    public void setOffset(int x, int y) {
    	offsetX = x;
    	offsetY = y;
    }
    

	public void addPendingPlant(Plant pendingPlant) {
		this.pendingPlant = pendingPlant;
	}
	
	public void removePendingPlant() {
		pendingPlant = null;
	}
	
	public Plant getPendingPlant() {
		return pendingPlant;
	}
	
	public void removeDeadEntity() {
		for(int i = 0; i < entities.size(); i++) {
			if(entities.get(i).isDead() || entities.get(i).isOutbound()) {
				entities.remove(i);
			}
		}
	}
	
	public void resetAll() {
		removeAll();
		spawnMower();
	}
	
	public void removeAll() {
		entities = new ArrayList<>();
	}
	
	public void spawnMower() {
		spawnEntity(Mower.class, 1);
		spawnEntity(Mower.class, 2);
		spawnEntity(Mower.class, 3);
		spawnEntity(Mower.class, 4);
		spawnEntity(Mower.class, 5);
	}
	
	public List<Plant> getPlants(){
		List<Plant> plants = new ArrayList<>();
		for (Entity entity : entities) {
			if(entity instanceof Plant) {
				plants.add((Plant) entity);
			}
		}
		return plants;
	}
	
	public List<Zombie> getZombies(){
		List<Zombie> zombies = new ArrayList<>();
		for (Entity entity : entities) {
			if(entity instanceof Zombie) {
				zombies.add((Zombie) entity);
			}
		}
		return zombies;
	}
	
	public List<Projectile> getProjectile(){
		List<Projectile> projectiles = new ArrayList<>();
		for (Entity entity : entities) {
			if(entity instanceof Projectile) {
				projectiles.add((Projectile) entity);
			}
		}
		return projectiles;
	}
	
	public double[] getZombieProbability() {
		double[] proportion = new double[] {1.0, 1.0, 1.0, 1.0, 1.0};
		for(Zombie zombie : getZombies()) {
			proportion[zombie.getRow()]++;
		}
		
		double s = Arrays.stream(proportion).max().getAsDouble() + 1;
		Arrays.setAll(proportion, i -> Math.pow((s - proportion[i]), 1.5));
		
		
		double sum = Arrays.stream(proportion).sum();
		Arrays.setAll(proportion, i -> proportion[i] / sum);
		
		
		return proportion;
	}
	
	public void spawnEntity(Entity entity) {
		entities.add(entity);
	}
	
	
	public <T extends Entity> T spawnEntity(Class<T> clazz, Object... args) {
	    try {
	        // Find a compatible constructor
	        Constructor<T> matchingConstructor = null;
	        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
	            Class<?>[] paramTypes = constructor.getParameterTypes();
	            if (paramTypes.length != args.length) {
	                continue;
	            }

	            boolean isCompatible = true;
	            for (int i = 0; i < paramTypes.length; i++) {
	                if (!paramTypes[i].isAssignableFrom(args[i].getClass())) {
	                    isCompatible = false;
	                    break;
	                }
	            }

	            if (isCompatible) {
	                matchingConstructor = (Constructor<T>) constructor;
	                break;
	            }
	        }

	        if (matchingConstructor == null) {
	            throw new NoSuchMethodException("No compatible constructor found for " + clazz.getSimpleName());
	        }

	        // Create a new instance of the class
	        T entity = matchingConstructor.newInstance(args);
	        entities.add(entity);
	        return entity;
	    } catch (Exception e) {
	        throw new RuntimeException("Could not create instance of class: " + clazz.getSimpleName(), e);
	    }
	}
	
	public void update(MouseHandler mouseH) {
		removeDeadEntity();
		if(getPendingPlant() != null) {
			
			getPendingPlant().update();
			if(mouseH.y - GamePanel.OFFSET_Y - 30 <= 0 || mouseH.x - GamePanel.OFFSET_X - 30 + GamePanel.cameraX <= 0) {
				getPendingPlant().move(-1, -1);
			}else if(getPendingPlant().validPos((mouseH.y - GamePanel.OFFSET_Y - 30) / GamePanel.tileY, (mouseH.x - GamePanel.OFFSET_X - 30 + GamePanel.cameraX) / GamePanel.tileX)){
				getPendingPlant().move((mouseH.y - GamePanel.OFFSET_Y - 30) / GamePanel.tileY, (mouseH.x - GamePanel.OFFSET_X - 30 + GamePanel.cameraX) / GamePanel.tileX);
			}
			
			if(mouseH.clicked && getPendingPlant().validPos()) {
				getPendingPlant().plant();
				removePendingPlant();
				plantFlag = true;
			}
		}
		ArrayList<Entity> temp = new ArrayList<>(entities);
		for (Entity entity : temp) {
			entity.update();
		}
		if (mouseH.clicked) {
			for (Entity entity : entities) {
				if(entity instanceof Sun) {
					if(((Sun) entity).collide(new Rectangle(mouseH.x, mouseH.y, 1, 1))) {
						((Sun)entity).action();
					}
				}
			}

			mouseH.clicked = false;
		}
		removeDeadEntity();
	}
	
	public void draw(Graphics2D g2, MouseHandler mouseH) {

		for (Entity entity : entities) {
			entity.draw(g2);
		}
		
		if (getPendingPlant() != null) {
			getPendingPlant().draw(g2);
			g2.drawImage(resourceManager.getAnimation(getPendingPlant().getRegistry(), "default").getSprite(0),
					mouseH.x - offsetX, mouseH.y - offsetY, null);
		}
	}
}
