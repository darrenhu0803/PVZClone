package gui.panel;

import javax.imageio.ImageIO;
import javax.swing.*;

import entity.character.plant.CabbagePult;
import entity.character.plant.Peashooter;
import entity.character.plant.Sunflower;
import gui.GUI;
import gui.component.Component;
import handler.KeyHandler;
import handler.MouseHandler;
import util.Registries;

import java.awt.*;
import java.util.concurrent.Callable;

public class TitlePanel extends JPanel implements Runnable {

	Thread thread;
	
	KeyHandler keyH = new KeyHandler();
	MouseHandler mouseH = new MouseHandler();
	public GUI gui;
	Callable<?> func;
	
    public TitlePanel() {
        setLayout(new BorderLayout());
        
        panelInit();
        guiInit();
        
        
    }
    
    void panelInit() {
		setPreferredSize(new Dimension(GamePanel.screenWidth, GamePanel.screenHeight));
		setBackground(Color.black);
		setDoubleBuffered(true);
		addKeyListener(keyH);
		addMouseListener(mouseH);
		addMouseMotionListener(mouseH);
		setFocusable(true);
	}
    
    void guiInit() {
		gui = new GUI();
		
		gui.addBackground(new Component(Registries.TITLE_SCREEN));
		
	}
    
    
    @Override
    protected void paintComponent(Graphics g) {
    	super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		gui.drawBackground(g2);
		gui.draw(g2);
		g2.dispose();
    }

	@Override
	public void run() {
		double drawInterval = 1000000000 / 30;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;

		while (thread != null) {

			currentTime = System.nanoTime();

			delta += (currentTime - lastTime) / drawInterval;

			lastTime = currentTime;

			if (delta >= 1) {
				update();
				repaint();
				delta--;
			}

		}
	}
	
	public void update() {

		gui.update(mouseH);
		
		
        mouseH.clicked = false;
		
        
	}
	
	public void startThread() {
		thread = new Thread(this);
		thread.start();
	}
}