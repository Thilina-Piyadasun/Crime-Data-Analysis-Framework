package org.inquisitors.framework.front_connector.api;

/**
 * Created by Thilina Piyadasun on 7/3/2016.
 * interface for Read the given file and cache the content
 */
public interface DataReader {

    /*
    *  set hadoop home property path (in windows)
    */
    void setSystemProperty(String hadoop_home);

    /*
    * Read user file and cache the content in RDD
    * int storage level specifies the level of Storage.(MEMORY_ONLY,MEMORY_ONLY_SER ,MEMORY_AND_DISK, MEMORY_AND_DISK_SER)
    *
    */
    void read_file(String filename, int storage_level);

}
