package ie.gmit.sw.ai.Traversers;

import ie.gmit.sw.ai.Node;
import ie.gmit.sw.ai.Spider;

public interface Hunterable {
	
	public Node hunt(Node[][] maze, Node start, Spider spider);

}
