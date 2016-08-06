package org.inquisitors.framework.statistical_analyzer.api;

import org.inquisitors.framework.statistical_analyzer.utils.DataSummary;

/**
 * Created by Thilina
* Analyze the Relationship between Category , PoliceJuri, location and time
* */
public interface Stat_Analyzer {
    DataSummary getSummary(String field);
}
