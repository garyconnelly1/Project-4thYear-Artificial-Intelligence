package ie.gmit.sw.ai.Traversers;

import ie.gmit.sw.ai.*;

import java.awt.Color;

public class TraversatorStats {
	public static int printStats(Node node, long time, int visitCount, boolean countSteps){
		
		int count = 0;
		
		while (node != null){			
			node = node.getParent();
			if (node != null){
				if(!countSteps){
					if(node.getNodeType() != 'P'){
						node.setColor(Color.YELLOW);
						node.setNodeType('Q');
						node.setWalkable(true);
					}
				}
				count++;
			}	
		}
		     
        return count;
	}
}