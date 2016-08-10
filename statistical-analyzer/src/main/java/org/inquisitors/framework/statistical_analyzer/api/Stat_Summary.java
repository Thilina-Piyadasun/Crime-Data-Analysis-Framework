package org.inquisitors.framework.statistical_analyzer.api;

import org.inquisitors.framework.statistical_analyzer.api.CorrelationMethod;
import org.apache.spark.mllib.linalg.Vector;
import org.inquisitors.framework.statistical_analyzer.utils.AssociationCrimeRules;
import org.inquisitors.framework.statistical_analyzer.utils.FPGrowthCrime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thilina
 * Overall description about the dataset
 */
public interface Stat_Summary {

    /*
    * give overall results and summeries of given colomns
    */
    Vector getMean();

    Vector getVariance();

    /*
    * get count of non zero values of each coloumn
    * */
    Vector getNonZeroes();

    /*
    * get correlation of given data set
    *   "pearson" or "spearman"
    * */
    org.apache.spark.mllib.linalg.Matrix getCorrelation(CorrelationMethod method);

    /*
    * get Covariance of two colomns
    * */
    double getCovariance(String col1, String col2);
    /*
     * get correlation of two colomns
     * */
    double getCorrelation(String col1, String col2, CorrelationMethod method);

    /*
    * frequent item sets in given data set
    * select convenient return type instead of Data frame
    * */
    ArrayList<ArrayList> getFrequentItems(String col[],double support);

    List<FPGrowthCrime> frequentItemSets(double minSupport);

    List<AssociationCrimeRules> mineFrequentPatterns(double minSupport,double conf);
}
