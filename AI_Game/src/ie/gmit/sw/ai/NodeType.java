package ie.gmit.sw.ai;

public enum NodeType {
	/*
	 * Ended up not using this. The intention was to use this to the node types instead of chars, but the GUI
	 * would keep crashing when I did this and I never figured out why.
	 */
	
	HEDGE, SWORD, HELP,
	BOMB, H_BOMB, SPIDER,
	ASTARSPIDER, DEADSPIDER, HELPSPIDER, 
	HEALTH, ARMOR, PLAYER, GOAL,
	END, LOSE, PLAYER1, BOMB2, ROAD

}
