package ie.gmit.sw.ai;

import java.util.Random;

public class Maze {

	// generation alogs we use in the labs. There are no "walls" to carve...
	private Node[][] maze;
	private Node goal;
	private int goalPos;

	public Maze(int dimension) {
		maze = new Node[dimension][dimension];
		init();
		buildMaze();

		//setGoalNode();
		placeGoal('G', '0');
		//unvisit();
		
		
		
		//featureNumber = 20;
		addFeature('F', '0', 40);
		addFeature('M', '0', 30);
		addFeature('K', '0', 40);
		addFeature('?', '0', 45);
		addFeature('B', '0', 30);
		addFeature('H', '0', 20);
		//addFeature('N', '0', 10);
	
	 
	 
		
		
		generateMaze();
	}

	private void init() {
		for (int row = 0; row < maze.length; row++) {
			for (int col = 0; col < maze[row].length; col++) {
				// maze[row][col] = '0'; //Index 0 is a hedge...
				maze[row][col] = new Node(row, col);
				//maze[row][col].setNodeType('X');
				maze[row][col].setNodeType('0');
				maze[row][col].setWalkable(false);
			}
		}
	}

	
	private void addFeature(char feature, char replace, int number) {
		
		int counter = 0;
		while (counter < number) { // Keep looping until feature number of items have been added.
			int row = (int) (maze.length * Math.random());
			int col = (int) (maze[0].length * Math.random());

			if (maze[row][col].getNodeType() == replace) {
				maze[row][col].setNodeType(feature); 
				counter++;
			}
		}
	}
	
	
	

	private void buildMaze() {
		System.out.println("buildMaze()");
		for (int row = 0; row < maze.length; row++) {
			for (int col = 0; col < maze[row].length - 1; col++) {
				int num = (int) (Math.random() * 10);
				if (num >= 5 && col + 1 < maze[row].length - 1) {
					// When char is set to ' ' this means its a free path or area to walk
					maze[row][col + 1].setNodeType(' ');
					//maze[row][col + 1].setNodeType(NodeType.ROAD);
					maze[row][col + 1].setWalkable(true);
					continue;
				}
				if (row + 1 < maze.length) { // Check south
					maze[row + 1][col].setNodeType(' ');
					//maze[row][col + 1].setNodeType(NodeType.ROAD);
					maze[row + 1][col].setWalkable(true);
				}
			}
		}
		
	}
	
	
	
	
	private void placeGoal(char feature, char replace) { // Change this to make is far away from the player.
		System.out.println("Place goal.");
		
		
			int row = (int) (maze.length * Math.random());
			int col = (int) (maze[0].length * Math.random());
			
			//if (maze[row][col].getNodeType() == replace){
				//door = new Node(row,col,feature);
				maze[row][col].setNodeType(feature);
				maze[row][col].setGoalNode(true);
				maze[row][col].setWalkable(true);
				goal = maze[row][col];
				System.out.println("Place - ");
				System.out.println("Is Goal? " + goal.isGoalNode());
				
		//	}
		
}
	
	
	protected void unvisit() {
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[i].length; j++) {
				maze[i][j].setVisited(false);
				maze[i][j].setParent(null);
			}
		}
		
	}
	
	
	
	
	 private void generateMaze(){ // Binary Maze gen.
	        for (int row = 0; row < maze.length; row++){
	            for (int col = 0; col < maze[row].length - 1; col++){
	            	/*
	            	int num = (int) (Math.random() * 10);
					if (col > 0 && (row == 0 || num >= 5)){
						maze[row][col].addPath(Node.Direction.West);
					}else{
						maze[row][col].addPath(Node.Direction.North);	
					}	*/
	            	if(col < maze[row].length - 1)
	                    if(maze[row][col + 1].isWalkable()){
	                        maze[row][col].addPath(Node.Direction.West);
	                    }
	                if(col > 0)
	                    if(maze[row][col - 1].isWalkable()){
	                        maze[row][col].addPath(Node.Direction.East);
	                    }
	                if(row < maze.length - 1)
	                    if(maze[row + 1][col].isWalkable()){
	                        maze[row][col].addPath(Node.Direction.North);
	                    }
	                if(row > 0)
	                    if(maze[row - 1][col].isWalkable()){
	                        maze[row][col].addPath(Node.Direction.South);
	                    }
	            }
	        }
	    }
	

	//private boolean isRoom(int row, int col) { // Flaky and only works half the time, but reduces the number of rooms
	//	return row > 1 && maze[row - 1][col].getNodeType() == ' ' && maze[row - 1][col + 1].getNodeType() == ' ';
	//}

    public Node getGoalNode(){
        return this.goal;
    }

    public Node[][] getMaze(){
        return this.maze;
    }

    public int getGoalPos() {
        return goalPos;
    }

    public void setGoalPos(int goalPos) {
        this.goalPos = goalPos;
    }

	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (int row = 0; row < maze.length; row++) {
			for (int col = 0; col < maze[row].length; col++) {
				sb.append(maze[row][col]);
				if (col < maze[row].length - 1)
					sb.append(",");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
}