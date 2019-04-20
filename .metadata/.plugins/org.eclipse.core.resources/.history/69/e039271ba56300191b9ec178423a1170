package ie.gmit.sw.ai.Traversers;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

import ie.gmit.sw.ai.Node;
import ie.gmit.sw.ai.NodeType;



public class BeamTraversator extends ResetTraversal implements Traversator{
	
	private Node goal;
	private int beamWidth = 1; 
	private boolean isFound = false;

	public BeamTraversator(Node goal, int beamWidth){
		this.goal = goal;
		this.beamWidth = beamWidth;
		System.out.println("[INFO] - Searching for exit with Beam Search.");
	}

	@Override
	public void traverse(Node[][] maze, Node node){
		resetA(maze);
		LinkedList<Node> queue = new LinkedList<Node>();
		queue.addFirst(node);

        long time = System.currentTimeMillis();
    	int visitCount = 0;

		while(!queue.isEmpty()){
			node = queue.poll();
			node.setVisited(true);	
			visitCount++;

			if (node.isGoalNode() && node.nodeType != 'P'){ // Check it against the player type.
			//if (node.isGoalNode() && node.getNodeType() != NodeType.PLAYER){ // Check it against the player type.
		        time = System.currentTimeMillis() - time; //Stop the clock
		        TraversatorStats.printStats(node, time, visitCount, false);
		        isFound = true;
				break;
			}

			// Sleep for x amount of seconds
			//sleep(1);

			Node[] children = node.children(maze);
			Collections.sort(Arrays.asList(children),(Node current, Node next) -> current.getHeuristic(goal) - next.getHeuristic(goal));

			int bound = 0;
			if (children.length < beamWidth){
				bound = children.length;
			}else{
				bound = beamWidth;
			}

			for (int i = 0; i < bound; i++) {
				if (children[i] != null && !children[i].isVisited()){
					children[i].setParent(node);
					queue.addFirst(children[i]);
				}
			}
		}
		
		if(!isFound) {
			System.out.println("Pruned Goal Node!");
		}
	}

	

}
