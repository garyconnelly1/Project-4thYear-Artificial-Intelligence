package ie.gmit.sw.ai.sprites;

public class GameCharacter {
	/*
	 * Super-class for the spider. Initially was created because I though I would have a separate class
	 * for the A* spider and regular spider, but ended up putting them in the same class meaning this class
	 * has only one sub-class..
	 */
	private int health;

	private int rowPos;
	private int colPos;

	public GameCharacter() {
		setHealth(100);
	}

	public GameCharacter(int health) {
		setHealth(health);

	}

	public GameCharacter(int health, int armor, int colPos, int rowPos) {
		setHealth(health);
		setColPos(colPos);
		setRowPos(rowPos);
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
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
}