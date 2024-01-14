package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.*;

import gui.component.Button;
import gui.panel.GamePanel;
import gui.panel.TitlePanel;
import util.Registries;

public class PVZClone {

	private JFrame window;
	private GamePanel gamePanel;
	private TitlePanel titlePanel;
	
	
	
	public PVZClone() {
		window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setTitle("PVZ Clone");
		window.setResizable(false);

		titlePanel = new TitlePanel();
		titlePanel.startThread();
		titlePanel.gui.addComponent(new Button(Registries.START_BUTTON, 638, 90) {
			@Override
			public void action() {
				startGame();
			}
		});
		

		window.add(titlePanel);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);

	}

	public void startGame() {
		window.remove(titlePanel);

		gamePanel = readOrCreateGamePanel();
		window.add(gamePanel);
		window.pack();

		gamePanel.startGameThread();
	}
	

    
    public static GamePanel readOrCreateGamePanel() {
    	GamePanel gamePanel;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("gamePanel.ser"))) {
        	gamePanel = (GamePanel) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("File not found. Creating a new counter.");
            gamePanel = GamePanel.getInstance();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return gamePanel;
    }
    
	public static void main(String[] args) {
		new PVZClone();
	}
	
}
