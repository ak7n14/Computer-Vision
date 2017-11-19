package com.anishkatariya.Tutorial_14;
import java.util.ArrayList;
import java.util.List;

import org.openimaj.data.dataset.GroupedDataset;
import org.openimaj.data.dataset.ListDataset;
import org.openimaj.data.dataset.VFSGroupDataset;
import org.openimaj.experiment.dataset.sampling.GroupSampler;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.annotation.evaluation.datasets.Caltech101;
import org.openimaj.image.colour.ColourSpace;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.processing.resize.ResizeProcessor;
import org.openimaj.time.Timer;
import org.openimaj.util.function.Operation;
import org.openimaj.util.parallel.Parallel;
import org.openimaj.util.parallel.partition.RangePartitioner;

/**
 * following out the results of using different parallelism implementation for computing normalised averages
 * Unparallel Implementation took 18671ms and hence is very slow
 * inner loop parallel implementation took 10951ms (gives better speed up than outer loop parallelism)
 * outer loop parallel implementation took 11888ms (gave the worst result out of all multi-threaded implementations) 
 * both loops parallel implementation took 7486ms  (fastest but the process of retrieving image makes the overall performance slower)
 */
public class App {
    public static void main( String[] args ) throws Exception {
    	
    		//parallelPrint();
    	
    		//importing caltech101 dataset
    		VFSGroupDataset<MBFImage> allImages = Caltech101.getImages(ImageUtilities.MBFIMAGE_READER);
    		//using the 1st 8 groups
    		GroupedDataset<String, ListDataset<MBFImage>, MBFImage> images = GroupSampler.sample(allImages, 8, false);
    		//unparallelAverageImg(allImages,images);//unparallel implementation
    		//parallelAverageImage(allImages,images); //(Inner loop Parallel)
    		//partitionedParallelAverageImage(allImages,images);s //(Both loop parallel)
    		parallelOut(allImages,images); //(Outerloop parallel)
    		
   }
    
    //Printing 0-9 using separate threads
    public static void parallelPrint() {
    		Parallel.forIndex(0, 10, 1, new Operation<Integer>() {
    			public void perform(Integer i) {
				System.out.println(i);
			}
		});
    }
	// building average image for each group(Unparallel)
    public static void unparallelAverageImg(VFSGroupDataset<MBFImage> allImages,GroupedDataset<String, ListDataset<MBFImage>, MBFImage> images) {
    
		List<MBFImage> output = new ArrayList<MBFImage>();
		ResizeProcessor resize = new ResizeProcessor(200);
		//resampling and normalizing each image in the group and drawing in center of white image
		Timer t1 = Timer.timer();
		for (ListDataset<MBFImage> clzImages : images.values()) {
		    MBFImage current = new MBFImage(200, 200, ColourSpace.RGB);

		    for (MBFImage i : clzImages) {
		        MBFImage tmp = new MBFImage(200, 200, ColourSpace.RGB);
		        tmp.fill(RGBColour.WHITE);

		        MBFImage small = i.process(resize).normalise();
		        int x = (200 - small.getWidth()) / 2;
		        int y = (200 - small.getHeight()) / 2;
		        tmp.drawImage(small, x, y);

		        current.addInplace(tmp);
		    }
		    current.divideInplace((float) clzImages.size());
		    output.add(current);    
		}
		System.out.println("Time: " + t1.duration() + "ms");
		DisplayUtilities.display("Images", output);
    }
    
