package com.anishkatariya.Tutorial_7;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.ColourSpace;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.processing.convolution.FFastGaussianConvolve;
import org.openimaj.image.processing.convolution.FGaussianConvolve;
import org.openimaj.image.processing.edges.CannyEdgeDetector;
import org.openimaj.image.processing.edges.*;
import org.openimaj.image.processing.edges.*;
import org.openimaj.image.typography.hershey.HersheyFont;
import org.openimaj.video.*;
import org.openimaj.video.capture.VideoCapture;
import org.openimaj.video.xuggle.XuggleVideo;


/**
 * OpenIMAJ Hello world!
 *
 */
public class App {
    public static void main( String[] args ) throws IOException {
    	Video<MBFImage> video;
    	video = new XuggleVideo(new File("Data/keyboardcat.flv"));
   	//video = new VideoCapture(320, 240);
//    	VideoDisplay<MBFImage> display = VideoDisplay.createVideoDisplay(video);
  	
//    	for (MBFImage mbfImage : video) {
//    	    DisplayUtilities.displayName(mbfImage.process(new CannyEdgeDetector()), "videoFrames");
//    	}
    	   
    

    	VideoDisplay<MBFImage> display1 = VideoDisplay.createVideoDisplay(video);
    	display1.addVideoListener(
    	  new VideoDisplayListener<MBFImage>() {
    	    @SuppressWarnings("deprecation")
			public void beforeUpdate(MBFImage frame) {
    	        frame.processInplace(new FGaussianConvolve(4,8));
    	    }

    	    public void afterUpdate(VideoDisplay<MBFImage> display1) {
    	    }
    	  });
    }
}
