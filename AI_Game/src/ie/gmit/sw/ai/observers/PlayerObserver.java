package ie.gmit.sw.ai.observers;

import java.awt.event.KeyEvent;

import ie.gmit.sw.ai.Direction;
import ie.gmit.sw.ai.GameSetup;
import ie.gmit.sw.ai.Traversers.Traversator;
import ie.gmit.sw.ai.controllers.PlayerController;
import ie.gmit.sw.ai.gui.GameView;
import ie.gmit.sw.ai.observables.KeyPressed;
import ie.gmit.sw.ai.sprites.ControlledSprite;

/*
* Provides the implementation of the KeyObserver interface and updates Player objects based on 
* key events.
*/

public class PlayerObserver implements KeyObserver {

	public static final int MAZE_DIMENSION = 100;
	private GameView gamePanel;
	private GameSetup setup;
	private ControlledSprite player;

	public PlayerObserver(GameView gamePanel, GameSetup setup, ControlledSprite player) {
		setGamePanel(gamePanel);
		setSetup(setup);
		setPlayer(player);
		KeyPressed keyPressed = KeyPressed.getInstance(); 
		keyPressed.AddObserver(this);

	}

	@Override
	public void update(KeyEvent e) { // Whenever a key is pressed.
		this.player = setup.getPlayer();

		if (setup.getPlayer() == null || setup.getPlayer().isGameOver())
			return; // If the game is over, don't do anything.
		// Check here if the block is a bomb or weapon etc.
		
		/*
		 * If the right key is pressed, move the player to the right and change the direction of
		 * the player sprite.
		 */
		if (e.getKeyCode() == KeyEvent.VK_RIGHT && setup.getPlayer().getColPos() < MAZE_DIMENSION - 1) {
			if (PlayerController.isValidMove(setup, setup.getPlayer().getRowPos(), setup.getPlayer().getColPos() + 1)
					&& setup.getMaze()[setup.getPlayer().getRowPos()][setup.getPlayer().getColPos() + 1].isWalkable()) {
				this.player.setColPos(setup.getPlayer().getColPos() + 1);
				this.player.setSteps(setup.getPlayer().getSteps() + 1);
				this.player.setDirection(Direction.RIGHT);

			}
			/*
			 * If the left key is pressed, move the player to the left and change the direction of
			 * the player sprite.
			 */
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT && setup.getPlayer().getColPos() > 0) {
			if (PlayerController.isValidMove(setup, setup.getPlayer().getRowPos(), setup.getPlayer().getColPos() - 1)
					&& setup.getMaze()[setup.getPlayer().getRowPos()][setup.getPlayer().getColPos() - 1].isWalkable()) {
				this.player.setColPos(setup.getPlayer().getColPos() - 1);
				this.player.setSteps(setup.getPlayer().getSteps() + 1);
				this.player.setDirection(Direction.LEFT);
			}
			/*
			 * If the up key is pressed, move the player upwards and change the direction of
			 * the player sprite.
			 */
		} else if (e.getKeyCode() == KeyEvent.VK_UP && setup.getPlayer().getRowPos() > 0) {
			if (PlayerController.isValidMove(setup, setup.getPlayer().getRowPos() - 1, setup.getPlayer().getColPos())
					&& setup.getMaze()[setup.getPlayer().getRowPos() - 1][setup.getPlayer().getColPos()].isWalkable()) {
				this.player.setRowPos(setup.getPlayer().getRowPos() - 1);
				this.player.setSteps(setup.getPlayer().getSteps() + 1);
				this.player.setDirection(Direction.UP);
			}
			/*
			 * If the down key is pressed, move the player downwards and change the direction of
			 * the player sprite.
			 */
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN && setup.getPlayer().getRowPos() < MAZE_DIMENSION - 1) {
			if (PlayerController.isValidMove(setup, setup.getPlayer().getRowPos() + 1, setup.getPlayer().getColPos())
					&& setup.getMaze()[setup.getPlayer().getRowPos() + 1][setup.getPlayer().getColPos()].isWalkable()) {
				this.player.setRowPos(setup.getPlayer().getRowPos() + 1);
				this.player.setSteps(setup.getPlayer().getSteps() + 1);
				this.player.setDirection(Direction.DOWN);
			}
			/*
			 * If the z key is pressed, toggle the zoom.
			 */
		} else if (e.getKeyCode() == KeyEvent.VK_Z) {
			gamePanel.toggleZoom();
			/*
			 * If the s key is pressed, allow the player to search for the exit node.
			 */
		} else if (e.getKeyCode() == KeyEvent.VK_S) {
			// If the player hasn't picked up any special pickup items then return.
			if (!setup.getPlayer().getHelp())
				return;
			Traversator traverse = PlayerController.algorithm(setup); // Gets what ever traversal algorithm the user
																		// selected.
			traverse.traverse(setup.getMaze(),
					setup.getMaze()[setup.getPlayer().getRowPos()][setup.getPlayer().getColPos()]);
			setup.getPlayer().setHelp(false);
		}
		updateView();

	}

	/*
	 * Update the view accordingly.
	 */
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
