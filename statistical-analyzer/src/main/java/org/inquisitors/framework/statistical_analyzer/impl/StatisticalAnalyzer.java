package org.inquisitors.framework.statistical_analyzer.impl;

/**
 * Created by minudika on 8/6/16.
 */
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;
import org.inquisitors.framework.statistical_analyzer.api.Stat_Analyzer;
import org.inquisitors.framework.statistical_analyzer.utils.Converter;
import org.inquisitors.framework.statistical_analyzer.utils.DataSummary;

import java.util.ArrayList;
import java.util.List;

public class StatisticalAnalyzer implements Stat_Analyzer{
    SparkConf conf = new SparkConf().setMaster("local").setAppName("StatAnalyzer");
    JavaSparkContext sc = new JavaSparkContext(conf);
    SQLContext sqlContext = new SQLContext(sc);


    public StatisticalAnalyzer(){
        conf = new SparkConf().setMaster("local").setAppName("StatAnalyzer");
        sc = new JavaSparkContext(conf);
        sqlContext = new SQLContext(sc);
    }

    public List getAllFields(DataFrame dataFrame) {
        String str = dataFrame.collectAsList().toString();
        String[] FnTs = str.substring(1,str.length()-1).split(",");
        List<String> fields = new ArrayList();
        for(String s : FnTs){
            fields.add(s.split(":")[1].trim().toString());
        }
        return fields;
    }

    public List getSubFields(DataFrame dataFrame, String baseField) {
        String str = dataFrame.collectAsList().toString();
        String[] FnTs = str.substring(1,str.length()-1).split(",");
        List<String> fields = new ArrayList();
        for(String s : FnTs){
            fields.add(s.split(":")[1].trim().toString());
        }
        fields.remove((Object)baseField);
        return fields;
    }

    public DataSummary getSummary(DataFrame dataFrame,String baseField,String baseClass){
        DataSummary dataSummary = new DataSummary(baseField);
        dataSummary.setRecords(summarize(dataFrame,baseField,baseClass));
        return dataSummary;
    }

    private List<ArrayList> summarize(DataFrame dataFrame,String baseField,String baseClass){;
        List<String> subFields = new ArrayList<String>();

        dataFrame.registerTempTable("dataset");

        StringBuilder stringBuilder = new StringBuilder();
        for(String field : subFields){
            stringBuilder.append(field+",");
        }

        stringBuilder.deleteCharAt(stringBuilder.length()-1);

        String sqlQuery = "SELECT " + stringBuilder.toString() + " FROM dataset " +
                " WHERE " + baseField + " = " + baseClass;

        List<ArrayList> list;
        DataFrame df = sqlContext.sql(sqlQuery);
        Converter converter = new Converter();
        list = converter.convert(df);

        return list;
    }
}
