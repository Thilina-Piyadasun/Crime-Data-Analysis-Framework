package org.inquisitors.framework.statistical_analyzer.impl;


import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.fpm.AssociationRules;
import org.apache.spark.mllib.fpm.FPGrowth;
import org.apache.spark.mllib.fpm.FPGrowthModel;
import org.apache.spark.mllib.linalg.Matrix;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.stat.MultivariateStatisticalSummary;
import org.apache.spark.mllib.stat.Statistics;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.inquisitors.framework.front_connector.impl.DataReaderImpl;
import org.inquisitors.framework.preprocessor.CrimeDataHolder;
import org.inquisitors.framework.statistical_analyzer.api.CorrelationMethod;
import org.inquisitors.framework.statistical_analyzer.api.Stat_Summary;
import org.inquisitors.framework.statistical_analyzer.utils.AssociationCrimeRules;
import org.inquisitors.framework.statistical_analyzer.utils.Converter;
import org.inquisitors.framework.statistical_analyzer.utils.FPGrowthCrime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Thilina on 8/5/2016.
 */
public class Stat_SummaryImpl implements Stat_Summary ,Serializable{


    public Vector getMean() {
        CrimeDataHolder crimeDataHolder= CrimeDataHolder.getCrimeDataHolder();
        MultivariateStatisticalSummary summary = Statistics.colStats(crimeDataHolder.getCrimeDataVector().rdd());
        return summary.mean();
    }

    public Vector getVariance() {
        CrimeDataHolder crimeDataHolder= CrimeDataHolder.getCrimeDataHolder();
        MultivariateStatisticalSummary summary = Statistics.colStats(crimeDataHolder.getCrimeDataVector().rdd());
        return summary.variance();
    }

    public Vector getNonZeroes() {
        CrimeDataHolder crimeDataHolder= CrimeDataHolder.getCrimeDataHolder();
        MultivariateStatisticalSummary summary = Statistics.colStats(crimeDataHolder.getCrimeDataVector().rdd());
        return summary.variance();

    }

    public Matrix getCorrelation(CorrelationMethod method) {
        CrimeDataHolder crimeDataHolder= CrimeDataHolder.getCrimeDataHolder();
        Matrix correlMatrix = Statistics.corr(crimeDataHolder.getCrimeDataVector().rdd(), method.toString());
        return correlMatrix;
    }

    public double getCovariance(String col1, String col2){
        //TODO:Implement after preprocessor done
        return 0;
    }

    public double getCorrelation(String col1, String col2, CorrelationMethod method) {
        //TODO:Implement after preprocessor done
        return  0;

    }

    public ArrayList<ArrayList> getFrequentItems(String[] col, double support) {

        CrimeDataHolder crimeDataHolder= CrimeDataHolder.getCrimeDataHolder();
        DataFrame frequntItemFrame=crimeDataHolder.getCrimeDataFrame();
        frequntItemFrame.stat().freqItems(col,support);

        DataFrame temp= frequntItemFrame.stat().freqItems(col,support);
        Converter converter=new Converter();
        return converter.convert(temp);
    }


    public List<FPGrowthCrime> frequentItemSets(double minSupport){

        List<FPGrowthCrime> fpGrowthCrimes=new ArrayList<>();
        //get <List<String>> type RDD
        JavaRDD<List<String>> transactions =getTempRDDForFPGrowth();

        FPGrowth fpg = new FPGrowth()
                .setMinSupport(minSupport)
                .setNumPartitions(4);
        FPGrowthModel<String> model = fpg.run(transactions);

        for (FPGrowth.FreqItemset<String> itemset: model.freqItemsets().toJavaRDD().collect()) {
            FPGrowthCrime record=new FPGrowthCrime(itemset.javaItems(),itemset.freq());
            fpGrowthCrimes.add(record);
        }
        return fpGrowthCrimes;
    }

    public List<AssociationCrimeRules> mineFrequentPatterns(double minSupport,double conf) {

        List<AssociationCrimeRules> associationCrimeRules=new ArrayList<>();
        //get <List<String>> type RDD
        JavaRDD<List<String>> transactions =getTempRDDForFPGrowth();

        FPGrowth fpg = new FPGrowth()
                .setMinSupport(minSupport)
                .setNumPartitions(4);
        FPGrowthModel<String> model = fpg.run(transactions);

        for (AssociationRules.Rule<String> rule : model.generateAssociationRules(conf).toJavaRDD().collect()) {
            System.out.println(
                    rule.javaAntecedent() + " => " + rule.javaConsequent() + ", " + rule.confidence());

            AssociationCrimeRules rules=new AssociationCrimeRules(rule.javaAntecedent(),rule.javaConsequent(),rule.confidence());
            associationCrimeRules.add(rules);
        }
        return associationCrimeRules;
    }

    private JavaRDD<List<String>> getTempRDDForFPGrowth() {
        //TODO:Exception handel
        //TODO:

        CrimeDataHolder crimeDataHolder= CrimeDataHolder.getCrimeDataHolder();
        DataFrame df=crimeDataHolder.getCrimeDataFrame();
        df.registerTempTable("freqtable");

        //TODO: add time col after preporessor done
        DataFrame selected=DataReaderImpl.sqlContext.sql("select Category,DayOfWeek,PdDistrict,X,Y from freqtable");

        JavaRDD<List<String>> transactions = selected.javaRDD().map(new Function<Row, List<String>>() {
            public List<String> call(Row row) {
                String[] items = {row.getString(0), row.getString(1),row.getString(2),""+ row.getDouble(3),""+row.getDouble(4)};
                return Arrays.asList(items);
            }
        });

        return transactions;
    }
}
