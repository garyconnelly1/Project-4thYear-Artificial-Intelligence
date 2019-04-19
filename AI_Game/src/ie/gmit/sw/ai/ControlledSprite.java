package ie.gmit.sw.ai;

//import javax.imageio.*;
//import java.awt.image.*;
public class ControlledSprite extends Sprite{//extends GameCharacter{	
    private int score;
    private int stepsToExit;
    private int steps;
    private boolean help = false;
    private String weapon;
    private int weaponStrength;
    private int searchCount;
    private boolean gameOver;
    private int health;
    private int defence;
    private int rowPos;
    private int colPos;
    

    public ControlledSprite() {
        super();
    }
   
    public ControlledSprite(int health, int armor, String name, int frames, String... images) throws Exception {
        //super(health, armor);
    	super(name, frames, images);
        setHealth(health);
        setDefence(armor);
        
    }
    
    
    public ControlledSprite(int health, int armor, int colPos, int rowPos) {
        setHealth(health);
        setDefence(armor);
        setColPos(colPos);
        setRowPos(rowPos);
    }

    

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getStepsToExit() {
        return stepsToExit;
    }

    public void setStepsToExit(int stepsToExit) {
        this.stepsToExit = stepsToExit;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public boolean getHelp() {
        return help;
    }

    public void setHelp(boolean help) {
        this.help = help;
    }

    public String getWeapon() {
        return weapon;
    }

    public void setWeapon(String weapon) {
        this.weapon = weapon;
    }

    public int getWeaponStrength() {
        return weaponStrength;
    }

    public void setWeaponStrength(int weaponStrength) {
        this.weaponStrength = weaponStrength;
    }

    public int getSearchCount() {
        return searchCount;
    }

    public void setSearchCount(int searchCount) {
        this.searchCount = searchCount;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
    
    
    
    
    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getDefence() {
        return defence;
    }

    public void setDefence(int defence) {
        this.defence = defence;
    }

    public int getRowPos() {
        return rowPos;
    }

    public void setRowPos(int rowPos) {
        this.rowPos = rowPos;
    }

    public int getColPos() {
        return colPos;
    }

    public void setColPos(int colPos) {
        this.colPos = colPos;
    }
    
    
    
    
  
	
	
	public void setDirection(Direction d){
		switch(d.getOrientation()) {
		case 0: case 1:
			super.setImageIndex(0); //UP or DOWN
			break;
		case 2:
			super.setImageIndex(1);  //LEFT
			break;
		case 3:
			super.setImageIndex(2);  //LEFT
		default:
			break; //Ignore...
		}		
	}
	
}