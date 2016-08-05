package org.inqusutors.framework.front_connector.impl;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.storage.StorageLevel;
import org.inqusutors.framework.front_connector.api.DataReader;

/**
 * Created by Thilina Piyadasun on 7/3/2016.
 * Read the given file and cache the content
 */
public class DataReaderImpl implements DataReader {

    //static JavaRDD<String> input;
    static DataFrame input;

    public void setSystemProperty(String hadoop_home){
        System.setProperty("hadoop.home.dir", hadoop_home);
    }

    /*
    * Read user file and cache the content in RDD
    * int storage level specifies the level of Storage.(MEMORY_ONLY,MEMORY_ONLY_SER ,MEMORY_AND_DISK, MEMORY_AND_DISK_SER)
    *
    */
    public void read_file(String filename ,int storage_level){

        SparkConf conf = new SparkConf().setMaster("local").setAppName("DataReader");

        JavaSparkContext sc = new JavaSparkContext(conf);
        SQLContext sqlContext = new SQLContext(sc);

        // Load the input data to a static Data Frame
        input=sqlContext.read()
                .format("com.databricks.spark.csv")
                .option("header","true")
                .option("inferSchema","true")
                .load(filename);

        cache_data(storage_level);
    }

    private void cache_data(int storage_level){

        if(storage_level==1)
            input.persist(StorageLevel.MEMORY_ONLY());
        else if(storage_level==2)
            input.persist(StorageLevel.MEMORY_ONLY_SER());
        else if(storage_level==3)
            input.persist(StorageLevel.MEMORY_AND_DISK());
        else
            input.persist(StorageLevel.MEMORY_AND_DISK_SER());
    }
}