    // building average image for each group(inner loop parallel)
    public static void parallelAverageImage(VFSGroupDataset<MBFImage> allImages,GroupedDataset<String, ListDataset<MBFImage>, MBFImage> images) {
    	List<MBFImage> output = new ArrayList<MBFImage>();
	final ResizeProcessor resize = new ResizeProcessor(200);
	//resampling and normalizing each image in the group and drawing in center of white image
	Timer t1 = Timer.timer();
		for (ListDataset<MBFImage> clzImages : images.values()) {
		    final MBFImage current = new MBFImage(200, 200, ColourSpace.RGB);

		    Parallel.forEach(clzImages, new Operation<MBFImage>() {
		        public void perform(MBFImage i) {
		            final MBFImage tmp = new MBFImage(200, 200, ColourSpace.RGB);
		            tmp.fill(RGBColour.WHITE);

		            final MBFImage small = i.process(resize).normalise();
		            final int x = (200 - small.getWidth()) / 2;
		            final int y = (200 - small.getHeight()) / 2;
		            tmp.drawImage(small, x, y);
		          //Stop multiple threads accessing the image at the same time
		            synchronized (current) {
		                current.addInplace(tmp);
		            }
		        }
		    });
		    current.divideInplace((float) clzImages.size());
		    output.add(current);    
		}
		System.out.println("Time: " + t1.duration() + "ms");
		DisplayUtilities.display("Images", output);
    }
   // building average image for each group(both loops parallel)
    public static void partitionedParallelAverageImage(VFSGroupDataset<MBFImage> allImages,GroupedDataset<String, ListDataset<MBFImage>, MBFImage> images) {
    final List<MBFImage> output = new ArrayList<MBFImage>();
    	final ResizeProcessor resize = new ResizeProcessor(200);
    	//resampling and normalizing each image in the group and drawing in center of white image
    	Timer t1 = Timer.timer();
    	Parallel.forEach(images.values(), new Operation<ListDataset<MBFImage>>(){
    		public void perform(ListDataset<MBFImage> object) {
    		    final MBFImage current = new MBFImage(200, 200, ColourSpace.RGB);
    		    Parallel.forEach(new RangePartitioner<MBFImage>(object), new Operation<MBFImage>() {
    		        public void perform(MBFImage i) {
    		            final MBFImage tmp = new MBFImage(200, 200, ColourSpace.RGB);
    		            tmp.fill(RGBColour.WHITE);

    		            final MBFImage small = i.process(resize).normalise();
    		            final int x = (200 - small.getWidth()) / 2;
    		            final int y = (200 - small.getHeight()) / 2;
    		            tmp.drawImage(small, x, y);
    		          //Stop multiple threads accessing the image at the same time
    		            synchronized (current) {
    		                current.addInplace(tmp);
    		            }
    		        }
    		    });
    		    current.divideInplace((float) object.size());
    		    output.add(current);    
    		}
    	});
    		System.out.println("Time: " + t1.duration() + "ms");
    		DisplayUtilities.display("Images", output);
    }
    // building average image for each group(Outer loop parallel)
    static void parallelOut(VFSGroupDataset<MBFImage> allImages,GroupedDataset<String, ListDataset<MBFImage>, MBFImage> images)
    {
    	final List<MBFImage> output = new ArrayList<MBFImage>();
    	final ResizeProcessor resize = new ResizeProcessor(200);
    	
    	Timer t1 = Timer.timer();
    	
    	Parallel.forEach(images.values(), new Operation<ListDataset<MBFImage>>(){
    		//resampling and normalizing each image in the group and drawing in center of white image
			public void perform(ListDataset<MBFImage> object) {
				final MBFImage current = new MBFImage(200, 200, ColourSpace.RGB);
				
				for (MBFImage i : object) {
	    	        MBFImage tmp = new MBFImage(200, 200, ColourSpace.RGB);
	    	        tmp.fill(RGBColour.WHITE);
	    	        MBFImage small = i.process(resize).normalise();
	    	        int x = (200 - small.getWidth()) / 2;
	    	        int y = (200 - small.getHeight()) / 2;
	    	        tmp.drawImage(small, x, y);
	    	        current.addInplace(tmp);
	    		}
	    		
	    		current.divideInplace((float) object.size());
	    	    output.add(current);
			}
    		
    	});
    	DisplayUtilities.display("Images", output);
    	System.out.println("Time: " + t1.duration() + "ms");
    }
}
