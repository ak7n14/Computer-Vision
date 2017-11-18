package com.anishkatariya.Tutorial_4;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.openimaj.feature.DoubleFVComparison;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.pixel.statistics.HistogramModel;
import org.openimaj.math.statistics.distribution.MultidimensionalHistogram;

/**
 * Tutorial 4 Question Answer
 * Using Euclidean distances to compare the images shows the two images shows the 2 images that are visually
 * similar as well where as using intersection shows to images which may have most number of common pixels
 * but does not look visually similar
 */
public class App {
	
    public static void main( String[] args ) throws IOException {
    		URL[] imageURLs = new URL[] {
    			   new URL( "http://users.ecs.soton.ac.uk/dpd/projects/openimaj/tutorial/hist1.jpg" ),
    			   new URL( "http://users.ecs.soton.ac.uk/dpd/projects/openimaj/tutorial/hist2.jpg" ), 
    			   new  URL( "http://users.ecs.soton.ac.uk/dpd/projects/openimaj/tutorial/hist3.jpg" ) 
    			};
    		//list for containing histograms
    		List<MultidimensionalHistogram> histograms = new ArrayList<MultidimensionalHistogram>();
    		HistogramModel model = new HistogramModel(4, 4, 4);
    		//creating histograms
    		for( URL u : imageURLs ) {
    		    model.estimateModel(ImageUtilities.readMBF(u));
    		    histograms.add( model.histogram.clone() );
    		}
    		//comparing histograms distances
    		for( int i = 0; i < histograms.size(); i++ ) {
    		    for( int j = i; j < histograms.size(); j++ ) {
    		        double distance = histograms.get(i).compare( histograms.get(j), DoubleFVComparison.EUCLIDEAN );
    		    }
    		}
    		displaySimilarEuclidean(imageURLs,"Euclidean"); //display two most similar images using euclidean distances
    		//displaySimilarEuclidean(imageURLs,"Intersection"); //display two most similar images using intersenction
    }
    public static void displaySimilarEuclidean(URL[] imageURLs,String distanceType) throws IOException {
    	HistogramModel model = new HistogramModel(4, 4, 4);
    	List<MultidimensionalHistogram> histograms = new ArrayList<MultidimensionalHistogram>();
    	HashMap<Integer,MBFImage>images = new HashMap<Integer,MBFImage>();
    	
    	int key=0;//key in hashmap
    	//create a hash map of images and also create histogram of images and add them to a list
    	for( URL u : imageURLs ) {
    			MBFImage image = ImageUtilities.readMBF(u);
    			images.put(key, image);
    			key++;
		    model.estimateModel(image);
		    histograms.add( model.histogram.clone() );
		}
    	double[] distances;//array to contain indexes of image and the distance between them
    	ArrayList<double[]> dataList = new ArrayList<double[]>();//To save the index of the image as well as the distance between them
    	for(int i=0; i<histograms.size() ; i++) {
    		for(int j=0; j<histograms.size(); j++){
    			distances = new double [3];
    			distances[0]=i;//index of 1st image
    			distances[1]=j;//index of 2nd image
    			//Using Euclidean Distance
    			if(distanceType.equals("Euclidean")){
    				
        			distances[2] = histograms.get(i).compare(histograms.get(j), DoubleFVComparison.EUCLIDEAN);
    			}else if(distanceType.equals("Intersection")) {//Using Intersection
    			distances[2] = histograms.get(i).compare(histograms.get(j), DoubleFVComparison.INTERSECTION);
    			}else {
    				System.err.println("Wrong value entered");//Wrong value entered
    				break;
    			}
    			dataList.add(distances);//add the details to the dataList
    		}
    	}
    	//Sort according to last parameter(i.e. Distance)
    	Collections.sort(dataList, new Comparator<double[]>() {
			public int compare(double[] arg0, double[] arg1) {				
				return (Integer) (Double.valueOf(arg0[2]).compareTo(arg1[2]));
			}
    		
    	});
    	
    	for(double[] data: dataList) {
    		//if distance =0 its a mapping of an image to its-self 
    			if(data[2]==0){
    				continue;
    			}
    			else {
    				//Getting the 2 most similar pictures
    				System.out.printf("Distance between most similar pictures is = %f",data[2]);
    				DisplayUtilities.display(images.get((int)data[0]),"Image 1");
    				DisplayUtilities.display(images.get((int)data[1]),"Image 2");
    				break;
    			}
    	}
    	
    	
    	}
}
