package com.anishkatariya.Coursework_2;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.FImage;
import org.openimaj.image.processing.resize.ResizeProcessor;
import org.openimaj.image.processor.SinglebandImageProcessor;

public class MyConvolution implements SinglebandImageProcessor<Float, FImage> {
	private float[][] kernel;

	public MyConvolution(float[][] kernel) {
		this.kernel = kernel;

	}
	@Override
	public void processImage(FImage image) {
		int imageRows = image.height;
		int imageCols = image.width;
		
		int templateRows = kernel.length;
		int templateCols = kernel[0].length;
		
		FImage tempImage = image.newInstance(image.width, image.height);
		tempImage.fill(0f);
		
		int templateRowsHalf = (int)Math.floor(templateRows/2);
		int templateColsHalf = (int)Math.floor(templateCols/2);
		
		for(int x=templateRowsHalf+1;x<(imageCols-templateRowsHalf);x++) {
			for(int y=templateCols+1;y<(imageRows-templateColsHalf);y++) {
				float sum =0;
				for(int i =1;i<templateRows;i++) {
					for(int j = 1;j<templateCols;j++) {
						sum = sum + image.getPixel(x+i-templateRowsHalf-1, y+j-templateColsHalf-1) * kernel[j][i];
					}
						
				}
				
				tempImage.setPixel(x, y, sum);
				
			}
		}
		
		tempImage.normalise();
		image.internalAssign(tempImage);
		image.process((new ResizeProcessor(tempImage.getHeight(),tempImage.getWidth())));
		//displayImage(image);
		
	}
	
	public void displayImage(FImage image) {
		DisplayUtilities.display(image);
	}

	
}