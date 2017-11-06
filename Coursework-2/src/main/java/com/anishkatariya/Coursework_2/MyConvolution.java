package com.anishkatariya.Coursework_2;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.FImage;
import org.openimaj.image.MBFImage;
import org.openimaj.image.processor.SinglebandImageProcessor;

public class MyConvolution implements SinglebandImageProcessor<Float, FImage> {
	private float[][] kernel;

	public MyConvolution(float[][] kernel) {
		this.kernel = kernel;
	}
	@Override
	public void processImage(FImage image) {
		int imageRows= image.getRows();
		int imageCols= image.getCols();
		
		int templateRows= kernel.length;
		int templateCols = kernel[0].length;
		
		int templateRowsHalf= (int) Math.floor(templateRows/2);
		int templateColsHalf = (int) Math.floor(templateCols/2);
		
		FImage templateImage = new FImage(imageRows,imageCols);
		templateImage.fill(0F);
		
		for(int x= templateRowsHalf +1;x<imageCols-templateRowsHalf;x++) {
			for(int y = templateColsHalf+1;y<imageRows-templateColsHalf;y++) {
				float sum =0;
				for(int i=1;i<templateRows;i++) {
					for(int j=0;j<templateCols;j++) {
						sum = sum + image.getPixel(x+i-templateRowsHalf-1, y+j-templateColsHalf-1) * kernel[j][i];
					}
				}
				templateImage.setPixel(x, y, sum);
			}
		}
		
		templateImage = templateImage.normalise();
		image.internalAssign(templateImage);
		displayImage(image);
		
	}
	
	public void displayImage(FImage image) {
		DisplayUtilities.display(image);
	}

	
}