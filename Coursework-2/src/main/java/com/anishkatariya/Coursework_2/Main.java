package com.anishkatariya.Coursework_2;

import java.io.File;
import java.io.IOException;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.processing.convolution.Gaussian2D;
import org.openimaj.image.processing.resize.ResizeProcessor;

public class Main {
	public static void main(String[] args) throws IOException {

	//DisplayGUI gui = new DisplayGUI();	//Creating the diplay GUI
		MBFImage image1 = null,image2 = null;
		
		try {
			image1 = ImageUtilities.readMBF(new File ("data/img1.png"));
			image2 = ImageUtilities.readMBF(new File ("data/img2.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		HybridImageCreator hb = new HybridImageCreator();
		hb.setSigma1(2);
		hb.setSigma2(2);
		image1.process(new ResizeProcessor(image2.getWidth(),image2.getHeight()));
		hb.setImage1(image1);
		hb.setImage2(image2);
		hb.displayHybrid(hb.createHybridImage());
		DisplayUtilities.display(hb.getReSizedImages());
	}
}
