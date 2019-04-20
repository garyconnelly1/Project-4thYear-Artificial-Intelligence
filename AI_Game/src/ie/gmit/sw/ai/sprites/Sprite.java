package ie.gmit.sw.ai.sprites;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Sprite {
	
	private String name; //The name of this sprite
	private BufferedImage[][] images; //The set of image frames to animate
 	private int index = 0; //Initial starting direction that the sprite is facing
 	private int frame = 0; //Initial starting index of the image 
 	
 	public Sprite() {
 		
 	}
	
	public Sprite(String name, int frames, String... files) throws Exception{
		this.name = name;
		this.index = 0; //Initialise the starting index to zero
		this.images = new BufferedImage[files.length / frames][frames]; //Initialise the image frames
		
		//Read the varargs list of images into a 2D array
		int row = 0;
		int col = 0;
		for (int i = 0; i < files.length; i++){
			images[row][col] = ImageIO.read(new java.io.File(files[i])); //Read in each image as a BufferedImage

			col++;
			if (col % frames == 0){
				row++;
				col = 0;
			} 
		}
	}
	
	public BufferedImage getNext(){ //Returns the next image frame
		frame++;
		//System.out.println("Frame - " + frame + " index - " + index);
		if (frame == images[index].length) frame = 0; //Circle back to the start of the array

		return images[index][frame]; 
	}
	
	public int getImageIndex(){
		return this.index;
	}
	
	public void setImageIndex(int idx){
		this.index = idx;
		//System.out.println("Image index - " + idx);
		//System.out.println("This ---------------------- " + this.index);
	}

	public String getName(){
		return this.name;
	}

}
