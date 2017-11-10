package com.anishkatariya.Coursework_2;

import java.util.ArrayList;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.FImage;
import org.openimaj.image.MBFImage;
import org.openimaj.image.processing.convolution.Gaussian2D;
import org.openimaj.image.processing.resize.ResizeProcessor;

public class HybridImageCreator {
	
	int cutOff1;//Sigma1
	int cutOff2;//Sigma2
	int size; 
	
	//The 2 images
	MBFImage image1;
	MBFImage image2;
	
	//lowpass images of the 2 mbfimages
	MBFImage lowPass1;
	MBFImage lowPass2;
	
	//highpass images of the 2 mbfimages
	MBFImage highPass1;
	MBFImage highPass2;
	
	//the 2 hybrid images formed
	MBFImage hybridImage1;
	MBFImage hybridImage2;
	 
	
	
	public void createHybridImage() {
		
		//getting size as a function of sigma value
		size = (int) (8.0f * cutOff1 + 0.1f);//window is +/- 4 sigmas from the centre of the Gaussian
		if(size % 2==0) {
			size+=1;
		}

		
		//Defining the 2 gaussian 2d templates to convolve with images to create lowpass images
		FImage gaussian1 = Gaussian2D.createKernelImage(size, cutOff1);
		FImage gaussian2 = Gaussian2D.createKernelImage(size,cutOff2);
	
		
		//creating 2 lowpass images by convolving with 2d gaussian matrices
		lowPass1=image1.process(new MyConvolution(gaussian1.pixels));
		lowPass2= image2.process(new MyConvolution(gaussian2.pixels));
		
		//creating highpass images by subtracting lowpass band from them
		highPass1=image1.subtract(lowPass1);
		highPass2=image2.subtract(lowPass2);
		
		//creating 2 hybrid images by adding the highpass bands of one image to the low pass of the other
		hybridImage1= highPass1.add(lowPass2);
		hybridImage2 = highPass2.add(lowPass1);
		
		//Displaying the 2 hybrid images
		displayHybrid(hybridImage1,hybridImage2);
		
	}
	
	//Method for returning and image with all scaled images
	public MBFImage getReSizedImages(){
		//array for scaled images
		ArrayList<MBFImage> scaled = new ArrayList<MBFImage>();
		//creating temporary image for scaling and adding it to array
		MBFImage temporaryImg = hybridImage1;
		scaled.add(temporaryImg);
		
		//adding 4 resized images to array
		for(int i=0; i<5; i++){
			scaled.add(temporaryImg=ResizeProcessor.halfSize(temporaryImg));
		}
		//dimension for adjacent images
		int height = hybridImage1.getHeight(); 
		int width = 0;
		for(MBFImage i : scaled){
			width += i.getWidth();
		}
		
		//create a new image to show the resized images
		MBFImage reSizedImgs = new MBFImage(width, height);
		for(MBFImage j : scaled)
		{
				int imgY = height-j.getHeight();
				int imgX = 0;
				
				for(MBFImage k : scaled){
					if(scaled.indexOf(k) < scaled.indexOf(j)){
						imgX += k.getWidth();
					}
				}
			//drawing the scaled images in one image	
			reSizedImgs.drawImage(j, imgX, imgY);	
		}
		
		//returning the resized images
		return reSizedImgs;
	}
	
	//Displaying the 2 hybrid images created
	public void displayHybrid(MBFImage image,MBFImage image2) {
		DisplayUtilities.display(image);
		DisplayUtilities.display(image2);
	}
	
	//setting value of sigma1
	public void setSigma1(int cutOff1) {
		this.cutOff1 = cutOff1;
	}
	
	//setting value of sigma2
	public void setSigma2(int cutOff2) {
		this.cutOff2 = cutOff2;
	}
	
	//setting image1
	public void setImage1(MBFImage image1) {
		this.image1 = image1;
	}
	
	//setting image2
	public void setImage2(MBFImage image2) {
		this.image2 = image2;
	}
}
