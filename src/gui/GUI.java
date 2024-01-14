package gui;

import java.awt.Graphics2D;
import java.util.ArrayList;

import entity.EntityManager;
import entity.character.plant.CabbagePult;
import entity.character.plant.Peashooter;
import entity.character.plant.Sunflower;
import gui.component.Button;
import gui.component.Card;
import gui.component.Component;
import gui.component.Container;
import gui.component.Digit;
import gui.panel.GamePanel;
import handler.MouseHandler;
import util.AnimationController;
import util.Registries;

public class GUI{
	EntityManager entityManager = EntityManager.getInstance();

	ArrayList<Card> deck;
	ArrayList<Component> background;
	ArrayList<Component> components;
	Container p1, sunContainer;
	Component startText;
	Digit sun;
	
	
	public GUI() {
		deck = new ArrayList<>();
		background = new ArrayList<>();
		components = new ArrayList<>();
	}
	
	
	public void update(MouseHandler mouseH) {
		for (int i = 0; i < deck.size(); i++) {
			deck.get(i).x = i * 108 + 175;
			deck.get(i).update();
		}
		for(Component component : background) {
			component.update();
			
		}
		for(Component component : components) {
			component.update();
			if (component instanceof Button) {
				Button button = (Button) component;
				if(button.contains(mouseH.x, mouseH.y) && mouseH.clicked) {
					button.action();
					mouseH.clicked = false;
				}
			}
			if(component instanceof Container) {
				Container container = (Container) component;
				container.update(mouseH);
			}
		}
		for(Card card : deck) {
			if(card.contains(mouseH.x, mouseH.y) && mouseH.clicked) {
				card.action(mouseH.x, mouseH.y);
				mouseH.clicked = false;
			}
		}
		if(entityManager.plantFlag) {
			for(Card card : deck) {
				card.clicked = false;
			}
			entityManager.plantFlag = false;
		}
	}
	
	public void drawBackground(Graphics2D g2) {
		for(Component component : background) {
			component.draw(g2, -GamePanel.cameraX, 0);
		}
	}
	
	
	public void draw(Graphics2D	g2) {
		for(Card card : deck) {
			card.draw(g2);
		}
		for(Component component : components) {
			component.draw(g2);
		}
	}
	

	public void addDeck(Card card) {
		deck.add(card);
	}
	
	public void addBackground(Component component) {
		background.add(component);
	}

	public void addComponent(Component component) {
		components.add(component);
	}
	
	public void setStartText(Component startText) {
		this.startText = startText;
	}
	
	public void showStartText() {
		startText.setVisible(true);
		startText.getAnimationController().play("default");
	}
}

