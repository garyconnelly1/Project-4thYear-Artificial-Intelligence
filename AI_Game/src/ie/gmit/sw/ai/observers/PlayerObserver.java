package ie.gmit.sw.ai.observers;

import java.awt.event.KeyEvent;

import ie.gmit.sw.ai.Direction;
import ie.gmit.sw.ai.GameSetup;
import ie.gmit.sw.ai.Traversers.Traversator;
import ie.gmit.sw.ai.controllers.PlayerController;
import ie.gmit.sw.ai.gui.GameView;
import ie.gmit.sw.ai.observables.KeyPressed;
import ie.gmit.sw.ai.sprites.ControlledSprite;

public class PlayerObserver implements KeyObserver{
	
	public static final int MAZE_DIMENSION = 100;
	private GameView gamePanel;
	private GameSetup setup;
	private ControlledSprite player;

	
	
	
	public PlayerObserver(GameView gamePanel, GameSetup setup, ControlledSprite player) {
		
		System.out.println("PlayerObserver.");
		
		setGamePanel(gamePanel);
		setSetup(setup);
		setPlayer(player);
		KeyPressed keyPressed = KeyPressed.getInstance(); // Because we want to get a handle on the same KeyPressed listener from multiple classes.
		keyPressed.AddObserver(this);
		
	}
	
	@Override
	public void update(KeyEvent e) {
		this.player = setup.getPlayer();

		if (setup.getPlayer() == null || setup.getPlayer().isGameOver())
			return; // If the game is over, don't do anything.
		// Check here if the block is a bomb or weapon etc
		if (e.getKeyCode() == KeyEvent.VK_RIGHT && setup.getPlayer().getColPos() < MAZE_DIMENSION - 1) {
			if (PlayerController.isValidMove(setup, setup.getPlayer().getRowPos(), setup.getPlayer().getColPos() + 1)
					&& setup.getMaze()[setup.getPlayer().getRowPos()][setup.getPlayer().getColPos() + 1].isWalkable()) {
				// gamePanel.setPlayerState(6);
				this.player.setColPos(setup.getPlayer().getColPos() + 1);
				this.player.setSteps(setup.getPlayer().getSteps() + 1);
				this.player.setDirection(Direction.RIGHT);

			}
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT && setup.getPlayer().getColPos() > 0) {
			if (PlayerController.isValidMove(setup, setup.getPlayer().getRowPos(), setup.getPlayer().getColPos() - 1)
					&& setup.getMaze()[setup.getPlayer().getRowPos()][setup.getPlayer().getColPos() - 1].isWalkable()) {
				// gamePanel.setPlayerState(6);
				this.player.setColPos(setup.getPlayer().getColPos() - 1);
				this.player.setSteps(setup.getPlayer().getSteps() + 1);
				this.player.setDirection(Direction.LEFT);
			}
		} else if (e.getKeyCode() == KeyEvent.VK_UP && setup.getPlayer().getRowPos() > 0) {
			if (PlayerController.isValidMove(setup, setup.getPlayer().getRowPos() - 1, setup.getPlayer().getColPos())
					&& setup.getMaze()[setup.getPlayer().getRowPos() - 1][setup.getPlayer().getColPos()].isWalkable()) {
				// gamePanel.setPlayerState(6);
				this.player.setRowPos(setup.getPlayer().getRowPos() - 1);
				this.player.setSteps(setup.getPlayer().getSteps() + 1);
				this.player.setDirection(Direction.UP);
			}
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN && setup.getPlayer().getRowPos() < MAZE_DIMENSION - 1) {
			if (PlayerController.isValidMove(setup, setup.getPlayer().getRowPos() + 1, setup.getPlayer().getColPos())
					&& setup.getMaze()[setup.getPlayer().getRowPos() + 1][setup.getPlayer().getColPos()].isWalkable()) {
				// gamePanel.setPlayerState(6);
				this.player.setRowPos(setup.getPlayer().getRowPos() + 1);
				this.player.setSteps(setup.getPlayer().getSteps() + 1);
				this.player.setDirection(Direction.DOWN);
			}
		} else if (e.getKeyCode() == KeyEvent.VK_Z) {
			gamePanel.toggleZoom();
		} else if (e.getKeyCode() == KeyEvent.VK_S) {
			// If the player hasn't picked up any special pickup items then return
			// if (game.getPlayer().getSpecial() <= 0) return;
			if (!setup.getPlayer().getHelp())
				return;
			// Creates a new search algorithm object to find the goal node, randomly
			// Traversator traverse = randomSearch(new Random().nextInt((3 - 0) + 1) + 0);
			 //Traversator traverse = PlayerController.algorithm(setup, 3); // Try to make it do BestFirstSearch.
			// Traversator traverse = PlayerController.algorithm(setup, 4); // Try to make it do HillCliming.
			// Traversator traverse = PlayerController.algorithm(setup, 1); // Try to make it do Beam.
			// Traversator traverse = algorithm(2); // Try to make it do SimulatedAnnealing.
			// Traversator traverse = PlayerController.algorithm(setup, 5); // Try to make it do DLDFS.
			//Traversator traverse = PlayerController.algorithm(setup, 0); // Try to make it do A*.
			Traversator traverse = PlayerController.algorithm(setup); // Try to make it do IDA*.
			traverse.traverse(setup.getMaze(),
					setup.getMaze()[setup.getPlayer().getRowPos()][setup.getPlayer().getColPos()]);
			setup.getPlayer().setHelp(false);
		}
		updateView();
		
	} 
		
		
		
		
		
	private void updateView() {
		gamePanel.setCurrentRow(setup.getPlayer().getRowPos());
		gamePanel.setCurrentCol(setup.getPlayer().getColPos());
	}
	
	
	
	
	
	
	public GameView getGamePanel() {
		return gamePanel;
	}

	public void setGamePanel(GameView gamePanel) {
		this.gamePanel = gamePanel;
	}

	public GameSetup getSetup() {
		return setup;
	}

	public void setSetup(GameSetup setup) {
		this.setup = setup;
	}

	public ControlledSprite getPlayer() {
		return player;
	}

	public void setPlayer(ControlledSprite player) {
		this.player = player;
	}


}
