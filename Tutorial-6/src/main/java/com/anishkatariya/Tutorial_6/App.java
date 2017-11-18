package com.anishkatariya.Tutorial_6;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.vfs2.FileSystemException;
import org.openimaj.data.dataset.MapBackedDataset;
import org.openimaj.data.dataset.VFSGroupDataset;
import org.openimaj.data.dataset.VFSListDataset;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.dataset.FlickrImageDataset;
import org.openimaj.util.api.auth.DefaultTokenFactory;
import org.openimaj.util.api.auth.common.FlickrAPIToken;
/**
 * Exercise 3 and 4 were not done due to unavailability of a credit card
 *
 */
public class App {
    public static void main( String[] args ) throws Exception {
    	VFSListDataset<FImage> images = new VFSListDataset<FImage>("/Users/anishkatariya/Desktop/Computer-Vision/Tutorial-6/data", ImageUtilities.FIMAGE_READER);
//    	System.out.println(images.size());
//    	DisplayUtilities.display(images.getRandomInstance(), "A random image from the dataset");
//    	DisplayUtilities.display("My images", images);
    	
    	
    	//loading dataset as a list
    	VFSListDataset<FImage> faces = 
    			new VFSListDataset<FImage>("zip:http://datasets.openimaj.org/att_faces.zip", ImageUtilities.FIMAGE_READER);
//    	DisplayUtilities.display("ATT faces", faces);
    	
    	
    	//loading dataset as a groupdataset
    	VFSGroupDataset<FImage> groupedFaces = 
    			new VFSGroupDataset<FImage>( "zip:http://datasets.openimaj.org/att_faces.zip", ImageUtilities.FIMAGE_READER);
    
    	//displaying each group  
//    for (final Entry<String, VFSListDataset<FImage>> entry : groupedFaces.entrySet()) {
//    	DisplayUtilities.display(entry.getKey(), entry.getValue());
//    }
    
    //	getting cat dataset from flikr
//    FlickrAPIToken flickrToken = DefaultTokenFactory.get(FlickrAPIToken.class);
//    FlickrImageDataset<FImage> cats = 
//    		FlickrImageDataset.create(ImageUtilities.FIMAGE_READER, flickrToken, "cat", 10);
//    DisplayUtilities.display("Cats", cats);
    	
    	
    	displayRandom(groupedFaces);
    	
    }
    
    //method to display a random image from each set
    public static void displayRandom(VFSGroupDataset<FImage> groupedFaces) throws Exception {
    		ArrayList<FImage> random = new ArrayList<FImage>();
    		for (Entry<String, VFSListDataset<FImage>> entries : groupedFaces.entrySet()){
    	    		random.add(entries.getValue().getRandomInstance());
    	    	}
    	    	DisplayUtilities.display("Random" , random);
    	}	
    
}
