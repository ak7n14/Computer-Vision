package com.anishkatariya.Coursework_2;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.FImage;
import org.openimaj.image.MBFImage;
import org.openimaj.image.processing.convolution.FConvolution;
import org.openimaj.image.processing.convolution.Gaussian2D;

public class HybridImageCreator {
	
	int cutOff1;
	int cutOff2;
	int size;
	
	MBFImage image1;
	MBFImage image2;
	
	MBFImage lowPass1;
	MBFImage lowPass2;
	
	MBFImage highPass1;
	MBFImage highPass2;
	
	MBFImage hybridImage1;
	MBFImage hybridImage2;
	 
	
	
	public MBFImage createHybridImage() {
		size = (int) (8.0f * cutOff1 + 0.1f);
		if(size % 2==0) {
			size+=1;
		}

		
		FImage gaussian1 = Gaussian2D.createKernelImage(size, cutOff1);
		FImage gaussian2 = Gaussian2D.createKernelImage(size,cutOff2);
	

		lowPass1=image1.process(new MyConvolution(gaussian1.pixels));
		lowPass2= image2.process(new MyConvolution(gaussian2.pixels));
//		lowPass1=image1.process(new FConvolution(gaussian1.pixels));
//		lowPass2= image2.process(new FConvolution(gaussian2.pixels));
		
		highPass1=image1.subtract(lowPass1);
		highPass2=image2.subtract(lowPass2);
		
		hybridImage1= highPass1.add(lowPass2);
		hybridImage2 = highPass2.add(lowPass1);
		displayHybrid(hybridImage1,hybridImage2);
		
		return hybridImage1;
		
	}
	public void displayHybrid(MBFImage image,MBFImage image2) {
		DisplayUtilities.display(image);
		DisplayUtilities.display(image2);
	}
	
	public void setCutOff1(int cutOff1) {
		this.cutOff1 = cutOff1;
	}
	
	public void setCutOff2(int cutOff2) {
		this.cutOff2 = cutOff2;
	}
	
	public void setImage1(MBFImage image1) {
		this.image1 = image1;
	}
	
	public void setImage2(MBFImage image2) {
		this.image2 = image2;
	}
}
