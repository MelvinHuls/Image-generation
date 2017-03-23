package image_generation;

import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.util.List;
import java.util.ArrayList;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.FileImageOutputStream;

class Gifs {	
	public static void main(String[] args) throws Exception {
		Integer width = Integer.valueOf(args[0]);
		Integer height = Integer.valueOf(args[1]);
		if(width != null && height != null) {			
			Images image = new Images();
			int amount;
			int milliSecBetween = 500;
			if(args.length < 3) {
				amount = 1;
			} else if(args.length < 4){
				amount  = Integer.valueOf(args[2]);
			} else {
				amount  = Integer.valueOf(args[2]);
				image.setRANGE(Integer.valueOf(args[3]));
				if(args.length >= 5) {
					milliSecBetween = Integer.valueOf(args[3]);
				}
			}			
			
			BufferedImage newImage = image.getImageFromArray(width,height);		
			
			ImageOutputStream output = 
				new FileImageOutputStream(new File("image.gif"));
			
			GifSequenceWriter writer = 
				new GifSequenceWriter(output, newImage.getType(), milliSecBetween/1000, true);
				
			writer.writeToSequence(newImage);	
			image.setPreviousImage(newImage);
			 
			for(int i = 0; i < amount; i++) {		
				newImage = image.getImageFromArray(width,height);
				writer.writeToSequence(newImage);				
				image.setPreviousImage(newImage);				
			}
			writer.close();
			output.close();
		} else {
			System.out.println("Don't forget your image dimensions");
		}
	}
}