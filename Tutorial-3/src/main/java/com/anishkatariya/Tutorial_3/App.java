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
import org.openimaj.image.processor.PixelProcessor;
import org.openimaj.image.segmentation.FelzenszwalbHuttenlocherSegmenter;
import org.openimaj.image.segmentation.SegmentationUtilities;
import org.openimaj.image.typography.hershey.HersheyFont;
import org.openimaj.ml.clustering.FloatCentroidsResult;
import org.openimaj.ml.clustering.assignment.HardAssigner;
import org.openimaj.ml.clustering.kmeans.FloatKMeans;

import java.util.List;
import cern.colt.Arrays;

/**
 * Using Pixel processor took the same amount of time however i believe explicit conversion from 
 * Float to float may take more memory can cause the algorithm to slow down a bit however can not be too significant 
 * Since both implementations used 2 for loops
 *
 */
public class App {
    public static void main( String[] args ) throws IOException {
    		MBFImage input = ImageUtilities.readMBF(new File("data/cat.bmp"));
    
    		input = ColourSpace.convert(input, ColourSpace.CIE_Lab);
    		FloatKMeans cluster = FloatKMeans.createExact(20,50);
   
    		float[][] imageData = input.getPixelVectorNative(new float[input.getWidth()*input.getHeight()][3]);
    		FloatCentroidsResult result = cluster.cluster(imageData);
   
    		final float[][] centroids = result.centroids;
    		for (float[] fs:centroids) {
    			System.out.println(Arrays.toString(fs));
    		}
    		final HardAssigner<float[],?,?> assigner = result.defaultHardAssigner();
    		
    		//============Tutorial Methods===============
 /*
    		for (int y=0; y<input.getHeight(); y++) {
    			for (int x=0; x<input.getWidth(); x++) {
    				float[] pixel = input.getPixelNative(x, y);
    				int centroid = assigner.assign(pixel);
    				input.setPixelNative(x, y, centroids[centroid]);
    			}
    		}
    		input = ColourSpace.convert(input, ColourSpace.RGB);
//    		DisplayUtilities.display(input);
    		GreyscaleConnectedComponentLabeler labeler = new GreyscaleConnectedComponentLabeler();
    		List<ConnectedComponent> components = labeler.findComponents(input.flatten());
    		int i = 0;
    		
    		for (ConnectedComponent comp : components) {
    		    if (comp.calculateArea() < 50) 
    		        continue;
    		    input.drawText("Point:" + (i++), comp.calculateCentroidPixel(), HersheyFont.TIMES_MEDIUM, 20);
    		}
    		*/
    		
 //==================Alternative method(My implementation)===============
    		
    		
    		input.processInplace(new PixelProcessor<Float[]>(){
    			public Float[] processPixel(Float[] pixel) 
    			{
    				float[] temp = new float[pixel.length];
    				for(int i=0; i<pixel.length; i++){
    					temp[i] = pixel[i];
    				}
    				
    				int centroid = assigner.assign(temp);
    				float[] result = centroids[centroid];
    				
    				Float[] temp2 = new Float[result.length];
    				for(int i=0; i<result.length; i++){
    					temp2[i] = result[i];
    				}
    				
    				return temp2;
    			}
        	});
    		//Alternation segmentation algorithm
    		FelzenszwalbHuttenlocherSegmenter<MBFImage> segemntor = new FelzenszwalbHuttenlocherSegmenter<MBFImage>();
        	List<ConnectedComponent> comps = segemntor.segment(input);

        	MBFImage segmented = SegmentationUtilities.renderSegments(input.getWidth(), input.getHeight(), comps);
        	DisplayUtilities.display(segmented);
        	int i=0;
        	for(ConnectedComponent con : comps){
        		if(con.calculateArea() < 500)
        			continue;
        		segmented.drawText("Region " + i++, con.calculateCentroidPixel(), HersheyFont.TIMES_MEDIUM, 20);
        	}  		
        	DisplayUtilities.display(segmented);
        	
    }
    	
}
