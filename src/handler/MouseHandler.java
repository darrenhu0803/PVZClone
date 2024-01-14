package handler;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseHandler extends MouseAdapter{
	public int x = 0, y = 0;
	public boolean clicked = false;
	
	public MouseHandler() {
		
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		clicked = true;
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		this.x = e.getX();
		this.y = e.getY();
	}
	
	
}
