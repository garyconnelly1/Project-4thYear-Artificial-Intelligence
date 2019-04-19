package ie.gmit.sw.ai;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ie.gmit.sw.ai.Traversers.AStarTraversator;
import ie.gmit.sw.ai.Traversers.BasicHillClimbingTraversator;
import ie.gmit.sw.ai.Traversers.BeamTraversator;
import ie.gmit.sw.ai.Traversers.BestFirstTraversator;
import ie.gmit.sw.ai.Traversers.DepthLimitedDFSTraversator;
import ie.gmit.sw.ai.Traversers.Traversator;
import ie.gmit.sw.ai.nn.Neural;

public class GameRunner implements KeyListener {
	public static final int MAZE_DIMENSION = 100;
	public static final int IMAGE_COUNT = 20;
	private ControlledSprite player;
	private JFrame gameFrame;
	
	

	// The game controller object instance & game view panel
	private GameView gamePanel;
	private GameSetup setup;
	private Sprite[] sprites;

	public GameRunner() throws Exception {

		newFrame();
		//initNewGame("Hard"); //////////////////////////////////////////
		initNewGame("Normal"); //////////////////////////////////////////
		gameFrame.repaint();

		
	}

	// Abstract the different parts of building the view.
	private void newFrame() {
		gameFrame = new JFrame("GMIT - B.Sc. in Computing (Software Development) - Gary Connelly");
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameFrame.addKeyListener(this);
		gameFrame.getContentPane().setLayout(null);
		gameFrame.getContentPane().setBackground(Color.black);
		gameFrame.setSize(800,800);
		gameFrame.setLocation(0,0);
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
								"resources/images/player/d2.png", "resources/images/player/d3.png", "resources/images/player/l1.png",
								"resources/images/player/l2.png", "resources/images/player/l3.png", "resources/images/player/r1.png",
						"resources/images/player/r2.png", "resources/images/player/r3.png");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
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
		gamePanel.setSize(800,800);
		gamePanel.setLocation(0,0);
		gamePanel.setPreferredSize(d);
		gamePanel.setMinimumSize(d);
		gamePanel.setMaximumSize(d);
		gameFrame.getContentPane().add(gamePanel);

