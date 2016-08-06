package org.inquisitors.framework.statistical_analyzer.utils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by minudika on 8/5/16.
 */
public class DataSummary {
    private String baseField = null;
    private ArrayList subfields = null;
    private HashMap<String,ArrayList>dataEntries = null;
    private ArrayList<Record> records;

    public DataSummary(){
        subfields = new ArrayList<String>();
        records = new ArrayList<Record>();
    }

    public DataSummary(String... fields){
        this.baseField = fields[0];
        records = new ArrayList<Record>();
        subfields = new ArrayList<String>();
        for(int i=1; i<fields.length; i++){
            subfields.add(fields[i]);
        }
    }

    public String getBaseField(){
        return baseField;
    }

    public void setBaseField(String baseField){
        this.baseField = baseField;
    }

    public void addRecord(Record record){
        this.records.add(record);
    }

    public void addSubfield(String field){
        if(!subfields.contains(field)) {
            dataEntries.put(field, new ArrayList<Object>());
        }
        else{
            // TODO: throw error
        }
    }

    public void addSubfields(String... fields){
        for(String field : fields){
            subfields.add(field);
        }
    }

    /*public void addDataEntry(String fieldName,Object value){
        if(dataEntries.get(fieldName)== null){
            ArrayList<Object> list = new ArrayList<Object>();
            list.add(value);
            dataEntries.put(fieldName,list);
        }
        else{
            dataEntries.get(fieldName).add(value);
        }
    }*/

}
