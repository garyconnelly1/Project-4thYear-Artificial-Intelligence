package ie.gmit.sw.ai.nn;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ie.gmit.sw.ai.nn.activator.Activator;

public class Neural {
	/*
	 * "resources/neural/Data.txt" "resources/neural/Expected.txt"
	 */

	private static final double[][] data = readData("resources/neural/Data.txt");

	private static final double[][] expected = readExpected("resources/neural/Expected.txt");

	private static NeuralNetwork nn = new NeuralNetwork(Activator.ActivationFunction.Sigmoid, 3, 3, 4);

	public static void main(String[] args) throws Exception {
		trainNetwork();

		process(80, 50, 20);
		// process(50,50,80);

	}

	public static void trainNetwork() throws Exception {

		System.out.println("Training Neural Network...");
		BackpropagationTrainer trainer = new BackpropagationTrainer(nn);
		trainer.train(data, expected, 0.6, 30000);

	}

	public static int process(int health, int strength, int defence) throws Exception {

		if (health < 25) {
			health = 0;
		} else if (health < 75) {
			health = 1;
		} else {
			health = 2;
		}

		if (strength < 40) {
			strength = 0;
		} else if (strength < 75) {
			strength = 1;
		} else {
			strength = 2;
		}

		if (defence < 25) {
			defence = 0;
		} else if (defence < 75) {
			defence = 1;
		} else {
			defence = 2;
		}

		

		

		double[] vector = { health, strength, defence };

		
		double[] result = nn.process(vector);
		
		 
		 
		// System.out.println("==>" + (Utils.getMaxIndex(result) + 1));

		int classification = (Utils.getMaxIndex(result) + 1);
		System.out.println("Classification ---> " + classification);

		switch (classification) {
		case 1:
			System.out.println("Neural Network ---> Panic.");
			break;
		case 2:
			System.out.println("Neural Network ---> Attack");
			break;
		case 3:
			System.out.println("Neural Network ---> Hide");
			break;
		case 4:
			System.out.println("Neural Network ---> Run ");
			break;
		default:
			System.out.println("Err.");
		}

		return classification;

	}

	public static double[][] readData(String fileName) {
		FileReader fileReader;
		fileReader = null;
		String[] dataTuple;
		double[][] data;
		data = new double[37][3];
		try {
			fileReader = new FileReader(fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		List<String> lines = new ArrayList<String>();
		String line = null;
		try {
			while ((line = bufferedReader.readLine()) != null) {
				lines.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String[] myLines = lines.toArray(new String[lines.size()]);

		int i = 0;
		for (String s : myLines) {
			dataTuple = myLines[i].split(",");
			for (int j = 0; j < dataTuple.length; j++) {
				data[i][j] = Double.parseDouble(dataTuple[j]);
			}
			i++;

		}

		return data;
	}

	public static double[][] readExpected(String fileName) {
		FileReader fileReader;
		fileReader = null;
		String[] dataTuple;
		double[][] data;
		data = new double[37][4];
		try {
			fileReader = new FileReader(fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		List<String> lines = new ArrayList<String>();
		String line = null;
		try {
			while ((line = bufferedReader.readLine()) != null) {
				lines.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String[] myLines = lines.toArray(new String[lines.size()]);

		int i = 0;
		for (String s : myLines) {
			dataTuple = myLines[i].split(",");
			for (int j = 0; j < dataTuple.length; j++) {
				data[i][j] = Double.parseDouble(dataTuple[j]);
			}
			i++;

		}

		return data;
	}

}
