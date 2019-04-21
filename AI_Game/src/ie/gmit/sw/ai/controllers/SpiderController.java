package ie.gmit.sw.ai.controllers;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

import ie.gmit.sw.ai.Node;
import ie.gmit.sw.ai.Traversers.AStarTraversator;
import ie.gmit.sw.ai.fuzzy.FuzzyFight;
import ie.gmit.sw.ai.nn.Neural;
import ie.gmit.sw.ai.sprites.Spider;

public class SpiderController {
	/*
	 * Controls the spider sprite's behaviour.
	 */

	private static boolean isValidMove(Spider thread, int row, int col) {
		
		/*
		 * If the game is over, don't let the sprites move.
		 */
		if (thread.getPlayer().isGameOver())
			return false;
		/*
		 * If the spider is at an edge, don't let them move.
		 */
		if ((row < 0) || (col < 0) || !(row <= thread.getMaze().length - 1 && col <= thread.getMaze()[row].length - 1))
			return false;

		switch (thread.getMaze()[row][col].getNodeType()) {
		case ' ': /* If it is a road node, move the spider into it and repaint the spiders original
					 node as a road node.
				  */
			thread.getMaze()[thread.getRowPos()][thread.getColPos()].setNodeType(' '); 
			thread.getMaze()[row][col].setNodeType('S');
			if (thread.isAstar())
				thread.getMaze()[row][col].setNodeType('A');
			thread.getMaze()[row][col]
					.setEnemyID(thread.getMaze()[thread.getRowPos()][thread.getColPos()].getEnemyID());
			thread.getMaze()[thread.getRowPos()][thread.getColPos()].setEnemyID(0);
			return true;
		case 'P': /* If it is the player node.
				  */
			if (thread.isAstar()) {
				// If the spider is an A* spider, fight.
				return fight(thread, row, col);

			} else {
				// Of it is a regular spider, classify what to do next.
				return classify(thread, row, col);

			}

		case 'Q': /* If it is a quickest path(for the player) node, allow the spider to walk into it.
				  */
			thread.getMaze()[thread.getRowPos()][thread.getColPos()].setNodeType(' ');
			if (thread.isAstar())
				thread.getMaze()[row][col].setNodeType('A');
			thread.getMaze()[row][col]
					.setEnemyID(thread.getMaze()[thread.getRowPos()][thread.getColPos()].getEnemyID());
			thread.getMaze()[thread.getRowPos()][thread.getColPos()].setEnemyID(0);
			return true;
		default:
			return false;
		}

	}

	/*
	 * The method that encapsulates the fuzzy logic fight implementation from the spiders point of view.
	 */
	public static boolean fight(Spider thread, int row, int col) {
		FuzzyFight fuzzyFight = new FuzzyFight();
		boolean enemyWon = fuzzyFight.startBattle(thread.getPlayer(), thread, "resources/fuzzy/fcl/fuzzyFight.fcl");
		if (enemyWon == true) {
			// The player has lost the fight, set game over to true and paint the 'RIP' image in the players place.
			thread.getMaze()[thread.getRowPos()][thread.getColPos()].setNodeType(' ');
			thread.getMaze()[thread.getRowPos()][thread.getColPos()].setEnemyID(0);
			thread.getPlayer().setGameOver(true);
			thread.getMaze()[row][col].setNodeType('L');

		} else {
			// The player won the fight, paint the blue(dead) spider in it's place. 
			thread.getMaze()[thread.getRowPos()][thread.getColPos()].setNodeType('D');
			if (thread.isAstar())
				thread.getMaze()[thread.getRowPos()][thread.getColPos()].setNodeType('D');
			thread.getMaze()[thread.getRowPos()][thread.getColPos()].setEnemyID(0);
			thread.setHealth(0);

		}
		return enemyWon;
	}

	/*
	 * Method encapsulates the neural network implementation.
	 */
	public static boolean classify(Spider thread, int row, int col) {

		int outcome = 5; // Default it to run.

		try {
			// Pass in the players attributes as the inputs to the neural netowkr.
			outcome = Neural.process(thread.getPlayer().getHealth(), thread.getPlayer().getWeaponStrength(),
					thread.getPlayer().getDefence());
		} catch (Exception e) {
			System.out.println("Error processing player data.");
			e.printStackTrace();
		}

		switch (outcome) {
		case 1: // The spider freezes on the spot.(Panic.)

			try {
				Thread.sleep(10000); // Freeze the thread from moving for 10 seconds.

				return false;
			} catch (InterruptedException e) {
				System.out.println("Thread error.");
				e.printStackTrace();
			}
			break;
		case 2: // Attack the player.
			return fight(thread, row, col);
		case 3: // Teleport away from the player.(Hide.)
			hide(thread);
			return false;
		case 4:
			return false; // Just move the spider to a new node, gives the illusion the spider is running away.
		default: // The default is to run away.
			return false;
		}
		return false;
	}

