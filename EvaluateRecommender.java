package org.bigdatacourse.Recommendersystem;

import java.io.File;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.RMSRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;

public class EvaluateRecommender {
	public static void main(String[] args) throws Exception
	{
		DataModel model = new FileDataModel(new File(args[0]));
		RecommenderEvaluator MAEevaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
		RecommenderEvaluator RMSEevaluator = new RMSRecommenderEvaluator();
		RecommenderBuilder builder = new MyRecommenderBuilder();
		double result = MAEevaluator.evaluate(builder,null, model, 0.9, 1.0);
		System.out.println("Average absolute Difference: "+result);
		result = RMSEevaluator.evaluate(builder,null, model, 0.9, 1.0);
		System.out.println("Root Mean Squared Error: "+result);
		RecommenderIRStatsEvaluator evaluator = new GenericRecommenderIRStatsEvaluator();
		IRStatistics stats = evaluator.evaluate(builder,null, model,null, 10,  
	    GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, 1.0);
		System.out.println("precision: "+stats.getPrecision());
		System.out.println("Recall: "+stats.getRecall());
		System.out.println("F1 measure: "+ stats.getF1Measure());
		
	}
}
	class MyRecommenderBuilder implements RecommenderBuilder
	{

		public Recommender buildRecommender(DataModel model)
				throws TasteException {
			UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
			UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.9, similarity, model);
			
			//UserSimilarity similarity1 = new EuclideanDistanceSimilarity(model);
			//UserNeighborhood neighborhood1 = new ThresholdUserNeighborhood(0.5, similarity1, model);
			
			//UserSimilarity similarity2 = new LogLikelihoodSimilarity(model);
			//UserNeighborhood neighborhood2 = new ThresholdUserNeighborhood(0.5, similarity2, model);
			
			//UserSimilarity similarity3 = new TanimotoCoefficientSimilarity(model);
			//UserNeighborhood neighborhood3 = new ThresholdUserNeighborhood(0.5, similarity3, model);
			
			
		return new GenericUserBasedRecommender(model , neighborhood, similarity);
	}
		
	}

	
