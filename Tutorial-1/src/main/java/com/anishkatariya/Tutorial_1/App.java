package com.anishkatariya.Tutorial_1;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.ColourSpace;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.processing.convolution.FGaussianConvolve;
import org.openimaj.image.typography.hershey.HersheyFont;

public class App {
    public static void main( String[] args ) {
    	//Create an image
        MBFImage image = new MBFImage(320,70, ColourSpace.RGB);

        //Fill the image with white
        image.fill(RGBColour.WHITE);
        		        
        //Render some test into the image
        image.drawText("Welcome to Comp3207", 10, 60, HersheyFont.TIMES_BOLD_ITALIC, 22, RGBColour.BLUE);

        //Apply a Gaussian blur
        image.processInplace(new FGaussianConvolve(2f));
        
        //Display the image
        DisplayUtilities.display(image);
    }
}
