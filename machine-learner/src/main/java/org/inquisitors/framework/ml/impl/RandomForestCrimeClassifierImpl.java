package org.inquisitors.framework.ml.impl;

import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.classification.RandomForestClassifier;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.ml.feature.*;
import org.apache.spark.sql.DataFrame;
import org.inquisitors.framework.ml.api.Model;
import org.inquisitors.framework.ml.api.RandomForestClassifierModel;
import org.inquisitors.framework.ml.util.MLDataParser;
import org.inquisitors.framework.ml.util.TestData;
import org.inquisitors.framework.ml.util.TrainData;

import java.util.HashMap;
import java.util.Vector;

/**
 * Created by Thilina on 8/13/2016.
 */
public class RandomForestCrimeClassifierImpl implements RandomForestClassifierModel {

    StringIndexerModel labelIndexer;
    VectorIndexerModel featureIndexer;
    RandomForestClassifier rf;
    IndexToString labelConverter;
    Pipeline pipeline;


    String generated_feature_col_name="features";
    String indexedLabel="indexedLabel";
    String indexedFeatures="indexedFeatures";
    String prediction="prediction";

    /*
    *   featuredDF - vector assemblered data frame to train the model
    *   label - label column in  train data set
    *   predictedLabel -a new column name to generate predictions to test data set and those predictions store under that column name
    *
    *   this method trains Random forest classifier model and return the model
    * */

    private PipelineModel train(DataFrame featuredDF,String label, String predictedLabel) {

        try{
            labelIndexer = new StringIndexer()
                    .setInputCol(label)
                    .setOutputCol(indexedLabel)
                    .fit(featuredDF);

            // Automatically identify categorical features, and index them.
            // Set maxCategories so features with > 4 distinct values are treated as continuous.
            featureIndexer = new VectorIndexer()
                    .setInputCol(generated_feature_col_name)
                    .setOutputCol(indexedFeatures)
                    .setMaxCategories(4)
                    .fit(featuredDF);

            rf = new RandomForestClassifier()
                    .setLabelCol(indexedLabel)
                    .setFeaturesCol(indexedFeatures);

            labelConverter = new IndexToString()
                    .setInputCol(prediction)
                    .setOutputCol(predictedLabel)
                    .setLabels(labelIndexer.labels());

            pipeline = new Pipeline()
                    .setStages(new PipelineStage[] {labelIndexer, featureIndexer, rf, labelConverter});

            PipelineModel model = pipeline.fit(featuredDF);
            return model;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public DataFrame train_and_Predict(TrainData trainData,TestData testData)throws Exception{

        String label=trainData.getLabel();
        String predictedLabel =testData.getPrediction_coloumn();

        DataFrame featuredDF=getFeaturesFrame(trainData.getTrainDF(), trainData.getFeature_columns());
        PipelineModel model=train(featuredDF,label,predictedLabel);

        if(model!=null){

            DataFrame featuredTestData=getFeaturesFrame(testData.getTestDF(), trainData.getFeature_columns());
            DataFrame predictions = model.transform(featuredTestData);

            return predictions;
        }
        else {
            throw new Exception("no trained randomForest classifier model found");
        }

    }
    /*
    * Use only train set to train model and get accuracy
    * */
    public double evaluateModel(TrainData dataFrame,double partition)throws Exception{

        String predictedLabel=prediction;
        String label =dataFrame.getLabel();
        DataFrame featuredDF=getFeaturesFrame(dataFrame.getTrainDF(), dataFrame.getFeature_columns());

        DataFrame[] splits = featuredDF.randomSplit(new double[] {1-partition, partition});
        DataFrame trainingData = splits[0];
        DataFrame testData = splits[1];

        PipelineModel model=train(trainingData,label,predictedLabel);
        if(model!=null){

            DataFrame predictions = model.transform(testData);
            MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
                    .setLabelCol(indexedLabel)
                    .setPredictionCol(prediction)
                    .setMetricName("precision");
            double accuracy = evaluator.evaluate(predictions);
            return accuracy;
        }
        else {
            throw new Exception("no trained randomForest classifier model found");
        }
    }

    /*
    * generate extra feature vector column to given dataset
    * */
    public DataFrame getFeaturesFrame(DataFrame df,String[] featureCols){

        return new MLDataParser().getFeaturesFrame(df,featureCols, generated_feature_col_name);
    }

    public DataFrame predict(Vector<Object> features) {
        return null;
    }

    public Vector evaluate() {
        return null;
    }

    public Model setGISData(HashMap<String, Vector> dataSet) {


        return null;
    }

    public Model setWeatherData(HashMap<String, Vector> dataSet) {
        return null;
    }
}
