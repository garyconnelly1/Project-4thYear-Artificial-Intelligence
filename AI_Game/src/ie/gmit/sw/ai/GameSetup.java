package ie.gmit.sw.ai;

import java.util.ArrayList;
import java.util.Random;

public class GameSetup {
	public static final int IMAGE_COUNT = 20;
	
	private Maze model;
    private Node[][] maze;
    private ControlledSprite player;
    private ArrayList<Spider> spiders;
    
    public GameSetup(Maze model, Node[][] maze, ControlledSprite player, ArrayList<Spider> enemies) {
        setModel(model);
        setMaze(maze);
        setPlayer(player);
        setEnemies(enemies);
    }
    
    public GameSetup() {
    	//System.out.println("GameSetup()");
    	
    }

  
    public void placePlayer(int goalPos){

    	player.setWeapon("Unarmed");
    

        Random random = new Random();
        boolean playerPosSet = false;

        // Continue to loop until a good position is found
        while(playerPosSet != true){

            switch(goalPos){
                case 0:
                    // Creates the player on the top side of the maze
                	player.setRowPos(random.nextInt((3 - 2) + 1) + 2);
                	player.setColPos(random.nextInt((getMaze()[0].length - 5) + 1) + 5);
                    break;
                case 1:
                    // Creates the player on the left side of the maze
                	player.setRowPos(random.nextInt(((getMaze().length - 15) - 1) + 1) + 1);
                	player.setColPos(random.nextInt((3 - 2) + 1) + 2);
                    break;
                case 2:
                    // Creates the player on the bottom side of the maze
                	player.setRowPos(random.nextInt(((getMaze().length - 15) - (getMaze().length - 15)) + 1) + (getMaze().length - 15));
                	player.setColPos(random.nextInt((getMaze()[0].length - 5) + 1) + 5);
                    break;
                default:
                	player.setRowPos(random.nextInt(((getMaze().length - 15) - 1) + 1) + 1);
                	player.setColPos(random.nextInt((3 - 2) + 1) + 2);
                    break;
            }

            // Checking if the area is walkable, if true then place the player and setting the node type
            try {
                if(maze[getPlayer().getRowPos()][getPlayer().getColPos()].isWalkable()){
                    maze[getPlayer().getRowPos()][getPlayer().getColPos()].setNodeType('P');
                    maze[getPlayer().getRowPos()][getPlayer().getColPos()].setGoalNode(true);
                    playerPosSet = true;
                }
            } catch (Exception e) {
            }
        }
    }

    
    public void placeEnemies(String gameDifficulty){
    	//System.out.println("Place enemies.");

        int amount;
        int health;
        int armor;
        int strength;
        int difficulty;
        int bosses;
        boolean isAstar = false;

        Random random = new Random();

        // Randomly choosing the enemy's strengths, depending on difficulty setting
        switch(gameDifficulty){
            case "Easy":
                amount = 35;
                health = random.nextInt((60 - 25) + 1) + 25;
                armor = random.nextInt((35 - 5) + 1) + 5;
                strength = random.nextInt((60 - 30) + 1) + 30;
                difficulty = 0;
                bosses = 3;
                break;
            case "Normal":
                amount = 50;
                health = random.nextInt((75 - 55) + 1) + 55;
                armor = random.nextInt((45 - 25) + 1) + 25;
                strength = random.nextInt((70 - 30) + 1) + 30;
                difficulty = 1;
                bosses = 5;
                break;
            case "Hard":
                amount = 65;
                health = random.nextInt((100 - 40) + 1) + 40;
                armor = random.nextInt((75 - 50) + 1) + 50;
                strength = random.nextInt((80 - 30) + 1) + 30;
                difficulty = 2;
                bosses = 10;
                break;
            default:
                amount = 50;
                health = random.nextInt((75 - 55) + 1) + 55;
                armor = random.nextInt((45 - 25) + 1) + 25;
                strength = random.nextInt((70 - 30) + 1) + 30;
                difficulty = 1;
                bosses = 5;
                break;
        }

        setEnemies(new ArrayList<Spider>());

	
        for(int i = 0; i < amount; i++){

            if(bosses > 0){
                isAstar = true;
                health = 100;
                bosses--;
            }

            // Create an enemy object & create new thread
            Runnable enemy = new Spider(i, health, isAstar);
            Thread thread = new Thread(enemy);
            this.spiders.add((Spider) enemy);
            this.spiders.get(i).setInstance(thread);
            this.spiders.get(i).setMaze(getMaze());
            this.spiders.get(i).setPlayer(this.player);
            this.spiders.get(i).setRun(true);
            this.spiders.get(i).setMinUpdateTime(600);
            this.spiders.get(i).setMaxUpdateTime(750);

            // Set the boss objects to be able to use A star algorithm. Change this to use other algorithms too.
            if(isAstar)
               // this.spiders.get(i).setAlgorithm(random.nextInt((1 - 1) + 1) + 1);
            	this.spiders.get(i).setAlgorithm(1);

            boolean enemyPosSet = false;

            // Continue to loop until a good position is found
            while(enemyPosSet != true){
                this.spiders.get(i).setRowPos((int)(GameRunner.MAZE_DIMENSION * Math.random()));
                this.spiders.get(i).setColPos((int)(GameRunner.MAZE_DIMENSION * Math.random()));

                // Checking if the area is walkable, if true then place enemy
                if(maze[spiders.get(i).getRowPos()][spiders.get(i).getColPos()].isWalkable()){
                    maze[spiders.get(i).getRowPos()][spiders.get(i).getColPos()].setNodeType('S');
                    if(this.spiders.get(i).isAstar()) 
                    	maze[spiders.get(i).getRowPos()][spiders.get(i).getColPos()].setNodeType('A');
                   maze[spiders.get(i).getRowPos()][spiders.get(i).getColPos()].setEnemyID(i);
                    enemyPosSet = true;
                }
            }

            isAstar = false;
            thread.start();
        }
    }

