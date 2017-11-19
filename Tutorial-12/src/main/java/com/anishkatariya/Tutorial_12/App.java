package com.anishkatariya.Tutorial_12;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.openimaj.experiment.dataset.sampling.GroupSampler;
import org.openimaj.experiment.dataset.split.GroupedRandomSplitter;

import org.openimaj.image.*;
import java.io.File;
import java.util.Map;
import org.openimaj.data.DataSource;
import org.openimaj.data.dataset.Dataset;
import org.openimaj.data.dataset.GroupedDataset;
import org.openimaj.data.dataset.ListDataset;
import org.openimaj.data.dataset.VFSListDataset;
import org.openimaj.experiment.dataset.sampling.GroupedUniformRandomisedSampler;
import org.openimaj.experiment.evaluation.classification.ClassificationEvaluator;
import org.openimaj.experiment.evaluation.classification.ClassificationResult;
import org.openimaj.experiment.evaluation.classification.analysers.confusionmatrix.CMAnalyser;
import org.openimaj.experiment.evaluation.classification.analysers.confusionmatrix.CMResult;
import org.openimaj.feature.DiskCachingFeatureExtractor;
import org.openimaj.feature.DoubleFV;
import org.openimaj.feature.FeatureExtractor;
import org.openimaj.feature.SparseIntFV;
import org.openimaj.feature.local.data.LocalFeatureListDataSource;
import org.openimaj.feature.local.list.LocalFeatureList;
import org.openimaj.image.annotation.evaluation.datasets.Caltech101;
import org.openimaj.image.annotation.evaluation.datasets.Caltech101.Record;
import org.openimaj.image.feature.dense.gradient.dsift.ByteDSIFTKeypoint;
import org.openimaj.image.feature.dense.gradient.dsift.DenseSIFT;
import org.openimaj.image.feature.dense.gradient.dsift.PyramidDenseSIFT;
import org.openimaj.image.feature.local.aggregate.BagOfVisualWords;
import org.openimaj.image.feature.local.aggregate.BlockSpatialAggregator;
import org.openimaj.ml.annotation.linear.LiblinearAnnotator;
import org.openimaj.ml.annotation.linear.LiblinearAnnotator.Mode;
import org.openimaj.ml.clustering.ByteCentroidsResult;
import org.openimaj.ml.clustering.assignment.HardAssigner;
import org.openimaj.ml.clustering.kmeans.ByteKMeans;
import org.openimaj.ml.kernel.HomogeneousKernelMap;
import org.openimaj.ml.kernel.HomogeneousKernelMap.KernelType;
import org.openimaj.ml.kernel.HomogeneousKernelMap.WindowType;
import org.openimaj.util.pair.IntFloatPair;

import de.bwaldvogel.liblinear.SolverType;


/**
 *Total instances: 150.000
 *Total correct: 77.000
 *Total incorrect: 73.000
 *Accuracy: 0.513
 *Error Rate: 0.487
 *Average Class Accuracy: 0.513
 *Average Class Error Rate: 0.487
 *
 */
