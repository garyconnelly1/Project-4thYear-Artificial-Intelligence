package ie.gmit.sw.ai.Traversers;

import ie.gmit.sw.ai.*;



public class BasicHillClimbingTraversator extends ResetTraversal implements Traversator{
	
	private Node goal;

	public BasicHillClimbingTraversator(Node goal){
		this.goal = goal;
		System.out.println("INFO[] - Searching for exit with Basic Hill Climing Algorithm.");
	}

	public void traverse(Node[][] maze, Node node){
		System.out.println("Using Basic Hill Climbing Traversator - Looking for Maze Exit Goal!\n");
		resetA(maze);
        long time = System.currentTimeMillis();
    	int visitCount = 0;

    	Node next = null;
		while(node != null){
			node.setVisited(true);	
			visitCount++;

			if (node.isGoalNode() && node.nodeType != 'P'){
			//if (node.isGoalNode() && node.getNodeType() != NodeType.PLAYER){
		        time = System.currentTimeMillis() - time; //Stop the clock
		        TraversatorStats.printStats(node, time, visitCount, false);
				break;
			}

			// Sleep for x amount of seconds
			//sleep(1);

			Node[] children = node.children(maze);			
			int fnext = Integer.MAX_VALUE;			
			for (int i = 0; i < children.length; i++) {					
				if (children[i].getHeuristic(goal) < fnext){
					next = children[i];
					fnext = next.getHeuristic(goal);	
				}
			}

			if (fnext >= node.getHeuristic(goal)){
				System.out.println("Cannot improve on current node " + node.toString() + " \nh(n)=" + node.getHeuristic(goal) + " = Local Optimum...");
				break;
			}
			node = next;	
			next = null;
		}
	}

}
