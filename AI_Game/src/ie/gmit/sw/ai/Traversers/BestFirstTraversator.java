package ie.gmit.sw.ai.Traversers;

import java.util.*;

import ie.gmit.sw.ai.*;

public class BestFirstTraversator extends ResetTraversal implements Traversator{
	
	private Node goal;
	//private Node player;
	
	public BestFirstTraversator(Node goal){ // Pass in goal node instead of player.
		this.goal = goal;
		System.out.println("[INFO] - Searching for exit with Best First Search Algorithm.");
	}
	
	public void traverse(Node[][] maze, Node node){
		System.out.println("Using Best First Traversator - Looking for Maze Exit Goal!\n");
		resetA(maze);
		LinkedList<Node> queue = new LinkedList<Node>();
		queue.addFirst(node);

        long time = System.currentTimeMillis();
    	int visitCount = 0;

		while(!queue.isEmpty()){
			node = queue.poll();
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
			for (int i = 0; i < children.length; i++) {
				if (children[i] != null && !children[i].isVisited()){
					children[i].setParent(node);
					queue.addFirst(children[i]);
				}
			}

			//Sort the whole queue. Effectively a priority queue, first in, best out
			Collections.sort(queue,(Node current, Node next) -> current.getHeuristic(goal) - next.getHeuristic(goal));
		}
	}

/*
	@Override
	public Node hunt(Node[][] maze, Node node, Spider spider) {
		LinkedList<Node> queue = new LinkedList<Node>();
		queue.addFirst(node);

		long time = System.currentTimeMillis();
		int visitCount = 0;

		while(!queue.isEmpty()){
			node = queue.poll();
			node.setVisited(true);
			visitCount++;

			if (node.isGoalNode() && node.nodeType != 'P'){
				time = System.currentTimeMillis() - time; //Stop the clock
				TraversatorStats.printStats(node, time, visitCount, false);
				break;
			}

			// Sleep for x amount of seconds
			//sleep(1);

			Node[] children = node.children(maze);
			for (int i = 0; i < children.length; i++) {
				if (children[i] != null && !children[i].isVisited()){
					children[i].setParent(node);
					queue.addFirst(children[i]);
				}
			}

			//Sort the whole queue. Effectively a priority queue, first in, best out
			Collections.sort(queue,(Node current, Node next) -> current.getHeuristic(goal) - next.getHeuristic(goal));
		}
		return null;
}
*/

}
