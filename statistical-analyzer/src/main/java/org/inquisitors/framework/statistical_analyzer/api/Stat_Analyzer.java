package org.inquisitors.framework.statistical_analyzer.api;

import javafx.scene.chart.PieChart;
import org.apache.spark.sql.DataFrame;
import org.inquisitors.framework.statistical_analyzer.utils.DataSummary;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thilina
* Analyze the Relationship between Category , PoliceJuri, location and time
* */
public interface Stat_Analyzer {
    DataSummary getSummary(DataFrame dataFrame, String baseField, String baseClass);

    List getAllFields(DataFrame dataFrame);

    List getSubFields(DataFrame dataFrame,String baseField);

}
