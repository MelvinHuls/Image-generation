package image_generation;

import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.util.List;
import java.util.ArrayList;

class Images {	
	public static final int WHITE = -1;
	public static final int BLACK = -16777216;
	public static int index = 0;
	public static BufferedImage image;
	private static int RANGE = 50;	
	private static int ROUNDRANGE = 2;
	private static BufferedImage previousImage;
	
	private static List<Coordinate> coordinates;
	
	public void setPreviousImage(BufferedImage previousImage) {
		this.previousImage = previousImage;
	}
	
	
	public void setRANGE(int range) {
		this.RANGE = range;
	}
	
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
			 
			if(args.length >= 5 && Integer.valueOf(args[4]) == 1337) {		
				if(args.length >= 6) {
					ROUNDRANGE = Integer.valueOf(args[5]);
				}
				for(int i = 0; i < amount; i++) {
					fillImageRandomOrder(width,height,"./images/random" + i + ".png");
				}
			} else {			
				for(int i = 0; i < amount; i++) {
					getImageFromArray(width,height,true,"./images/random" + i + ".png");
				}
			}
		} else {
			System.out.println("Don't forget your image dimensions");
		}
	}
	public static BufferedImage getImageFromArray(int width, int height) throws Exception{
		return getImageFromArray(width,height,false,"");
	}
	
	public static BufferedImage getImageFromArray(int width, int height, boolean writeToFile, String name) throws Exception{
		try {
			image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);			
			for(int w = 0; w < width; w++) {
				for(int h = 0; h < height; h++) {					
					image.setRGB(w,h, getNewColorRGB(w, h));
				}
			}
			
			if(writeToFile) {
				File outputfile = new File(name);
				ImageIO.write(image, "png", outputfile);
			}
			
			return image;
		} catch (Exception ex) {
			System.err.println("You done fucked up ");
			throw ex; 	
		}
	}	
	
	private static Color averageColor(int w, int h, BufferedImage image) {
		int[] rgb = new int[4];
		
		int rangeI = w;
		int rangeJ = h;
		if(rangeI > ROUNDRANGE) rangeI = ROUNDRANGE;
		if(rangeJ > ROUNDRANGE) rangeJ = ROUNDRANGE;		
		
		for(int i = 0; i <= rangeI; i++) {
			for(int j = 0; j <= rangeJ; j++)
				addColor(rgb,w-i,h-j,image);
		}		
		
		if(rgb[3] <= 0) {return new Color((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255));} 
		return new Color(rgb[0]/rgb[3], rgb[1]/rgb[3], rgb[2]/rgb[3]);
	}
	
	private static void addColor(int[] rgb, int w, int h, BufferedImage image) {
		if(image.getRGB(w,h) != BLACK){
			Color temp = new Color(image.getRGB(w,h));
			rgb[0] += temp.getRed();
			rgb[1] += temp.getGreen();
			rgb[2] += temp.getBlue();
			rgb[3]++;
		}
	}
	
	public static void fillImageRandomOrder(int width, int height, String name) throws Exception {
		try {
			image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);	
			initializeCoordinates(width, height);
			
			while(coordinates.size() > 0) {
				int i = (int)(Math.random()*coordinates.size());
				int w = coordinates.get(i).getX();
				int h = coordinates.get(i).getY();
				image.setRGB(w,h, getNewColorRGB(w, h, width, height, true));
				coordinates.remove(i);
			}
			
			File outputfile = new File(name);
			ImageIO.write(image, "png", outputfile);
		} catch (Exception ex) {
			System.err.println("You done fucked up ");
			throw ex; 	
		}
	}
	
	public static void initializeCoordinates(int width, int height) {
		coordinates = new ArrayList<Coordinate> ();
		
		for(int w = 0; w < width; w++) {
			for(int h = 0; h < height; h++) {
				coordinates.add(new Coordinate(w,h));
			}
		}		
	}
	
	static class Coordinate{
		private int x;
		private int y;
		
		public Coordinate(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public void setX(int x) { this.x = x; }
		
		public void setY(int y) { this.y = y; }
		
		public int getX() { return x; }
		
		public int getY() { return y; }
	}
	
	private static int getNewColorRGB(int w, int h) {
		return getNewColorRGB(w,h,0,0,false);
	} 
	
	private static int getNewColorRGB(int w, int h, int widthImage, int heightImage, boolean allAround) {
		Color averageColor;
		if(allAround) {
			averageColor = averageColorAllAround(w,h, widthImage, heightImage, image);
		} else {
			averageColor = averageColor(w,h,image);			
		}
		
		if(previousImage != null) {
			Color prevImgColor = averageColorAllAround(w,h,widthImage,heightImage,previousImage);
			averageColor = new Color((averageColor.getRed() + prevImgColor.getRed())/2, (averageColor.getGreen() + prevImgColor.getGreen())/2, (averageColor.getBlue() + prevImgColor.getBlue())/2);
		}
		
		int red = averageColor.getRed() + (int)(Math.random()*2*(RANGE+1))-RANGE;
		int green = averageColor.getGreen() + (int)(Math.random()*2*(RANGE+1))-RANGE;
		int blue = averageColor.getBlue() + (int)(Math.random()*2*(RANGE+1))-RANGE;
		if(red > 255)red = 255; else if (red < 0) red = 0;
		if(green > 255)green = 255; else if (green < 0) green = 0;
		if(blue > 255)blue = 255; else if (blue < 0) blue = 0;
		 
		return new Color(red,green,blue).getRGB();
	}
	
	private static Color averageColorAllAround(int w, int h, int widthImage, int heightImage, BufferedImage image) {
		int[] rgb = new int[4];
		
		int rangeI = (widthImage-1) - w;
		int rangeJ = (heightImage-1) - h;
		
		if(rangeI > ROUNDRANGE) rangeI = ROUNDRANGE;
		if(rangeJ > ROUNDRANGE) rangeJ = ROUNDRANGE;		
		
		for(int i = 0; i <= rangeI; i++) {
			for(int j = 0; j <= rangeJ; j++)
				addColor(rgb,w+i,h+j,image);
		}
		
		rangeI = w;
		rangeJ = h;
		if(rangeI > ROUNDRANGE) rangeI = ROUNDRANGE;
		if(rangeJ > ROUNDRANGE) rangeJ = ROUNDRANGE;		
		
		for(int i = 0; i <= rangeI; i++) {
			for(int j = 0; j <= rangeJ; j++)
				addColor(rgb,w-i,h-j,image);
		}		
		
		if(rgb[3] <= 0) {return new Color((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255));} 
		return new Color(rgb[0]/rgb[3], rgb[1]/rgb[3], rgb[2]/rgb[3]);
	}
}
