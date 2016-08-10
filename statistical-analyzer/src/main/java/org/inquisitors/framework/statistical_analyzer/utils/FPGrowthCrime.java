package org.inquisitors.framework.statistical_analyzer.utils;

import java.io.Serializable;
import java.lang.String;import java.util.List;

/**
 * Created by Thilina on 8/6/2016.
 */
public class FPGrowthCrime  implements Serializable {

    private List<String> frequentCrimePatterns;
    private long frequency;

    public FPGrowthCrime(List<String> frequentCrimePatterns, long frequency) {
        this.frequentCrimePatterns = frequentCrimePatterns;
        this.frequency = frequency;
    }

    public List<String> getFrequentCrimePatterns() {
        return frequentCrimePatterns;
    }

    public void setFrequentCrimePatterns(List<String> frequentCrimePatterns) {
        this.frequentCrimePatterns = frequentCrimePatterns;
    }

    public long getFrequency() {
        return frequency;
    }

    public void setFrequency(long frequency) {
        this.frequency = frequency;
    }
}