		updateView();
	}

	private void updateView() {
		gamePanel.setCurrentRow(setup.getPlayer().getRowPos());
		gamePanel.setCurrentCol(setup.getPlayer().getColPos());
	}

	public void keyPressed(KeyEvent e) { // Try to abstract this to the player.
		this.player = setup.getPlayer();
		
		if (setup.getPlayer() == null || setup.getPlayer().isGameOver()) return; // If the game is over, don't do anything.
		// Check here if the block is a bomb or weapon etc
				if (e.getKeyCode() == KeyEvent.VK_RIGHT && setup.getPlayer().getColPos() < MAZE_DIMENSION - 1) {
					if (isValidMove(setup.getPlayer().getRowPos(), setup.getPlayer().getColPos() + 1) && setup.getMaze()[setup.getPlayer().getRowPos()][setup.getPlayer().getColPos() + 1].isWalkable()) {
						//gamePanel.setPlayerState(6);
						this.player.setColPos(setup.getPlayer().getColPos() + 1);
						this.player.setSteps(setup.getPlayer().getSteps() + 1);
						this.player.setDirection(Direction.RIGHT);
						
					}
				} else if (e.getKeyCode() == KeyEvent.VK_LEFT && setup.getPlayer().getColPos() > 0) {
					if (isValidMove(setup.getPlayer().getRowPos(), setup.getPlayer().getColPos() - 1) && setup.getMaze()[setup.getPlayer().getRowPos()][setup.getPlayer().getColPos() - 1].isWalkable()) {
						//gamePanel.setPlayerState(6);
						this.player.setColPos(setup.getPlayer().getColPos() - 1);
						this.player.setSteps(setup.getPlayer().getSteps() + 1);
						this.player.setDirection(Direction.LEFT);
					}
				} else if (e.getKeyCode() == KeyEvent.VK_UP && setup.getPlayer().getRowPos() > 0) {
					if (isValidMove(setup.getPlayer().getRowPos() - 1, setup.getPlayer().getColPos()) && setup.getMaze()[setup.getPlayer().getRowPos() - 1][setup.getPlayer().getColPos()].isWalkable()) {
					//	gamePanel.setPlayerState(6);
						this.player.setRowPos(setup.getPlayer().getRowPos() - 1);
						this.player.setSteps(setup.getPlayer().getSteps() + 1);
						this.player.setDirection(Direction.UP);
					}
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN && setup.getPlayer().getRowPos() < MAZE_DIMENSION - 1) {
					if (isValidMove(setup.getPlayer().getRowPos() + 1, setup.getPlayer().getColPos()) && setup.getMaze()[setup.getPlayer().getRowPos() + 1][setup.getPlayer().getColPos()].isWalkable()) {
					//	gamePanel.setPlayerState(6);
						this.player.setRowPos(setup.getPlayer().getRowPos() + 1);
						this.player.setSteps(setup.getPlayer().getSteps() + 1);
						this.player.setDirection(Direction.DOWN);
					}
				} else if (e.getKeyCode() == KeyEvent.VK_Z) {
					gamePanel.toggleZoom();
				} else if (e.getKeyCode() == KeyEvent.VK_S) {
					// If the player hasn't picked up any special pickup items then return
					//if (game.getPlayer().getSpecial() <= 0) return;
					if (!setup.getPlayer().getHelp()) return;
					// Creates a new search algorithm object to find the goal node, randomly
					//Traversator traverse = randomSearch(new Random().nextInt((3 - 0) + 1) + 0);
					//Traversator traverse = algorithm(3); // Try to make it do BestFirstSearch.
					//Traversator traverse = algorithm(4); // Try to make it do HillCliming.
					//Traversator traverse = algorithm(1); // Try to make it do Beam.
					//Traversator traverse = algorithm(2); // Try to make it do SimulatedAnnealing.
					//Traversator traverse = algorithm(5); // Try to make it do DLDFS.
					Traversator traverse = algorithm(0); // Try to make it do A*.
					traverse.traverse(setup.getMaze(), setup.getMaze()[setup.getPlayer().getRowPos()][setup.getPlayer().getColPos()]);
					setup.getPlayer().setHelp(false);
		}
		updateView();
	}

	public void keyReleased(KeyEvent e) {
	} // Ignore

	public void keyTyped(KeyEvent e) {
	} // Ignore

	private boolean isValidMove(int r, int c) {
		
		
		Node[][] maze;// = new Node[][];
		maze = setup.getMaze();
		if (!(r <= setup.getMaze().length - 1 && c <= setup.getMaze()[r].length - 1)) return false;

		switch (maze[r][c].getNodeType()) {
			case ' ':
			
				maze[setup.getPlayer().getRowPos()][setup.getPlayer().getColPos()].setNodeType(' ');
				
				maze[setup.getPlayer().getRowPos()][setup.getPlayer().getColPos()].setGoalNode(false);
				maze[r][c].setNodeType('P');
				maze[r][c].setGoalNode(true);
				return true;
			case 'K':
			
				if (!(setup.getPlayer().getWeaponStrength() >= 45)) {
					setup.getPlayer().setWeaponStrength(45);
					System.out.println("--- Player Stats ---");
					System.out.println("Player Health: " + setup.getPlayer().getHealth());
					System.out.println("Player Strength: " + setup.getPlayer().getWeaponStrength());
					System.out.println("Player Defence: " + setup.getPlayer().getDefence());
					System.out.println("");
					setup.getMaze()[r][c].setNodeType('0');
				}
				return true;
			case '?':
				setup.getPlayer().setHelp(true);
				maze[r][c].setNodeType('0');
				return true;
			case 'B':
			
				if (!(setup.getPlayer().getWeaponStrength() >= 75)) {
					setup.getPlayer().setWeaponStrength(75);
					System.out.println("--- Player Stats ---");
					System.out.println("Player Health: " + setup.getPlayer().getHealth());
					System.out.println("Player Strength: " + setup.getPlayer().getWeaponStrength());
					System.out.println("Player Defence: " + setup.getPlayer().getDefence());
					System.out.println("-------------------");
					maze[r][c].setNodeType('0');
					
				}
				return true;
			case 'H':
				if (!(setup.getPlayer().getWeaponStrength() >= 100)) {
					setup.getPlayer().setWeaponStrength(100);
					System.out.println("--- Player Stats ---");
					System.out.println("Player Health: " + setup.getPlayer().getHealth());
					System.out.println("Player Strength: " + setup.getPlayer().getWeaponStrength());
					System.out.println("Player Defence: " + setup.getPlayer().getDefence());
					System.out.println("-------------------");
					maze[r][c].setNodeType('0');
					
				}
				return true;
			case 'F':
				// Health pick up, adds 50 health to players character
				if (setup.getPlayer().getHealth() < 100) {
					setup.getPlayer().setHealth(setup.getPlayer().getHealth() + 50);
					if (setup.getPlayer().getHealth() > 100)
						setup.getPlayer().setHealth(100);
					maze[r][c].setNodeType('0');
					System.out.println("--- Player Stats ---");
					System.out.println("Player Health: " + setup.getPlayer().getHealth());
					System.out.println("Player Strength: " + setup.getPlayer().getWeaponStrength());
					System.out.println("Player Defence: " + setup.getPlayer().getDefence());
					System.out.println("-------------------");
					
					
				}
				return true;
			case 'M':
				// Health pick up, adds 50 defence to players character
				if (setup.getPlayer().getDefence() < 100) {
					setup.getPlayer().setDefence(setup.getPlayer().getDefence() + 25);
					if (setup.getPlayer().getDefence() > 100)
						setup.getPlayer().setDefence(100);
					maze[r][c].setNodeType('0');
					System.out.println("--- Player Stats ---");
					System.out.println("Player Health: " + setup.getPlayer().getHealth());
					System.out.println("Player Strength: " + setup.getPlayer().getWeaponStrength());
					System.out.println("Player Defence: " + setup.getPlayer().getDefence());
					System.out.println("-------------------");
					
				}
				return true;
			case 'Q':
				maze[setup.getPlayer().getRowPos()][setup.getPlayer().getColPos()].setNodeType(' ');
				maze[setup.getPlayer().getRowPos()][setup.getPlayer().getColPos()].setGoalNode(false);
				maze[r][c].setNodeType('P');
				maze[r][c].setGoalNode(true);
				return true;
			case 'G':
				maze[setup.getPlayer().getRowPos()][setup.getPlayer().getColPos()].setNodeType(' ');
				maze[setup.getPlayer().getRowPos()][setup.getPlayer().getColPos()].setGoalNode(false);
				maze[r][c].setNodeType('C');
				setup.getPlayer().setGameOver(true);
				
				return true;
			
			case 'S':
				
				return fight(maze, r, c);
			
			case 'A':
				
				return fight(maze, r, c);
				
			default:
				return false;
				
				
}
	}
	
	public boolean fight(Node[][] maze, int r, int c ) {
		FuzzyFight fuzzyBattle1 = new FuzzyFight();
		boolean enemyWon1 = fuzzyBattle1.startBattle(setup.getPlayer(), setup.getEnemies().get(setup.getMaze()[r][c].getEnemyID()), "fcl/fuzzyFight.fcl");
		if (enemyWon1 == true) {
			// The player has lost the game!
			maze[setup.getPlayer().getRowPos()][setup.getPlayer().getColPos()].setNodeType(' ');
			//game.getMaze()[game.getPlayer().getRowPos()][game.getPlayer().getColPos()].setNodeType(NodeType.ROAD);
			maze[setup.getPlayer().getRowPos()][setup.getPlayer().getColPos()].setEnemyID(0);
			setup.getPlayer().setGameOver(true);
			maze[r][c].setNodeType('L');
			//game.getMaze()[r][c].setNodeType(NodeType.LOSE);
			
			return false;
		} else {
			setup.getEnemies().get(setup.getMaze()[r][c].getEnemyID()).setHealth(0);
			maze[r][c].setNodeType('D');
			//game.getMaze()[r][c].setNodeType(NodeType.DEADSPIDER);
			maze[r][c].setEnemyID(0);
			
			return false;
		}
		
	}
	
	
	
	private Traversator algorithm(int randNum) {
		// Selecting a random algorithm to be created and returned
		switch (randNum) {
			case 0:
				return new AStarTraversator(setup.getModel().getGoalNode(), false);
			case 1:
				return new BeamTraversator(setup.getModel().getGoalNode(), 10);
			case 2:
			//	return new SimulatedAnnealingTraversator(game.getModel().getGoalNode());
			case 3:
				return new BestFirstTraversator(setup.getModel().getGoalNode());
			case 4:
				return new BasicHillClimbingTraversator(setup.getModel().getGoalNode());
			case 5:
				//return new DepthLimitedDFSTraversator(game.getMaze().length);
				return new DepthLimitedDFSTraversator(5); // Only works sometimes.
			case 6:
				//return new IDAStarTraversator(game.getModel().getGoalNode());
			case 7:
				//return new IDDFSTraversator();
			default:
				return new AStarTraversator(setup.getModel().getGoalNode(), false);
		}
}


	public static void main(String[] args) throws Exception {
		
		Neural.trainNetwork();
		new GameRunner();
	}
}