	/*
	 * Allows A* spiders to use the A* algorithm to find the player.
	 */
	public static void huntPlayer(Spider thread) {
		
		/*
		 * If the player has moved since the last computation, recompute the path to the player's location.
		 */
		if (thread.getPlayerRowT() != thread.getPlayer().getRowPos()
				|| thread.getPlayerColT() != thread.getPlayer().getColPos()) { 
			
			thread.setNodeListPath(new LinkedList<Node>());
			thread.setPlayerRowT(thread.getPlayer().getRowPos()); // The players current row.
			thread.setPlayerColT(thread.getPlayer().getColPos()); // The players current col.
			thread.setTraverse(new AStarTraversator(
					thread.getMaze()[thread.getPlayer().getRowPos()][thread.getPlayer().getColPos()], false, true));// Here
			thread.getTraverse().traverse(thread.getMaze(), thread.getMaze()[thread.getRowPos()][thread.getColPos()]); // Current
																														// maze,
																														// current
																														// node.
			// If the player node is found.
			if (thread.getTraverse().isFoundGoal()) {
				/*
				 * Get the path back to the current node by getting the parent of each node.
				 */
				thread.setPathGoal(thread.getTraverse().getPathGoal());
				while (thread.getPathGoal() != null) {
					thread.getNodeListPath().add(thread.getPathGoal());
					thread.setPathGoal(thread.getPathGoal().getParent());
				}
				/*
				 * Reverse the path so it becomes the path from the current node to the player node.
				 */
				Collections.reverse(thread.getNodeListPath());
				thread.getNodeListPath().removeFirst(); // Remove itself from the list.
			}
		}

		// If the path has been found then start moving the enemy towards the player.
		if (thread.getTraverse().isFoundGoal()) {

			Node nextPath = thread.getNodeListPath().poll();
			boolean foundNextPath = false;

			makeMove(thread, nextPath, foundNextPath);
		}
	} // End hunt player.

	/*
	 * If it is not an A* spider, randomly move the spider through the maze.
	 */
	public static void randomWalk(Spider thread, int direction) {
		if (thread.getHealth() <= 0)
			return;
		// Moving the enemy object to a new position in the maze.
		switch (direction) {
		// Going up in the maze
		case 0:
			if (isValidMove(thread, thread.getRowPos() - 1, thread.getColPos())) {
				thread.setRowPos(thread.getRowPos() - 1);
			}
			break;
		// Going down in the maze.
		case 1:
			if (isValidMove(thread, thread.getRowPos() + 1, thread.getColPos())) {
				thread.setRowPos(thread.getRowPos() + 1);
			}
			break;
		// Going left in the maze.
		case 2:
			if (isValidMove(thread, thread.getRowPos(), thread.getColPos() - 1)) {
				thread.setColPos(thread.getColPos() - 1);
			}
			break;
		// Going right in the maze.
		case 3:
			if (isValidMove(thread, thread.getRowPos(), thread.getColPos() + 1)) {
				thread.setColPos(thread.getColPos() + 1);
			}
			break;
		default:
			if (isValidMove(thread, thread.getRowPos() + 1, thread.getColPos())) {
				thread.setRowPos(thread.getRowPos() + 1);
			}
			break;
		}
	}

	/*
	 * Teleport the spider to a new location in the maze to provide the illusion that the spider is hiding
	 * on the player.
	 */
	public static void hide(Spider thread) { 
		if (isValidMove(thread, thread.getRowPos() + 10, thread.getColPos() + 10)) {
			thread.setRowPos(thread.getRowPos() + 10);
			thread.setColPos((thread.getColPos() + 10));
		} else if (isValidMove(thread, thread.getRowPos() - 10, thread.getColPos() + 10)) {
			thread.setRowPos(thread.getRowPos() - 10);
			thread.setColPos((thread.getColPos() + 10));
		} else if (isValidMove(thread, thread.getRowPos() + 10, thread.getColPos() - 10)) {
			thread.setRowPos(thread.getRowPos() + 10);
			thread.setColPos((thread.getColPos() - 10));
		} else if (isValidMove(thread, thread.getRowPos() - 10, thread.getColPos() - 10)) {
			thread.setRowPos(thread.getRowPos() - 10);
			thread.setColPos((thread.getColPos() - 10));
		} else {
			randomWalk(thread, new Random().nextInt((3 - 0) + 1) + 0);
		}
	}

	
	/*
	 * Move the A* spider throughout the maze.
	 */
	public static void makeMove(Spider thread, Node nextPath, boolean foundNextPath) {

		while (nextPath != null && !foundNextPath) {

			// Moving spider up in the maze.
			if (nextPath.getRow() == (thread.getRowPos() - 1)) {
				if (isValidMove(thread, thread.getRowPos() - 1, thread.getColPos())) {
					thread.setRowPos(thread.getRowPos() - 1);
					foundNextPath = true;
					break;
				}
			}
			// Moving the spider down in the maze.
			if (nextPath.getRow() == (thread.getRowPos() + 1)) {
				if (isValidMove(thread, thread.getRowPos() + 1, thread.getColPos())) {
					thread.setRowPos(thread.getRowPos() + 1);
					foundNextPath = true;
					break;
				}
			}
			// Moving the spider left in the maze.
			if (nextPath.getCol() == (thread.getColPos() - 1)) {
				if (isValidMove(thread, thread.getRowPos(), thread.getColPos() - 1)) {
					thread.setColPos(thread.getColPos() - 1);
					foundNextPath = true;
					break;
				}
			}
			// Moving the spider right in the maze.
			if (nextPath.getCol() == (thread.getColPos() + 1)) {
				if (isValidMove(thread, thread.getRowPos(), thread.getColPos() + 1)) {
					thread.setColPos(thread.getColPos() + 1);
					foundNextPath = true;
					break;
				}
			}

			// In case the spider gets stuck, randomly move it.
			if (!foundNextPath) {
				randomWalk(thread, new Random().nextInt((3 - 0) + 1) + 0);
			}

			// In case the game is over.
			if (thread.getHealth() <= 0 || thread.getPlayer().isGameOver()) {
				thread.setRun(false); // Kill the thread.
				break;
			}

		}

	}// end makeMove.

}
