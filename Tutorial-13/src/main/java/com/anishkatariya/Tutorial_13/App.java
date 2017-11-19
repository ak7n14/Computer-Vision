package com.anishkatariya.Tutorial_13;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.openimaj.data.dataset.GroupedDataset;
import org.openimaj.data.dataset.ListDataset;
import org.openimaj.data.dataset.VFSGroupDataset;
import org.openimaj.experiment.dataset.split.GroupedRandomSplitter;
import org.openimaj.experiment.dataset.util.DatasetAdaptors;
import org.openimaj.feature.DoubleFV;
import org.openimaj.feature.DoubleFVComparison;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;

import org.openimaj.image.model.EigenImages;


/**
 * Reducing the training set makes the performance of the algorithm significantly worse
 * Threshold removes unsure guesses however the performance stays more or less the same
 * The algorithm starts performing better with a threshold near 10
 */
public class App {
    public static void main( String[] args ) throws Exception {
    	//importing data set
    	VFSGroupDataset<FImage> dataset = 
    		    new VFSGroupDataset<FImage>("zip:http://datasets.openimaj.org/att_faces.zip", ImageUtilities.FIMAGE_READER);
    	
    	int nTraining = 5;//no of examples for training
    	int nTesting = 5;//no of examples for testing
    	
    	//splitting into training and testing satasets
    	GroupedRandomSplitter<String, FImage> splits = 
    	    new GroupedRandomSplitter<String, FImage>(dataset, nTraining, 0, nTesting);
    	GroupedDataset<String, ListDataset<FImage>, FImage> training = splits.getTrainingDataset();
    	GroupedDataset<String, ListDataset<FImage>, FImage> testing = splits.getTestDataset();
   
    	//training image to learn PCA basis
    	List<FImage> basisImages = DatasetAdaptors.asList(training);
    	int nEigenvectors = 100;
    	EigenImages eigen = new EigenImages(nEigenvectors);
    	eigen.train(basisImages);
    	
    	//displaying eigen faces
    	List<FImage> eigenFaces = new ArrayList<FImage>();
    	for (int i = 0; i < 12; i++) {
    	    eigenFaces.add(eigen.visualisePC(i));
    	}
    	DisplayUtilities.display("EigenFaces", eigenFaces);
    	
    	
    	//building database of features from taining images
    	Map<String, DoubleFV[]> features = new HashMap<String, DoubleFV[]>();
    	for (final String person : training.getGroups()) {
    	    final DoubleFV[] fvs = new DoubleFV[nTraining];

    	    for (int i = 0; i < nTraining; i++) {
    	        final FImage face = training.get(person).get(i);
    	        fvs[i] = eigen.extractFeature(face);
    	    }
    	    features.put(person, fvs);
    	    
    	    
    	}
    	evaluate(testing,features,eigen);
	reconstructFace(training,features,eigen);
    }
  //evaluation with test images
    	public  static void evaluate( GroupedDataset<String, ListDataset<FImage>, FImage> testing,Map<String, DoubleFV[]> features,EigenImages eigen) {
    	double correct = 0, incorrect = 0;
    	double threshold =5;
    	for (String truePerson : testing.getGroups()) {
    	    for (FImage face : testing.get(truePerson)) {
    	        DoubleFV testFeature = eigen.extractFeature(face);

    	        String bestPerson = null;
    	        double minDistance = Double.MAX_VALUE;
    	        for (final String person : features.keySet()) {
    	            for (final DoubleFV fv : features.get(person)) {
    	                double distance = fv.compare(testFeature, DoubleFVComparison.EUCLIDEAN);
    	                if(minDistance>threshold) {
    	                		bestPerson="Cannot be determined";
    	                }else {
    	                		if (distance < minDistance) {
    	                			minDistance = distance;
    	                			bestPerson = person;
    	                		}
    	                }
    	                
    	            }
    	                
    	        }

    	        System.out.println("Actual: " + truePerson + "\tguess: " + bestPerson);

    	        if (truePerson.equals(bestPerson))
    	            correct++;
    	        else
    	            incorrect++;
    	    }
    	}

    	System.out.println("Accuracy: " + (correct / (correct + incorrect)));
    	}
    
    
    public static void reconstructFace(GroupedDataset<String, ListDataset<FImage>, FImage> training,Map<String, DoubleFV[]> features,	EigenImages eigen)
    {   
    	//names of all people
    	List<String> names = new ArrayList<String>(training.getGroups());
    	
    	//random person
    	String person = names.get((int) Math.random()*names.size());
    	
    	//display a random of the person
    	FImage real = training.getRandomInstance(person);
    	ArrayList<FImage> image = new ArrayList<FImage>();
    	image.add(real);

    	//array of weight vectors 
    	DoubleFV[] extendedFeatures = features.get(person);
    	
    	FImage reconstruction=null;
    	for(int i=0; i<extendedFeatures.length; i++)
    	{
    		reconstruction = eigen.reconstruct(extendedFeatures[i]).normalise();
    	}
    	
    	image.add(reconstruction);
    	DisplayUtilities.display(person, image);
    	
    }
}
