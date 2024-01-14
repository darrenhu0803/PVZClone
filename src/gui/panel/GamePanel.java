package gui.panel;

import entity.EntityManager;
import entity.character.plant.CabbagePult;
import entity.character.plant.Peashooter;
import entity.character.plant.Sunflower;
import entity.character.plant.Shovel;
import entity.character.zombie.BasicZombie;
import entity.character.zombie.Zombie;
import entity.projectile.Sun;
import gui.GUI;
import gui.component.Button;
import gui.component.Card;
import gui.component.Component;
import gui.component.Container;
import gui.component.Digit;
import gui.component.ProgressBar;
import handler.KeyHandler;
import handler.MouseHandler;
import resource.ResourceManager;
import resource.ResourceManager.AnimationDeserializer;
import util.Animation;
import util.AnimationController;
import util.Registries;
import util.Util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.stream.Collectors;

import javax.swing.JPanel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class GamePanel extends JPanel implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static GamePanel instance;
	public static final int OFFSET_X = 380, OFFSET_Y = 90;
	public static final int tileX = 130, tileY = 160;

	public final static int screenWidth = 1280;
	public final static int screenHeight = 960;

	public static int cameraX; // 0, 320, 955
	int cameraFrom;
	Queue<Integer> cameraTo;
	int cameraInterval;

	public static final int FPS = 60;

	Thread gameThread;
	KeyHandler keyH = new KeyHandler();
	MouseHandler mouseH = new MouseHandler();

	EntityManager entityManager;
	ResourceManager resourceManager;
	LevelManager levelManager;
	GUI gui;
	ProgressBar progressBar;

	int nextSpawn;

	public final static int RUN = 1;
	public final static int PAUSE = 2;
	public final static int TRANSITION = 3;
	public static int state = TRANSITION;
	int stateBeforePause = state;

	public static int timer;
	public static int sun = 100;

	private int lastSmallWaveTime = 0;
	private int lastBigWaveTime = 0;

	public GamePanel() {

		entityManager = EntityManager.getInstance();
		resourceManager = ResourceManager.getInstance();
		init();
		levelManager = new LevelManager(1);

		this.cameraX = 0;
		this.cameraFrom = 0;
		this.cameraTo = new LinkedList<>();
		cameraTo.add(955);
		cameraTo.add(320);
		this.cameraInterval = 0;
		this.timer = -150;

	}

	public static synchronized GamePanel getInstance() {
		if (instance == null) {
			instance = new GamePanel();
		}
		return instance;
	}

	void init() {
		panelInit();
		resourceInit();
		guiInit();
	}

	void panelInit() {
		setPreferredSize(new Dimension(screenWidth, screenHeight));
		setBackground(Color.black);
		setDoubleBuffered(true);
		addKeyListener(keyH);
		addMouseListener(mouseH);
		addMouseMotionListener(mouseH);
		setFocusable(true);

	}

	void guiInit() {
		gui = new GUI();

		Container p1 = new Container(GamePanel.screenWidth / 2 - 696 / 2, GamePanel.screenHeight / 2 - 752 / 2);
		p1.addComponent(new Component(Registries.PAUSE_WINDOW));
		p1.addComponent(new Button(Registries.PAUSE_WINDOW_BUTTON, 68, 604) {
			@Override
			public void action() {
				GamePanel.state = stateBeforePause;
				p1.setVisible(false);
			}
		});
		p1.addComponent(new Button(Registries.PAUSE_WINDOW_SAVE_BUTTON, 142, 477) {
			@Override
			public void action() {

			}
		});
		p1.setVisible(false);
		gui.addComponent(p1);

		gui.addComponent(new Button(Registries.MENU_BUTTON, GamePanel.screenWidth - 96, 0) {
			@Override
			public void action() {
				stateBeforePause = state;
				GamePanel.state = GamePanel.PAUSE;
				p1.setVisible(true);
			}
		});
		gui.addBackground(new Component(Registries.BACKGROUND));
		gui.addBackground(new Component(Registries.GRASS, GamePanel.OFFSET_X, GamePanel.OFFSET_Y));

		Container sunContainer = new Container(0, 0);
		sunContainer.addComponent(new Component(Registries.SUN_GUI));
		Digit sun = new Digit(0, 0, 0) {
			@Override
			public void update() {
				this.number = GamePanel.sun;
			}
		};
		sunContainer.addComponent(sun);
		gui.addComponent(sunContainer);

		Component startText = new Component(Registries.START_TEXT, (GamePanel.screenWidth - 620) / 2,
				(GamePanel.screenHeight - 260) / 2) {
			@Override
			public void update() {
				super.update();
				if (animationController.animationFinished) {
					setVisible(false);
					animationController.animationFinished = false;
				}
			}
		};
		startText.setAnimationController(new AnimationController(Registries.START_TEXT, 60));
		startText.setVisible(false);
		gui.setStartText(startText);

		gui.addComponent(startText);
		gui.addDeck(new Card(Registries.PEASHOOTER, Peashooter.class, 100, 600));
		gui.addDeck(new Card(Registries.SUNFLOWER, Sunflower.class, 50, 360));
		gui.addDeck(new Card(Registries.CABBAGE_PULT, CabbagePult.class, 50, 600));
		gui.addDeck(new Card(Registries.SHOVEL, Shovel.class, 0, 0));

		gui.addComponent(
				new Component(Registries.LEVEL, GamePanel.screenWidth - 512 - 96 - 50, GamePanel.screenHeight - 42));
		gui.addComponent(new Digit(GamePanel.screenWidth - 512 - 50, GamePanel.screenHeight - 42, 0) {
			@Override
			public void update() {
				this.number = levelManager.currentLevel;
			}
		});

		progressBar = new ProgressBar(GamePanel.screenWidth - 512, GamePanel.screenHeight - 42);
		gui.addComponent(progressBar);

		progressBar.updateFlags(Arrays.asList(new Double[] { 0.0, 0.2, 0.4, 0.6, 0.8, 1.0 }));
	}

	void resourceInit() {

		resourceManager.registerMovement(Registries.BASIC_ZOMBIE, "default",
				new Double[][] { { 0.0, 0.0 }, { 0.0, 0.0 }, { 0.0, 0.0 }, { -2.5, 0.0 }, { -2.5, 0.0 }, { -5., 0.0 },
						{ -7.5, 0.0 }, { -12.5, 0.0 }, { -15., 0.0 }, { -12.5, 0.0 } });
		resourceManager.registerMovement(Registries.BASIC_ZOMBIE, "injured",
				new Double[][] { { 0.0, 0.0 }, { 0.0, 0.0 }, { 0.0, 0.0 }, { -2.5, 0.0 }, { -2.5, 0.0 }, { -5., 0.0 },
						{ -7.5, 0.0 }, { -12.5, 0.0 }, { -15., 0.0 }, { -12.5, 0.0 } });
	}

	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	@Override
	public void run() {

		double drawInterval = 1000000000 / FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;

		while (gameThread != null) {

			currentTime = System.nanoTime();

			delta += (currentTime - lastTime) / drawInterval;

			lastTime = currentTime;

			if (delta >= 1) {
				update();
				repaint();
				if (state == RUN) {
					timer++;
				}
				delta--;
			}

		}
	}

	public void update() {
		gui.update(mouseH);
		switch (state) {
		case (TRANSITION):
			if (!cameraTo.isEmpty()) {
				int nextPos = cameraTo.peek();
				if (this.cameraX != nextPos) {
					moveCamera(cameraFrom, nextPos, 180);
				} else {
					cameraInterval = 0;
					cameraFrom = cameraX;
					cameraTo.poll();
				}
			} else {
				gui.showStartText();
				state = RUN;
				entityManager.spawnMower();
				lastSmallWaveTime = timer;
			}
			break;
		case (RUN):
			entityManager.update(mouseH);
			levelManager.update();
			if (levelManager.isEmpty()) {
				if (entityManager.getZombies().size() == 0) {
					// TO-DO
					sun = 100;
					entityManager.resetAll();
					levelManager.advanceLevel();
				}
			} else if (timer - lastSmallWaveTime >= nextSpawn) {
				spawnZombie();
				lastSmallWaveTime = timer;
				nextSpawn = levelManager.getNextSpawn();
			}

			break;
		}

		mouseH.clicked = false;

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		gui.drawBackground(g2);

		entityManager.draw(g2, mouseH);

		gui.draw(g2);
		g2.dispose();
	}

	public void setGameState(int state) {
		this.state = state;
	}

	public void moveCamera(int start, int end, int step) {
		double progress = (double) cameraInterval / step;
		double easedProgress = Util.smoothstep(0.0, 1.0, progress);
		cameraX = (int) (start + (end - start) * easedProgress);
		cameraInterval++;
	}

	private void spawnZombie() {
		double[] probability = entityManager.getZombieProbability();
		int row = chooseRow(probability);
		entityManager.spawnEntity(BasicZombie.class, row, 9);
		levelManager.setZombieWeight(levelManager.getZombieWeight() + 1);
		progressBar.setProgress((double) levelManager.getZombieWeight() / (double) levelManager.getMaxZombieWeight());

	}

	private int chooseRow(double[] probabilities) {
		double randomValue = Math.random();

		double sum = 0;
		for (int i = 0; i < probabilities.length; i++) {
			sum += probabilities[i];
			if (randomValue <= sum) {
				return i;
			}
		}
		return -1;
	}

	public class LevelManager {
		private int currentLevel;
		private int zombiesPerLevel;
		private List<Integer> zombiesPerWave = new ArrayList<>();
		private List<Integer> waveTime = new ArrayList<>();
		private int maxZombieWeight;
		private int zombieWeight;
		List<Queue<Integer>> queues;
		int currentWave;
		double[] flags;

		public LevelManager(int level) {
			currentLevel = level;
			init();

		}

		public void update() {
			if (getSpawnQueue().isEmpty()) {
				if (currentWave % 2 == 0) {
					if (entityManager.getZombies().size() == 0) {
						currentWave++;
						nextSpawn = getNextSpawn();
						progressBar.updateFlags(Arrays.stream(flags, 0, Math.max(0, flags.length - 2 - currentWave / 2))
								.boxed().collect(Collectors.toList()));
					}
				}
			}
		}

		public void init() {
			currentWave = 0;
			zombieWeight = 0;
			try {
				readZombieWaves(currentLevel - 1);
			} catch (IOException e) {
				e.printStackTrace();
			}
			setSpawnQueue();
			
			maxZombieWeight = zombiesPerWave.stream().mapToInt(Integer::intValue).sum();
			flags = new double[zombiesPerWave.size() + 1];

			flags[0] = (double) (zombiesPerWave.get(zombiesPerWave.size() - 1) / 2);

			for (int i = 1; i < flags.length - 1; i++) {
				flags[i] = (double) (zombiesPerWave.get(zombiesPerWave.size() - i - 1) / 2)
						+ (double) (zombiesPerWave.get(zombiesPerWave.size() - i) / 2);
			}

			flags[flags.length - 1] = (double) (zombiesPerWave.get(0) / 2);

			double sum = Arrays.stream(flags).sum();

			Arrays.setAll(flags, i -> flags[i] / sum);

			for (int i = 1; i < flags.length; i++) {
				flags[i] = flags[i] + flags[i - 1];
			}
			progressBar.setProgress(0.0);
			progressBar.updateFlags(Arrays.stream(flags).boxed().collect(Collectors.toList()));
			nextSpawn = getNextSpawn();
		}

		public void advanceLevel() {
			currentLevel++;
			init();

			cameraTo.add(955);
			cameraTo.add(320);
			state = TRANSITION;

		}

		public void setSpawnQueue() {
			queues = new ArrayList<>();
			for (int i = 0; i < zombiesPerWave.size(); i++) {
				queues.add(Arrays.stream(generateArray(zombiesPerWave.get(i) / 2, waveTime.get(i) * 60)).boxed()
						.collect(Collectors.toCollection(LinkedList::new)));
				queues.add(Arrays.stream(generateArray(zombiesPerWave.get(i) / 2, 180)).boxed()
						.collect(Collectors.toCollection(LinkedList::new)));
			}
		}

		public int getMaxZombieWeight() {
			return maxZombieWeight;
		}

		public void setMaxZombieWeight(int weight) {
			maxZombieWeight = weight;
		}

		public int getZombieWeight() {
			return zombieWeight;
		}

		public void setZombieWeight(int weight) {
			zombieWeight = weight;
		}

		public int getNextSpawn() {
			if (getSpawnQueue().isEmpty()) {
				if (currentWave % 2 == 0 && entityManager.getZombies().size() != 0) {
					return 99999;
				} else {
					currentWave++;
				}
			}
			return getSpawnQueue().isEmpty() ? 99999 : getSpawnQueue().poll();
		}

		public Queue<Integer> getSpawnQueue() {
			if (currentWave >= queues.size()) {
				return new LinkedList<Integer>();
			}
			return queues.get(currentWave);
		}

		public boolean isEmpty() {
			return currentWave >= queues.size();
		}

		public int[] generateArray(int targetSize, int targetSum) {
			Random random = new Random();
			double[] array = new double[targetSize];
			double sum = 0;

			for (int i = 0; i < targetSize; i++) {
				double value = (1.0 / Math.pow(i + 10, 1));
				sum += value;
				array[i] = value;
			}

			double scale = (targetSum / 1.2) / sum;
			sum = 0;
			for (int i = 0; i < targetSize; i++) {
				array[i] = (int) (array[i] * scale);
				sum += array[i];
			}

			while (sum != targetSum) {
				int index = random.nextInt(targetSize);
				if (sum > targetSum && array[index] > 0) {
					array[index]--;
					sum--;
				} else if (sum < targetSum) {
					array[index]++;
					sum++;
				}
			}

			return Arrays.stream(array).mapToInt(i -> (int) i).toArray();
		}

		public void readZombieWaves(int index) throws IOException {

			
			Gson gson = new Gson();
			Type type = new TypeToken<List<Map<String, List<Integer>>>>() {}.getType();
			InputStream inputStream = ResourceManager.class.getClassLoader().getResourceAsStream("zombie_waves.json");
			if (inputStream != null) {
				try (InputStreamReader reader = new InputStreamReader(inputStream)) {

					List<Map<String, List<Integer>>> zombieWaves = gson.fromJson(reader, type);
					if (index >= 0 && index < zombieWaves.size()) {
						
					} else {
						index = 0;
					}
					Map<String, List<Integer>> element = zombieWaves.get(index);
					List<Integer> array1 = element.get("array1");
					List<Integer> array2 = element.get("array2");

					zombiesPerWave.clear();
					zombiesPerWave.addAll(array1);
					waveTime.clear();
					waveTime.addAll(array2);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
