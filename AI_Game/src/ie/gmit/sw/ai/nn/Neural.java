package ie.gmit.sw.ai.nn;


import java.util.Scanner;

import ie.gmit.sw.ai.nn.activator.Activator;

public class Neural {
	/*
	private static final double[][] data = { // Health, Strength, Defence, Score
			{ 2, 0, 0, 0 }, { 2, 0, 0, 1 }, { 2, 0, 1, 1 }, { 2, 0, 1, 2 },
			{ 2, 1, 0, 2 }, { 2, 1, 0, 1 }, { 1, 0, 0, 0 }, { 1, 0, 0, 2 },
			{ 1, 0, 1, 1 }, { 1, 0, 1, 2 }, { 1, 1, 0, 1}, { 1, 1, 0, 1 },
			{ 0, 0, 0, 0 }, { 0, 0, 0, 1 }, { 0, 0, 1, 1 }, { 0, 0, 1, 2 },
			{ 0, 1, 0, 2 }, { 0, 1, 0, 1 } };

	private static final double[][] expected = { // Panic, Attack, Hide, Run
			{ 0.0, 1.0, 0.0, 0.0 }, { 0.0, 0.0, 0.0, 1.0 }, { 1.0, 0.0, 0.0, 0.0 }, { 1.0, 0.0, 0.0, 0.0 },
			{ 1.0, 0.0, 0.0, 0.0 }, { 0.0, 0.0, 1.0, 0.0 }, { 0.0, 1.0, 0.0, 0.0 }, { 0.0, 0.0, 0.0, 1.0 },
			{ 0.0, 0.0, 0.0, 1.0 }, { 0.0, 0.0, 1.0, 0.0 }, { 0.0, 0.0, 0.0, 1.0 }, { 0.0, 0.0, 0.0, 1.0 },
			{ 1.0, 0.0, 0.0, 0.0 }, { 0.0, 1.0, 0.0, 0.0 }, { 0.0, 0.0, 0.0, 1.0 }, { 0.0, 1.0, 0.0, 0.0 },
			{ 0.0, 0.0, 0.0, 1.0 }, { 0.0, 1.0, 0.0, 1.0 } };
			
			*/
	
	private static final double[][] data = { // Health, Strength, Defence, Score
			{ 2, 0, 0 }, { 2, 1, 1 }, { 2, 0, 1 }, { 2, 1, 2 },
			{ 2, 1, 0}, { 2, 2, 0 }, { 1, 0, 0 }, { 1, 0, 0 },
			{ 1, 0, 1 }, { 1, 0, 1 }, { 1, 1, 0}, { 1, 2, 0},
			{ 0, 0, 0}, { 2, 2, 2}, { 0, 0, 1 }, { 1, 1, 1 },
			{ 0, 1, 0 }, { 0, 1, 0 } };

	private static final double[][] expected = { // Panic, Attack, Hide, Run
			{ 0.0, 1.0, 0.0, 0.0 }, { 1.0, 0.0, 0.0, 0.0 }, { 0.0, 0.0, 1.0, 0.0 }, { 1.0, 0.0, 0.0, 0.0 },
			{ 0.0, 0.0, 1.0, 0.0 }, { 1.0, 0.0, 0.0, 0.0 }, { 0.0, 1.0, 0.0, 0.0 }, { 0.0, 1.0, 0.0, 0.0 },
			{ 0.0, 0.0, 0.0, 1.0 }, { 0.0, 0.0, 0.0, 1.0 }, { 0.0, 0.0, 0.0, 1.0 }, { 0.0, 0.0, 1.0, 0.0 },
			{ 0.0, 1.0, 0.0, 0.0 }, { 1.0, 0.0, 0.0, 0.0 }, { 0.0, 1.0, 0.0, 0.0 }, { 0.0, 0.0, 0.0, 1.0 },
			{ 0.0, 1.0, 0.0, 0.0 }, { 0.0, 1.0, 0.0, 0.0 } };
	
	private static NeuralNetwork nn = new NeuralNetwork(Activator.ActivationFunction.Sigmoid, 3, 3, 4);

	public static void main(String[] args) throws Exception {
		trainNetwork();
		Scanner scanner = new Scanner(System.in);
		while(true) {
			System.out.println("Press 1.");
			int number = scanner.nextInt();
			if(number == 1) {
				process(80,50,90);
			}
		}
		

	}
	
	public static void trainNetwork() throws Exception {
		
		
		BackpropagationTrainer trainer = new BackpropagationTrainer(nn);
		trainer.train(data, expected, 0.6, 10000);
			
	}
	
	public static int process(int health, int strength, int defence) throws Exception {
		
		if(health < 25) {
			health = 0;
		}else if(health < 75) {
			health = 1;
		}else {
			health = 2;
		}
		
		if(strength < 40) {
			strength = 0;
		}else if(strength < 75) {
			strength = 1;
		}else {
			strength = 2;
		}
		
		if(defence < 25) {
			defence = 0;
		}else if(defence < 75){
			defence = 1;
		}else {
			defence = 2;
		}
		
		
		
		int testIndex = 0;
		
		//System.out.println(health);
		//System.out.println(strength);
		//System.out.println(defence);
		
		double[] vector = {health, strength, defence};
		
		
		
		//double[] result = nn.process(data[testIndex]);
		double[] result = nn.process(vector);
		/*
		for (int i = 0; i < expected[testIndex].length; i++){
		 System.out.println(expected[testIndex][i] + ",");
		}
		*/
		System.out.println("==>" + (Utils.getMaxIndex(result) + 1));
		
		int classification = (Utils.getMaxIndex(result) + 1);
		/*
		switch(classification) {
		case 1:
			System.out.println("Panic.");
			break;
		case 2:
			System.out.println("Attack");
			break;
		case 3:
			System.out.println("Hide");
		case 4:
			System.out.println("Run");
			default:
				System.out.println("Err.");
		}
		*/
		
		return classification;
		
	}

}
