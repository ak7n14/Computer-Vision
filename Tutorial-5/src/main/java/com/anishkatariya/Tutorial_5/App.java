package com.anishkatariya.Tutorial_5;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.openimaj.feature.local.list.LocalFeatureList;
import org.openimaj.feature.local.matcher.BasicMatcher;
import org.openimaj.feature.local.matcher.BasicTwoWayMatcher;
import org.openimaj.feature.local.matcher.FastBasicKeypointMatcher;
import org.openimaj.feature.local.matcher.LocalFeatureMatcher;
import org.openimaj.feature.local.matcher.MatchingUtilities;
import org.openimaj.feature.local.matcher.VotingKeypointMatcher;
import org.openimaj.feature.local.matcher.consistent.ConsistentLocalFeatureMatcher2d;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.ColourSpace;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.feature.local.engine.DoGSIFTEngine;
import org.openimaj.image.feature.local.keypoints.Keypoint;
import org.openimaj.image.processing.convolution.FGaussianConvolve;
import org.openimaj.image.typography.hershey.HersheyFont;
import org.openimaj.math.geometry.transforms.HomographyRefinement;
import org.openimaj.math.geometry.transforms.estimation.RobustAffineTransformEstimator;
import org.openimaj.math.geometry.transforms.estimation.RobustHomographyEstimator;
import org.openimaj.math.model.fit.RANSAC;

/**
 *Consistent matching using RANSAC and homography model gave me the best matches 
 *and drew polygon very accurately, 
 *voting keypoint algorithm showed too many mismatches even
 *for relatively low thresholds
 */
public class App {
    public static void main( String[] args ) throws MalformedURLException, IOException {
    	
    	MBFImage query = ImageUtilities.readMBF(new URL("http://static.openimaj.org/media/tutorial/query.jpg"));
    	MBFImage target = ImageUtilities.readMBF(new URL("http://static.openimaj.org/media/tutorial/target.jpg"));
    	
    	DoGSIFTEngine engine = new DoGSIFTEngine();	
    	LocalFeatureList<Keypoint> queryKeypoints = engine.findFeatures(query.flatten());
    	LocalFeatureList<Keypoint> targetKeypoints = engine.findFeatures(target.flatten());
    	//basicMatch(query,queryKeypoints,target,targetKeypoints);	//carrying out a basic match between the 2 features
    //conistentMatchAffine(query,queryKeypoints,target,targetKeypoints);//carrying out a consistent match using implementation of RANSAC
    //twoWayMatch(query,queryKeypoints,target,targetKeypoints); //Experimenting with a two way match
    //votingKeypointMatch(query,queryKeypoints,target,targetKeypoints); //Experimenting with a two way match
    	conistentMatchHomography(query,queryKeypoints,target,targetKeypoints);
    	
    }
    //defines a basic match between the 2 images
   public static void basicMatch(MBFImage query, LocalFeatureList<Keypoint> queryKeypoints, MBFImage target, LocalFeatureList<Keypoint> targetKeypoints) {
    	LocalFeatureMatcher<Keypoint> matcher = new BasicMatcher<Keypoint>(80);
    	matcher.setModelFeatures(queryKeypoints);//extracting features
    	matcher.findMatches(targetKeypoints);//matching to target image
    	
    	
    	MBFImage basicMatches = MatchingUtilities.drawMatches(query, target, matcher.getMatches(), RGBColour.RED);
    	DisplayUtilities.display(basicMatches);
   }
   //defines a consistent matching using implementation of ransac algorithm and affine transform estimator
   public static void conistentMatchAffine(MBFImage query, LocalFeatureList<Keypoint> queryKeypoints, MBFImage target, LocalFeatureList<Keypoint> targetKeypoints) {
	   LocalFeatureMatcher<Keypoint> matcher;
	   //definind a ransac estimator
	   RobustAffineTransformEstimator modelFitter = new RobustAffineTransformEstimator(5.0, 1500,
 			  new RANSAC.PercentageInliersStoppingCondition(0.5)); 
 			matcher = new ConsistentLocalFeatureMatcher2d<Keypoint>(
 			  new FastBasicKeypointMatcher<Keypoint>(8), modelFitter);

 			matcher.setModelFeatures(queryKeypoints);//extracing features from image
 			matcher.findMatches(targetKeypoints);//matching features
 			//drawing matches
 			MBFImage consistentMatches = MatchingUtilities.drawMatches(query, target, matcher.getMatches(), 
 			  RGBColour.RED);

 	DisplayUtilities.display(consistentMatches);
 	//trawing a polygon around the approximate shape of the book		
 	target.drawShape(
		  query.getBounds().transform(modelFitter.getModel().getTransform().inverse()), 3,RGBColour.BLUE);
  	  DisplayUtilities.display(target);
   }
   //implementing a 2way match
   public static void twoWayMatch(MBFImage query, LocalFeatureList<Keypoint> queryKeypoints, MBFImage target, LocalFeatureList<Keypoint> targetKeypoints) {
	LocalFeatureMatcher<Keypoint> matcher = new BasicTwoWayMatcher<Keypoint>();
   	matcher.setModelFeatures(queryKeypoints);
   	matcher.findMatches(targetKeypoints);	
   	MBFImage twoWayMatches = MatchingUtilities.drawMatches(query, target, matcher.getMatches(), RGBColour.RED);
   	DisplayUtilities.display(twoWayMatches);
   }
   //implementing voting keypoint matching with threshold 100
   public static void votingKeypointMatch(MBFImage query, LocalFeatureList<Keypoint> queryKeypoints, MBFImage target, LocalFeatureList<Keypoint> targetKeypoints) {
	  LocalFeatureMatcher<Keypoint> matcher = new VotingKeypointMatcher<Keypoint>(10);
	  matcher.setModelFeatures(queryKeypoints);
	  matcher.findMatches(targetKeypoints);	
	  MBFImage twoWayMatches = MatchingUtilities.drawMatches(query, target, matcher.getMatches(), RGBColour.RED);
	  DisplayUtilities.display(twoWayMatches);
   }
   //Ransac with homography estimator and least meadian of squares fitting
   public static void conistentMatchHomography(MBFImage query, LocalFeatureList<Keypoint> queryKeypoints, MBFImage target, LocalFeatureList<Keypoint> targetKeypoints) {
	   LocalFeatureMatcher<Keypoint> matcher = null;
	   //definind a ransac estimator
	   RobustHomographyEstimator homographyModelFitter = new RobustHomographyEstimator(5.0, 1500, 
  			 new RANSAC.PercentageInliersStoppingCondition(0.5), HomographyRefinement.SYMMETRIC_TRANSFER);
	   matcher = new ConsistentLocalFeatureMatcher2d<Keypoint>(
	 			  new FastBasicKeypointMatcher<Keypoint>(8), homographyModelFitter);
 	   matcher.setModelFeatures(queryKeypoints);//extracing features from image
 	   matcher.findMatches(targetKeypoints);//matching features
 	 //drawing matches
 	   MBFImage consistentMatches = MatchingUtilities.drawMatches(query, target, matcher.getMatches(), 
 			  RGBColour.RED);

 	DisplayUtilities.display(consistentMatches);
 	//trawing a polygon around the approximate shape of the book		
 	target.drawShape(
		  query.getBounds().transform(homographyModelFitter.getModel().getTransform().inverse()), 3,RGBColour.BLUE);
  	  DisplayUtilities.display(target);
   }
}