public class App {
    public static void main( String[] args ) throws IOException {
    	File featureCache = new File("/Users/anishkatariya/Desktop/Computer-Vision/Tutorial-12/cache");
    	GroupedDataset<String, VFSListDataset<Record<FImage>>, Record<FImage>> allData = 
    			Caltech101.getData(ImageUtilities.FIMAGE_READER);
    	GroupedDataset<String, ListDataset<Record<FImage>>, Record<FImage>> data = 
    			GroupSampler.sample(allData, 10, false);
    	
    	//spliting into training and test, 15/group
    	GroupedRandomSplitter<String, Record<FImage>> splits = 
    			new GroupedRandomSplitter<String, Record<FImage>>(data, 15, 0, 15);
    	
    	//taking dense sift and applying to diffrent sizes 
    	DenseSIFT dsift = new DenseSIFT(5, 7); 	
    	PyramidDenseSIFT<FImage> pdsift = new PyramidDenseSIFT<FImage>(dsift, 6f, 7);
    	
    	//training with random set of 30 images
    	HardAssigner<byte[], float[], IntFloatPair> assigner = 
    			trainQuantiser(GroupedUniformRandomisedSampler.sample(splits.getTrainingDataset(), 30), pdsift);
    	
    	//Using homogenious kernel map
   // 	HomogeneousKernelMap kernelMap = new HomogeneousKernelMap(KernelType.Chi2, WindowType.Rectangular);
    	
    
    	FeatureExtractor<DoubleFV, Record<FImage>> extractor = new PHOWExtractor(pdsift, assigner);
  //  	DiskCachingFeatureExtractor<DoubleFV, Record<FImage>> cache = new DiskCachingFeatureExtractor<DoubleFV, Record<FImage>>(featureCache, extractor);
    	
    	//making and training the classifier
    	System.out.println("Training");
    	LiblinearAnnotator<Record<FImage>, String> ann = new LiblinearAnnotator<Record<FImage>, String>(
	            extractor, Mode.MULTICLASS, SolverType.L2R_L2LOSS_SVC, 1.0, 0.00001);
    	ann.train(splits.getTrainingDataset());
    	System.out.println("Trained");
    	
    	//Evaluating classifier
    	System.out.println("Evaluating");
    	ClassificationEvaluator<CMResult<String>, String, Record<FImage>> eval = 
    			new ClassificationEvaluator<CMResult<String>, String, Record<FImage>>(
    				ann, splits.getTestDataset(), new CMAnalyser<Record<FImage>, String>(CMAnalyser.Strategy.SINGLE));
    				
    Map<Record<FImage>, ClassificationResult<String>> guesses = eval.evaluate();
    CMResult<String> result = eval.analyse(guesses);
    //printing result
    System.out.println(result.getDetailReport());
    }
   //extracts 1000 dense SIFT features from images in dataset,cluster in 300 classes,hard assigner that assigns sift features  
    static HardAssigner<byte[], float[], IntFloatPair> trainQuantiser(
            Dataset<Record<FImage>> sample, PyramidDenseSIFT<FImage> pdsift)
    {
    		List<LocalFeatureList<ByteDSIFTKeypoint>> allkeys = new ArrayList<LocalFeatureList<ByteDSIFTKeypoint>>();
    			//extracting dense features
    			for (Record<FImage> rec : sample) {
    				FImage img = rec.getImage();

    				pdsift.analyseImage(img);
    				allkeys.add(pdsift.getByteKeypoints(0.005f));
    			}
    			//keepin only 10000 features
    			if (allkeys.size() > 10000)
    				allkeys = allkeys.subList(0, 10000);
    			//clustering into seperate classes
    			ByteKMeans km = ByteKMeans.createKDTreeEnsemble(300);
    			DataSource<byte[]> datasource = new LocalFeatureListDataSource<ByteDSIFTKeypoint, byte[]>(allkeys);
    			ByteCentroidsResult result = km.cluster(datasource);

    			return result.defaultHardAssigner();
    }
    
    static class PHOWExtractor implements FeatureExtractor<DoubleFV, Record<FImage>> {
        PyramidDenseSIFT<FImage> pdsift;
        HardAssigner<byte[], float[], IntFloatPair> assigner;

        public PHOWExtractor(PyramidDenseSIFT<FImage> pdsift, HardAssigner<byte[], float[], IntFloatPair> assigner)
        {
            this.pdsift = pdsift;
            this.assigner = assigner;
        }

        public DoubleFV extractFeature(Record<FImage> object) {
            FImage image = object.getImage();
            pdsift.analyseImage(image);
            //using hard assigner to assign dense sift
            BagOfVisualWords<byte[]> bovw = new BagOfVisualWords<byte[]>(assigner);

            BlockSpatialAggregator<byte[], SparseIntFV> spatial = new BlockSpatialAggregator<byte[], SparseIntFV>(
                    bovw, 2, 2);
            //histograms appended and normalised
            return spatial.aggregate(pdsift.getByteKeypoints(0.015f), image.getBounds()).normaliseFV();
        }
    }
    
}
