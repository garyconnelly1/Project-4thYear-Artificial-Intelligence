package ie.gmit.sw.ai.fuzzy;

import ie.gmit.sw.ai.sprites.ControlledSprite;
import ie.gmit.sw.ai.sprites.Spider;
import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.rule.Variable;

public class FuzzyFight {
	
	/*
	 * Class allows the game to integrate with the fuzzy logic.
	 */

	public FuzzyFight() {

	}

	public boolean startBattle(ControlledSprite player, Spider enemy, String fclFilePath) {

		FIS fis = FIS.load(fclFilePath, true);

		if (fis == null) {
			System.err.println("Error loading: '" + fclFilePath + "'");
			return true;
		}

		/*
		 * Get a handle on the 'fight' function block.
		 */
		FunctionBlock functionBlock = fis.getFunctionBlock("fight");
		
		/*
		 * Provide the input variables.
		 */
		fis.setVariable("health", player.getHealth());
		fis.setVariable("defense", player.getDefence());
		fis.setVariable("weapon", player.getWeaponStrength());
		/*
		 * Evaluate the equation.
		 */
		fis.evaluate();

		/*
		 * Get a handle on the result of the fuzzy logic computation.
		 */
		Variable damage = functionBlock.getVariable("damage");
		System.out.println("Damage Percentage: " + (int) damage.getValue() + "%\n");

		boolean enemyWon = false;

		/*
		 * Decrease the players attributes accordingly with the amount of damage they received.
		 */
		player.setHealth((int) (player.getHealth() - (damage.getValue()) + 10));
		player.setDefence((int) (player.getDefence() - damage.getValue()));
		if (player.getDefence() < 0)
			player.setDefence(0);
		;
		player.setWeaponStrength((int) (player.getWeaponStrength() - damage.getValue()));
		if (player.getWeaponStrength() < 0)
			player.setWeaponStrength(0);

		// Checking health below zero, if true then the game is over.
		if (player.getHealth() <= 0) {
			player.setHealth(0);
			player.setGameOver(true);
			System.out.println("Game Over!");
			player.setScore(player.getScore() + player.getDefence() + player.getWeaponStrength());
			enemyWon = true;
			System.out.println("--- Game Over ---");
			System.out.println("Player Health: " + player.getHealth());
			System.out.println("Player Strength: " + player.getWeaponStrength());
			System.out.println("Player Defence: " + player.getDefence());
			System.out.println("Player Score: " + player.getScore());
			System.out.println("-------------------------------");
		}

		// If the player wins the fight then update the player object variables.
		if (!enemyWon) {
			player.setScore(player.getScore() + 25);
			System.out.println("Player Score: " + player.getScore());
			System.out.println("--- Post Fight Player Stats ---");
			System.out.println("Player Health: " + player.getHealth());
			System.out.println("Player Strength: " + player.getWeaponStrength());
			System.out.println("Player Defence: " + player.getDefence());
			System.out.println("Player Score: " + player.getScore());
			System.out.println("-------------------------------");
		}

		return enemyWon;
	}

}
