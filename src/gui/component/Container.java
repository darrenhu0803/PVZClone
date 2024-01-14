package gui.component;

import java.awt.Graphics2D;
import java.util.ArrayList;

import handler.MouseHandler;

public class Container extends Component{
	ArrayList<Component> components;
	
	public Container(int x, int y) {
		super(null, x, y);
		components = new ArrayList<>();
	}
	
	public void addComponent(Component component) {
		component.x += x;
		component.y += y;
		this.components.add(component);
	}
	
	public ArrayList<Component> getComponents(){
		return components;
	}
	
	@Override
	public void setVisible(boolean visible) {
		for(Component component : components) {
			component.setVisible(visible);
		}
		this.visible = visible;
	}
	
	public boolean getVisible() {
		return visible;
	}

	@Override
	public void draw(Graphics2D g2) {
		for(Component component : components) {
			component.draw(g2);
		}
	}

	@Override
	public void update() {
		
	}
	
	public void update(MouseHandler mouseH) {
		for(Component component : components) {
			component.update();
			if (component instanceof Button) {
				Button button = (Button) component;
				if(button.contains(mouseH.x, mouseH.y) && mouseH.clicked) {
					button.action();
					mouseH.clicked = false;
				}
			}
		}
	}
}
