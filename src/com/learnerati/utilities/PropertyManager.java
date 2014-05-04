package com.learnerati.utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author      Ben S. Jones <ben.jones@learnerati.com>
 * @version     1.0                 
 * @since       2013-06-31
 */
public class PropertyManager {

    public PropertyManager() {
    }

    
    /**
     * Reads a properties file from the file system using full pathing
     * 
     * @param fileName name of the properties file
     * @return A properties object 
     */
    public Properties readFile(String fileName) {

        Properties props = new Properties();

        try {
            InputStream is = new FileInputStream(fileName);
            if (is != null) {
                props.load(is);
            }
        } catch (FileNotFoundException ex) {
            Logger lgr = Logger.getLogger(PropertyManager.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        } catch (IOException ex) {
            Logger lgr = Logger.getLogger(PropertyManager.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return props;
    }

    
    /**
     * Reads a properties file from the current context of the calling resource
     * 
     * @param fileName name of the properties file
     * @return A properties object
     */
    public Properties read(String fileName) {
        Properties props = new Properties();

        try {
            InputStream is = PropertyManager.class.getResourceAsStream(fileName);
            if (is != null) {
                props.load(is);
            }
        } catch (FileNotFoundException ex) {
            Logger lgr = Logger.getLogger(PropertyManager.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);


        } catch (IOException ex) {
            Logger lgr = Logger.getLogger(PropertyManager.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return props;
    }
    
    
    
    /**
     * Searching resources from the provided class to load a properties file
     * 
     * @param cls class from which to start searching resources
     * @param fileName name of the properties file
     * @return A properties object
     */
    public Properties fromPackage(Class cls, String fileName) {
        Properties props = new Properties();

        try {
            InputStream is = cls.getResourceAsStream(fileName);
            if (is != null) {
                props.load(is);
            }
        } catch (FileNotFoundException ex) {
            Logger lgr = Logger.getLogger(cls.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);


        } catch (IOException ex) {
            Logger lgr = Logger.getLogger(cls.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return props;
    }    
}
