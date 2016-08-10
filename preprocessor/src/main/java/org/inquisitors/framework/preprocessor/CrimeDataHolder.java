package org.inquisitors.framework.preprocessor;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.DataFrame;
import org.inquisitors.framework.front_connector.impl.DataReaderImpl;
import org.apache.spark.mllib.linalg.Vector;
/**
 * Created by Thilina on 8/5/2016.
 */
public class CrimeDataHolder {



    private static CrimeDataHolder crimeDataHolder;

    private CrimeDataHolder(){}

    public static CrimeDataHolder getCrimeDataHolder(){
        if(crimeDataHolder==null)
            crimeDataHolder=new CrimeDataHolder();
        return crimeDataHolder;
    }

    /**
     * @return DataFrame
     */
    public  DataFrame getCrimeDataFrame(){
        //change the implementation of this method after preprocessor  implementation
        return DataReaderImpl.input;
    }

    public  JavaRDD getCrimeDataRDD(){
        return DataReaderImpl.getCrimeRDD();
    }
    public  JavaRDD<Vector> getCrimeDataVector(){
        return DataReaderImpl.getCrimeDataVector();
    }

}