    /**
     * Kills the previously running enemy threads
     */
    public void killEnemyThreads() {
        if(this.spiders == null || this.spiders.size() <= 0) return;
        for(int i = 0; i < this.spiders.size(); i++){
            this.spiders.get(i).setHealth(0);
            this.spiders.get(i).setRun(false);
        }
    }

    public Maze getModel() {
        return model;
    }

    public void setModel(Maze model) {
        this.model = model;
    }

    public Node[][] getMaze() {
        return maze;
    }

    public void setMaze(Node[][] maze) {
        this.maze = maze;
    }

    public ControlledSprite getPlayer() {
        return player;
    }

    public void setPlayer(ControlledSprite player) {
        this.player = player;
    }

    public ArrayList<Spider> getEnemies() {
        return spiders;
    }

    public void setEnemies(ArrayList<Spider> spiders) {
        this.spiders = spiders;
    }
    
public Sprite[] getSprites() throws Exception {
		

		Sprite[] sprites = new Sprite[IMAGE_COUNT];
		sprites[0] = new Sprite("Hedge", 1, "resources/images/objects/hedge.png");
		sprites[1] = new Sprite("Sword", 1, "resources/images/objects/sword.png");
		sprites[2] = new Sprite("Help", 1, "resources/images/objects/help.png");
		sprites[3] = new Sprite("Bomb", 1, "resources/images/objects/bomb.png");
		sprites[4] = new Sprite("Hydrogen Bomb", 1, "resources/images/objects/h_bomb.png");
		sprites[5] = this.player;//new Sprite("Player", 3, "resources/images/player/d1.png", "resources/images/player/d2.png", "resources/images/player/d3.png");
		sprites[6] = new Sprite("Player2", 1, "resources/images/player/d2.png");
		sprites[7] = new Sprite("Red1", 1, "resources/images/spiders/red_spider_1.png");//, "resources/images/spiders/red_spider_2.png");
		sprites[8] = new Sprite("Red2", 1, "resources/images/spiders/red_spider_2.png");
		sprites[9] = new Sprite("Blue2", 1, "resources/images/spiders/blue_spider_2.png");
		sprites[10] = new Sprite("Green1", 1, "resources/images/spiders/green_spider_1.png");
		sprites[11] = new Sprite("Food", 1, "resources/images/objects/food.png");
		sprites[12] = new Sprite("Shield", 1, "resources/images/objects/shield.png");
		sprites[13] = new Sprite("Chest", 1, "resources/images/objects/chest.png");
		sprites[14] = new Sprite("Gold", 1, "resources/images/objects/goal.png");
		sprites[15] = new Sprite("Lose", 1, "resources/images/objects/rip-2.png");
		sprites[16] = new Sprite("Player", 1, "resources/images/player/d1.png");
		sprites[17] = new Sprite("Bomb", 1, "resources/images/objects/bomb.png");
		sprites[18] = new Sprite("Black1", 1, "resources/images/spiders/black_spider_1.png");//, "resources/images/spiders/black_spider_2.png");
		sprites[19] = new Sprite("Black2", 1, "resources/images/spiders/black_spider_2.png");
		
		
		return sprites;
	} 

}
