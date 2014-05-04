/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.learnerati.utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Provides reading access to a java properties file. 
 * <p/>
 * <b>Note:</b> The class defaults to "database.properties" as the intended file name.
 *
 * @author Ben
 */
public class ReadProperties {
       
    private String _fileName = new String();

    /** Default constructor which uses "database.properties" as the intended properties file name.
     * 
     */
    public ReadProperties() {
        _fileName = "database.properties";
    }

    /** Over loaded constructor that accepts a filename.
     * 
     * @param fileName the filename to extract properties from.
     */
    public ReadProperties(String fileName) {
        _fileName = fileName;
    }

    /** Reads a properties file and creates a new Properties object with available 
     * info.    
     * 
     * <p/><b>Usage Examples:</b>
     * {@code Properties props = new ReadProperties().Fetch(); }
     * <p/>
     * {@code Properties props = new ReadProperties("my.properties).Fetch(); }
     * 
     * @return Properties Object
     */
    public Properties Fetch() {
        Properties props = new Properties();

        try {
            InputStream is = ReadProperties.class.getResourceAsStream(_fileName);            
            if (is != null) 
            {
                props.load(is);
            }            
        } catch (FileNotFoundException ex) {
            Logger lgr = Logger.getLogger(ReadProperties.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            

        } catch (IOException ex) {
            Logger lgr = Logger.getLogger(ReadProperties.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);                   
        } 
        return props;
    }
    
    
    public Properties FetchFromFile(String fileName) {
        
        Properties props = new Properties();

        try {
            InputStream is = new FileInputStream(fileName);            
            if (is != null) 
            {
                props.load(is);
            }            
        } catch (FileNotFoundException ex) {
            Logger lgr = Logger.getLogger(ReadProperties.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        } catch (IOException ex) {
            Logger lgr = Logger.getLogger(ReadProperties.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        } 
        return props;
    }    
    
    
}
