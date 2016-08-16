package org.inquisitors.framework.ml.api;

import org.apache.spark.sql.DataFrame;
import org.inquisitors.framework.ml.util.TestData;
import org.inquisitors.framework.ml.util.TrainData;

/**
 * Created by Thilina on 8/15/2016.
 */
public interface RandomForestClassifierModel {

    DataFrame train_and_Predict(TrainData trainData,TestData testData)throws Exception;

    double evaluateModel(TrainData dataFrame,double partition)throws Exception;
}
