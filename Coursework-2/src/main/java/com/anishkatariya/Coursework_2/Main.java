package com.anishkatariya.Coursework_2;

import java.io.File;
import java.io.IOException;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.processing.convolution.FConvolution;
import org.openimaj.image.processing.convolution.Gaussian2D;
import org.openimaj.*;

public class Main {
	public static void main(String[] args) throws IOException {
		MBFImage image2 = ImageUtilities.readMBF(new File ("data/submarine.bmp"));
		int cutOff1 =2;
		int cutOff2 =1;
		int size;
		size = (int) (8.0f * cutOff1 + 0.1f);
		if(size % 2==0) {
			size+=1;
		}
		MBFImage image1 = ImageUtilities.readMBF(new File ("data/fish.bmp"));
		
		HybridImageCreator hb = new HybridImageCreator();
		hb.setCutOff1(cutOff1);
		hb.setCutOff2(cutOff2);
		hb.setImage1(image1);
		hb.setImage2(image2);
		hb.createHybridImage();
		//FImage gauss1 = Gaussian2D.createKernelImage(size,cutOff1);
		//image.process(new MyConvolution(gauss1.pixels));
		//DisplayUtilities.display(image1.process(new MyConvolution(gauss1.pixels)));
		//DisplayUtilities.display(image1);

		
	}
}
