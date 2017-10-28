package com.anishkatariya.Tutorial_2;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.ColourSpace;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.processing.convolution.FGaussianConvolve;
import org.openimaj.image.processing.edges.CannyEdgeDetector;
import org.openimaj.image.typography.hershey.HersheyFont;
import org.openimaj.math.geometry.shape.Ellipse;

/**
 * OpenIMAJ Hello world!
 *
 */
public class App {
    public static void main( String[] args ) {
    	//Create an image
        try {
			MBFImage image = ImageUtilities.readMBF(new File ("Data/openImaj.jpg"));
			DisplayUtilities.createNamedWindow("Display");
			//Printing Colour space field
			System.out.println(image.colourSpace);
			
			//Displaying the red Channel
			DisplayUtilities.displayName(image,"Display");
			DisplayUtilities.displayName(image.getBand(0),"Display");
			
			//Getting pixel array and changing the blue and green pixels to back to the clone of the image
			
			MBFImage clone = image.clone();
			for (int y=0; y<image.getHeight(); y++) {
			    for(int x=0; x<image.getWidth(); x++) {
			        clone.getBand(1).pixels[y][x] = 0;
			        clone.getBand(2).pixels[y][x] = 0;
			    }
			}
			DisplayUtilities.displayName(clone,"Display");
			
			//Alternative method
			
			clone.getBand(1).fill(0f);
			clone.getBand(2).fill(0f);
			DisplayUtilities.displayName(clone,"Display");
			//Using Canny Edge detection Algorithm
			image.processInplace(new CannyEdgeDetector());
			
			DisplayUtilities.displayName(image,"Display");
			//Displaying Text bubble aroung image with borders
			image.drawShapeFilled(new Ellipse(700f, 450f, 20f, 10f, 0f), RGBColour.WHITE);
			image.drawShape(new Ellipse(700, 450, 20, 10, 0f), 5, RGBColour.GREEN);
			image.drawShapeFilled(new Ellipse(650f, 425f, 25f, 12f, 0f), RGBColour.WHITE);
			image.drawShape(new Ellipse(650, 425, 25, 12, 0f), 5, RGBColour.GREEN);
			image.drawShapeFilled(new Ellipse(600f, 380f, 30f, 15f, 0f), RGBColour.WHITE);
			image.drawShape(new Ellipse(600, 380, 30, 15, 0f), RGBColour.GREEN);
			image.drawShapeFilled(new Ellipse(500f, 300f, 100f, 70f, 0f), RGBColour.WHITE);
			image.drawShape(new Ellipse(500, 300, 100, 70, 0f), RGBColour.GREEN);
			image.drawText("OpenIMAJ is", 425, 300, HersheyFont.ASTROLOGY, 20, RGBColour.BLACK);
			image.drawText("Awesome", 425, 330, HersheyFont.ASTROLOGY, 20, RGBColour.BLACK);
			DisplayUtilities.displayName(image,"Display");
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
    }
}
