package ie.gmit.sw.ai;



import ie.gmit.sw.ai.Traversers.AStarTraversator;
import ie.gmit.sw.ai.Traversers.BasicHillClimbingTraversator;
import ie.gmit.sw.ai.Traversers.BeamTraversator;
import ie.gmit.sw.ai.Traversers.BestFirstTraversator;
import ie.gmit.sw.ai.Traversers.DepthLimitedDFSTraversator;
import ie.gmit.sw.ai.Traversers.IDAStarTraversator;
import ie.gmit.sw.ai.Traversers.Traversator;

public class PlayerController{
	
	/*
	 * isValidMove().
	 * 
	 */
	
	public static int exitAlgorithm = 0;
	
	
	public static boolean isValidMove(GameSetup setup, int r, int c) {

		Node[][] maze;// = new Node[][];
		maze = setup.getMaze();
		if (!(r <= setup.getMaze().length - 1 && c <= setup.getMaze()[r].length - 1))
			return false;

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

			return fight(maze, setup, r, c);

		// return
			//return classify(setup.getEnemies().get(setup.getMaze()[r][c].getEnemyID()), maze, r, c);

		case 'A':

			return fight(maze, setup, r, c);

		default:
			return false;

		}
	}
	
	public static boolean fight(Node[][] maze, GameSetup setup, int r, int c) {
		FuzzyFight fuzzyBattle1 = new FuzzyFight();
		boolean enemyWon1 = fuzzyBattle1.startBattle(setup.getPlayer(),
				setup.getEnemies().get(setup.getMaze()[r][c].getEnemyID()), "fcl/fuzzyFight.fcl");
		if (enemyWon1 == true) {
			// The player has lost the game!
			maze[setup.getPlayer().getRowPos()][setup.getPlayer().getColPos()].setNodeType(' ');
			// game.getMaze()[game.getPlayer().getRowPos()][game.getPlayer().getColPos()].setNodeType(NodeType.ROAD);
			maze[setup.getPlayer().getRowPos()][setup.getPlayer().getColPos()].setEnemyID(0);
			setup.getPlayer().setGameOver(true);
			maze[r][c].setNodeType('L');
			// game.getMaze()[r][c].setNodeType(NodeType.LOSE);

			return false;
		} else {
			setup.getEnemies().get(setup.getMaze()[r][c].getEnemyID()).setHealth(0);
			maze[r][c].setNodeType('D');
			// game.getMaze()[r][c].setNodeType(NodeType.DEADSPIDER);
			maze[r][c].setEnemyID(0);

			return false;
		}

	}
	
	
	public static Traversator algorithm(GameSetup setup) {
		// Selecting a random algorithm to be created and returned
		switch (exitAlgorithm) {
		case 0:
			return new AStarTraversator(setup.getModel().getGoalNode(), false);
		case 1:
			return new BeamTraversator(setup.getModel().getGoalNode(), 10);
		case 2:
			return new BestFirstTraversator(setup.getModel().getGoalNode());
		case 3:
			return new IDAStarTraversator(setup.getModel().getGoalNode());
		case 4:
			return new BasicHillClimbingTraversator(setup.getModel().getGoalNode());
			//return new BasicHillClimbingTraversator(setup.getModel().getGoalNode());
		case 5:
			//return new BasicHillClimbingTraversator(setup.getModel().getGoalNode());
			// return new DepthLimitedDFSTraversator(game.getMaze().length);
			
		case 6:
			//return new DepthLimitedDFSTraversator(5); // Only works sometimes.
			// return new SimulatedAnnealingTraversator(game.getModel().getGoalNode());
			//return new BasicHillClimbingTraversator(setup.getModel().getGoalNode());
		case 7:
			// return new IDDFSTraversator();
		default:
			return new AStarTraversator(setup.getModel().getGoalNode(), false);
		}
	}
	
	public static void setExitAlgorithm(int algorithm) {
		exitAlgorithm = algorithm;
		System.out.println("Algorithm chosen: " + exitAlgorithm);
	}


}
