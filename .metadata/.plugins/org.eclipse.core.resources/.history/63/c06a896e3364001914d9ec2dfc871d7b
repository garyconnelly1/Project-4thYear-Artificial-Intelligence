package ie.gmit.sw.ai.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.Timer;

import ie.gmit.sw.ai.GameSetup;
import ie.gmit.sw.ai.Node;
import ie.gmit.sw.ai.sprites.Sprite;

public class GameView extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	public static final int DEFAULT_VIEW_SIZE = 800;
	private int cellspan;
	private int cellpadding;
	private int currentRow;
	private int currentCol;
	private boolean zoomOut;
	private int imageIndex;
	private int playerState;
	private int enemyState;
	private int enemyBossState;
	private Node[][] maze;
	private Timer timer;
	private Sprite[] sprites;

	public GameView() {
	}

	public GameView(GameSetup game, Sprite[] sprites) {
		this.sprites = sprites;
		this.maze = game.getMaze();
		this.imageIndex = -1;
		this.playerState = 5; // Edit this later to animate the player.
		this.enemyState = 7;
		this.enemyBossState = 18;
		this.cellspan = 5;
		this.cellpadding = 2;
		this.timer = new Timer(100, this);
		this.timer.start();
		setBackground(Color.LIGHT_GRAY);
		setDoubleBuffered(true);
	}

	public void setCurrentRow(int row) {
		if (row < cellpadding) {
			currentRow = cellpadding;
		} else if (row > (maze.length - 1) - cellpadding) {
			currentRow = (maze.length - 1) - cellpadding;
		} else {
			currentRow = row;
		}
	}

	public void setCurrentCol(int col) {
		if (col < cellpadding) {
			currentCol = cellpadding;
		} else if (col > (maze.length - 1) - cellpadding) {
			currentCol = (maze.length - 1) - cellpadding;
		} else {
			currentCol = col;
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		cellspan = zoomOut ? maze.length : 5;
		final int size = DEFAULT_VIEW_SIZE / cellspan;
		g2.drawRect(0, 0, GameView.DEFAULT_VIEW_SIZE, GameView.DEFAULT_VIEW_SIZE);

		for (int row = 0; row < cellspan; row++) {
			for (int col = 0; col < cellspan; col++) {
				int x1 = col * size;
				int y1 = row * size;

				char ch = '0';
				ch = maze[row][col].getNodeType();
				if (zoomOut) {

					if (ch == 'S') { // Normal Spider.
						g2.setColor(Color.RED);
						g2.fillRect(x1, y1, size, size);
					}

					if (ch == 'A') { // A* Spider.
						g2.setColor(Color.BLACK);
						g2.fillRect(x1, y1, size, size);
					}
					if (ch == 'D') { // Dead Spider.
						g2.setColor(Color.BLUE);
						g2.fillRect(x1, y1, size, size);
					}
					if (ch == 'Q') { // Quickest Path.
						g2.setColor(Color.YELLOW);
						g2.fillRect(x1, y1, size, size);
					}
					if (row == currentRow && col == currentCol) {
						g2.setColor(Color.MAGENTA); // Maybe change this color.
						g2.fillRect(x1, y1, size, size);
					}
				} else {
					ch = maze[currentRow - cellpadding + row][currentCol - cellpadding + col].getNodeType();
				}

				switch (ch) {

				case '0': // Hedge.
					imageIndex = 0;
					break;

				case 'K': // Sword(K for knife.)
					imageIndex = 1;
					break;
				case '?': // Help.
					imageIndex = 2;
					break;
				case 'B': // Bomb.
					imageIndex = 3;
					break;
				case 'H': // Hydrogen bomb.
					imageIndex = 4;
					break;
				case 'S': // Normal Spider.
					imageIndex = enemyState;
					break;
				case 'A': // A* Spider.
					imageIndex = enemyBossState;
					break;
				case 'D': // Dead Spider.
					imageIndex = 9;
					break;
				case 'Q': // Quickest Path.
					imageIndex = 10;
					break;
				case 'F': // Food.
					imageIndex = 11;
					break;
				case 'M': // Metal Shield.
					imageIndex = 12;
					break;
				case 'P': // Player.
					imageIndex = playerState;
					break;
				case 'G': // Goal.
					imageIndex = 13;
					break;
				case 'C': // Chest.
					imageIndex = 14;
					break;
				case 'L': // Lose.
					imageIndex = 15;
					break;
				case 'O': // Player.
					imageIndex = 16;
					break;
				case 'N': // Bomb.
					imageIndex = 17;
					break;
				default:
					imageIndex = -1;
					break;
				}

				if (imageIndex >= 0) {
					g2.drawImage(sprites[imageIndex].getNext(), x1, y1, null);
				} else {
					if (maze[row][col].getNodeType() == ' ') { // Print blank space grey.
						g2.setColor(Color.LIGHT_GRAY);
						g2.fillRect(x1, y1, size, size);
					}
				}

				if (maze[row][col].getNodeType() == 'Q') {
					g2.setColor(maze[row][col].getColor());
					g2.fillRect(x1, y1, size, size);
				}

				if (maze[row][col].isGoalNode() && maze[row][col].getNodeType() != 'P') {
					g2.setColor(Color.WHITE);
					g2.fillRect(x1, y1, size, size);
				}

			}
		}
	}

	public void toggleZoom() {
		zoomOut = !zoomOut;
	}

	public void actionPerformed(ActionEvent e) {

		if (enemyState < 0 || enemyState == 7) {
			enemyState = 8;
		} else {
			enemyState = 7;
		}

		if (enemyBossState < 0 || enemyBossState == 18) {
			enemyBossState = 19;
		} else {
			enemyBossState = 18;
		}

		if (e.getSource() == timer) {
			repaint();
		}

	}

	public int getPlayerState() {
		return playerState;
	}

	public void setPlayerState(int playerState) {
		this.playerState = playerState;
	}
}
