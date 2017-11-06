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
		MBFImage image = ImageUtilities.readMBF(new File ("data/cat.bmp"));
		FImage gauss1 = Gaussian2D.createKernelImage(5,5);
		image.process(new MyConvolution(gauss1.pixels));
//		DisplayUtilities.display(image);
//		image.process(new FConvolution(gauss1));
		DisplayUtilities.display(image.process(new FConvolution(gauss1.pixels)));

		
	}
}
