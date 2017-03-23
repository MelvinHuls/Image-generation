import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Color;

class Images {	
	public static final int WHITE = -1;
	public static final int BLACK = -16777216;
	public static final String name = "./images/img";
	public static int index = 0;
	public static BufferedImage image;
	private static int RANGE = 50;
	
	public static void main(String[] args) throws Exception {
		Integer width = Integer.valueOf(args[0]);
		Integer height = Integer.valueOf(args[1]);
		if(width != null && height != null) {
			Integer amount;
			if(args.length < 3) {
				amount = 1;
			} else if(args.length < 4){
				amount  = Integer.valueOf(args[2]);
			} else {
				amount  = Integer.valueOf(args[2]);
				RANGE = Integer.valueOf(args[3]);
			}
			 
			for(int i = 0; i < amount; i++) {
				getImageFromArray(width,height,"./images/random" + i + ".png");
			}
		} else {
			System.out.println("Don't forget your image dimensions");
		}
	}
	
	public static void getImageFromArray(int width, int height, String name) throws Exception{
		try {
			image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);			
			for(int w = 0; w < width; w++) {
				for(int h = 0; h < height; h++) {					
					image.setRGB(w,h, getNewColorRGB(w, h));
				}
			}
			
			File outputfile = new File(name);
			ImageIO.write(image, "png", outputfile);
		} catch (Exception ex) {
			System.err.println("You done fucked up ");
			throw ex; 	
		}
	}	
	
	private static int getNewColorRGB(int w, int h) {
		Color averageColor = averageColor(w,h);
		int randRed = (int)(Math.random()*2*(RANGE+1))-RANGE;
		int red = averageColor.getRed() + randRed;
		int randGreen = (int)(Math.random()*2*(RANGE+1))-RANGE;
		int green = averageColor.getGreen() + randGreen;
		int randBlue = (int)(Math.random()*2*(RANGE+1))-RANGE;
		int blue = averageColor.getBlue() + randBlue;
		if(red > 255)red = 255; else if (red < 0) red = 0;
		if(green > 255)green = 255; else if (green < 0) green = 0;
		if(blue > 255)blue = 255; else if (blue < 0) blue = 0;
		
		return new Color(red,green,blue).getRGB();
	}
	
	private static Color averageColor(int w, int h) {
		int[] rgb = new int[4];
		if(w > 0) {
			addCollor(rgb,w-1,h);
			
			if(h > 0) {
				addCollor(rgb,w-1,h-1);
				
				if(h > 1) 
					addCollor(rgb,w-1,h-2);				
			}
		}
		if(h>0){				
			addCollor(rgb,w,h-1);
			
			if(h>1)
				addCollor(rgb,w,h-2);
		}
		if(w > 1) {
			addCollor(rgb,w-2,h);
			
			if(h > 0) {
				addCollor(rgb,w-2,h-1);
				
				if(h > 1) 
					addCollor(rgb,w-2,h-2);
			}
		}	
		if(rgb[3] <= 0) {return new Color(125,125,125);}
		return new Color(rgb[0]/rgb[3], rgb[1]/rgb[3], rgb[2]/rgb[3]);
	}
	
	private static void addCollor(int[] rgb, int w, int h) {
		Color temp = new Color(image.getRGB(w,h));
		rgb[0] += temp.getRed();
		rgb[1] += temp.getGreen();
		rgb[2] += temp.getBlue();
		rgb[3]++;
	}
}
