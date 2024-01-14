package gui.component;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import util.Registries;

public class ProgressBar extends Container{
	
	List<Component> flags;
	double p;
	Component progress, head;
	
	public ProgressBar(int x, int y) {
		super(x, y);
		flags = new ArrayList<>();
		p = 0.0;
		progress = new Component(Registries.PROGRESS_BAR_PROGRESS);
		head = new Component(Registries.PROGRESS_BAR_HEAD);
		this.addComponent(new Component(Registries.PROGRESS_BAR));
		//flags.add(new Component)
		
	}
	
	@Override
	public void update() {
		super.update();
		head.setX(476 - (int) (488 * p));
		
	}
	
	@Override
	public void draw(Graphics2D g2) {
		for(Component component : components) {
			component.draw(g2);
		}
		
		for(Component component : flags) {
			component.draw(g2, x, y);
		}
		
		progress.draw(g2, x + 500 - (int) (488 * p), y + 12, (int) (488 * p), 18);
		head.draw(g2, x, y - 4);
	}
	
	public void setProgress(double p) {
		this.p = p;
	}
	
	public void updateFlags(List<Double> newFlags) {
		if(newFlags.size() > flags.size()) {
			int s = flags.size();
			for(int i = 0; i < newFlags.size() - s; i++) {
				flags.add(new Component(Registries.PROGRESS_BAR_FLAG));
			}
		}
		for(int i = 0; i < flags.size(); i++) {
			if(i < newFlags.size()) {
				flags.get(i).setX(12 + (int) (448 * newFlags.get(i)));
				flags.get(i).setVisible(true);
			}else {
				flags.get(i).setVisible(false);
			}
		}
	}
	
}
