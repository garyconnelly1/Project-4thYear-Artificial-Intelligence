package ie.gmit.sw.ai;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;

import ie.gmit.sw.ai.nn.Neural;
import ie.gmit.sw.ai.observables.KeyPressed;
import ie.gmit.sw.ai.observers.PlayerObserver;

public class GameRunner {//implements KeyListener {
	public static final int MAZE_DIMENSION = 100;
	public static final int IMAGE_COUNT = 20;
	private ControlledSprite player;
	private JFrame gameFrame;

	// The game controller object instance & game view panel
	private GameView gamePanel;
	private GameSetup setup;
	private Sprite[] sprites;

	public GameRunner() throws Exception {
		gameMenu();
		newFrame();
		// initNewGame("Hard"); //////////////////////////////////////////
		initNewGame("Normal"); //////////////////////////////////////////
		gameFrame.repaint();

	}
	
	public void gameMenu() {
		Scanner console = new Scanner(System.in);
		int exitStrategy = 0;
		System.out.println("Enter Your Exit Strategy: ");
		System.out.println("---------------------------");
		System.out.println("Press 1 --> A Star Algorithm.");
		System.out.println("Press 2 --> Beam Search Algorithm.");
		System.out.println("Press 3 --> Best First Search  Algorithm.");
		System.out.println("Press 4 --> Iterative Deepening A Star Algorithm.");
		System.out.println("Press 5 ---> Basic Hill Climbing Algorithm.");
		exitStrategy = console.nextInt();
		console.close();
		
		if(exitStrategy < 1 || exitStrategy > 5) {
			exitStrategy = 0;
		}else{
			PlayerController.setExitAlgorithm(exitStrategy -1);
		}
		
		
	}

	// Abstract the different parts of building the view.
	private void newFrame() {
		gameFrame = new JFrame("GMIT - B.Sc. in Computing (Software Development) - Gary Connelly");
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//gameFrame.addKeyListener(this);
		gameFrame.addKeyListener(KeyPressed.getInstance());
		gameFrame.getContentPane().setLayout(null);
		gameFrame.getContentPane().setBackground(Color.black);
		gameFrame.setSize(800, 800);
		gameFrame.setLocation(0, 0);
		gameFrame.setVisible(true);
	}

	public void initNewGame(String gameDifficulty) {
		System.out.println("Start.");
		if (setup != null)
			setup.killEnemyThreads();

		if (gamePanel != null)
			gameFrame.getContentPane().remove(gamePanel);

		setup = new GameSetup();

		newGame(gameDifficulty);

		gameFrame.repaint();

	}

	private void newGame(String gameDifficulty) {

		Maze model = new Maze(MAZE_DIMENSION);
		Node[][] maze = model.getMaze(); // The already build maze.
		try {
			this.player = new ControlledSprite(100, 100, "The Player", 3, "resources/images/player/d1.png",
					"resources/images/player/d2.png", "resources/images/player/d3.png",
					"resources/images/player/l1.png", "resources/images/player/l2.png",
					"resources/images/player/l3.png", "resources/images/player/r1.png",
					"resources/images/player/r2.png", "resources/images/player/r3.png");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		ArrayList<Spider> spiders = new ArrayList<Spider>();
		Dimension d = new Dimension(800, 800);
		System.out.println("Created Player.");
		setup.setModel(model);
		setup.setMaze(maze);
		setup.setPlayer(player);
		setup.setEnemies(spiders);
		setup.placePlayer(model.getGoalPos());
		setup.placeEnemies(gameDifficulty);

		try {
			sprites = setup.getSprites();
		} catch (Exception e) {
			System.out.println("Error reading images.");
			e.printStackTrace();
		}
		gamePanel = new GameView(setup, sprites); // Pass sprites in here.
		gamePanel.setSize(800, 800);
		gamePanel.setLocation(0, 0);
		gamePanel.setPreferredSize(d);
		gamePanel.setMinimumSize(d);
		gamePanel.setMaximumSize(d);
		gameFrame.getContentPane().add(gamePanel);

		updateView();
		
		new PlayerObserver(gamePanel, setup, setup.getPlayer());
	}

	private void updateView() {
		gamePanel.setCurrentRow(setup.getPlayer().getRowPos());
		gamePanel.setCurrentCol(setup.getPlayer().getColPos());
	}


	public void keyReleased(KeyEvent e) {
	} // Ignore

	public void keyTyped(KeyEvent e) {
	} // Ignore


	public static void main(String[] args) throws Exception {

		Neural.trainNetwork();
		new GameRunner();
	}
}
