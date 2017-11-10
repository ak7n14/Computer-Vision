package com.anishkatariya.Tutorial_3;

import java.io.File;
import java.io.IOException;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.ColourSpace;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.connectedcomponent.GreyscaleConnectedComponentLabeler;
import org.openimaj.image.pixel.ConnectedComponent;
import org.openimaj.image.processing.convolution.FGaussianConvolve;
import org.openimaj.image.typography.hershey.HersheyFont;
import org.openimaj.ml.clustering.FloatCentroidsResult;
import org.openimaj.ml.clustering.assignment.HardAssigner;
import org.openimaj.ml.clustering.kmeans.FloatKMeans;

import java.util.List;
import cern.colt.Arrays;

/**
 * OpenIMAJ Hello world!
 *
 */
public class App {
    public static void main( String[] args ) throws IOException {
    		MBFImage input = ImageUtilities.readMBF(new File("data/cat.bmp"));
    
    		input = ColourSpace.convert(input, ColourSpace.CIE_Lab);
    		FloatKMeans cluster = FloatKMeans.createExact(2);
   
    		float[][] imageData = input.getPixelVectorNative(new float[input.getWidth()*input.getHeight()][3]);
    		FloatCentroidsResult result = cluster.cluster(imageData);
   
    		float[][] centroids = result.centroids;
    		for (float[] fs:centroids) {
    			System.out.println(Arrays.toString(fs));
    		}
    		HardAssigner<float[],?,?> assigner = result.defaultHardAssigner();
    		for (int y=0; y<input.getHeight(); y++) {
    			for (int x=0; x<input.getWidth(); x++) {
    				float[] pixel = input.getPixelNative(x, y);
    				int centroid = assigner.assign(pixel);
    				input.setPixelNative(x, y, centroids[centroid]);
    			}
    		}
    		input = ColourSpace.convert(input, ColourSpace.RGB);
    		DisplayUtilities.display(input);
    		GreyscaleConnectedComponentLabeler labeler = new GreyscaleConnectedComponentLabeler();
    		List<ConnectedComponent> components = labeler.findComponents(input.flatten());
    		int i = 0;
    		for (ConnectedComponent comp : components) {
    		    if (comp.calculateArea() < 50) 
    		        continue;
    		    input.drawText("Point:" + (i++), comp.calculateCentroidPixel(), HersheyFont.TIMES_MEDIUM, 20);
    		}
    		DisplayUtilities.display(input);

    }
    	
}
