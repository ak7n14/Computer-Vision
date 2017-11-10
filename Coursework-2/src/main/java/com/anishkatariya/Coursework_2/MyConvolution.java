package com.anishkatariya.Coursework_2;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.FImage;
import org.openimaj.image.processing.resize.ResizeProcessor;
import org.openimaj.image.processor.SinglebandImageProcessor;

public class MyConvolution implements SinglebandImageProcessor<Float, FImage> {
	private float[][] kernel;

	public MyConvolution(float[][] kernel) {
		this.kernel = kernel;//template

	}
	@Override
	public void processImage(FImage image) {
		//Getting image dimensions
		int imageRows = image.height;
		int imageCols = image.width;
		
		//Getting template dimensions
		int templateRows = kernel.length;
		int templateCols = kernel[0].length;
		
		//Creating a temporary image to manipulate
		FImage tempImage = image.newInstance(image.width, image.height);
		
		//Calculating half of the templates dimensions and rounding them off.
		int templateRowsHalf = (int)Math.floor(templateRows/2);
		int templateColsHalf = (int)Math.floor(templateCols/2);
		
		for(int x=templateRowsHalf+1;x<(imageCols-templateRowsHalf);x++) { //Manipulating all columns except the border
			for(int y=templateCols+1;y<(imageRows-templateColsHalf);y++) { //Manipulating all rows except the border
				float sum =0;//Setting sum to be 0 initially
				for(int i =1;i<templateRows;i++) { //for every template columns
					for(int j = 1;j<templateCols;j++) { //for every template rows
						//Finding the pixel value by accumulating sum and multiplying by template value at that point
						sum = sum + image.getPixel(x+i-templateRowsHalf-1, y+j-templateColsHalf-1) * kernel[j][i];
					}
						
				}
				
				tempImage.setPixel(x, y, sum);//setting the pixel at (x,y) as the accumulated sum
				
			}
		}
		
		tempImage.normalise();//Normalising the convolved image(pixels in range 0-1)
		//setting the values of the pixels of the image to that of the temporary image
		image.internalAssign(tempImage);	
	}
	//Method to display the image after convolution 
	public void displayImage(FImage image) {
		DisplayUtilities.display(image);
	}

	
}