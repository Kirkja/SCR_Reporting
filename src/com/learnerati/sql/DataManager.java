package com.learnerati.sql;

import com.learnerati.utilities.PropertyManager;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Ben S. Jones <ben.jones@learnerati.com>
 * @version 1.0
 * @since 2013-06-31
 */
public class DataManager {

    private Properties props;
    private PropertyManager propManager;
    private Connection con = null;
    private Statement st = null;
    private ResultSet rs = null;

    public DataManager() {
        propManager = new PropertyManager();
    }

    /**
     * Reads a file to use as a properties definition for the database connection
     * 
     * @param propFile name of the properties file
     */
    public void initFile(String propFile) {
        props = propManager.read(propFile);
        try {
            initialize();
        } catch (Exception ex) {
            Logger.getLogger(DataManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Reads a package resource to use as a properties definition for the database connection 
     * 
     * @param clazz class from which to start the search through local resources
     * @param propFile name of the properties file
     */
    public void initPackage(Class clazz, String propFile) {
        props = propManager.fromPackage(clazz, propFile);
        try {
            initialize();
        } catch (Exception ex) {
            Logger.getLogger(DataManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Uses a properties definition to establish the database connection 
     * 
     * @throws Exception missing properties file error
     */
    private void initialize() throws Exception {
        if (props.propertyNames().hasMoreElements()) {
            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String passwd = props.getProperty("db.passwd");

            if (url != null && user != null && passwd != null) {
                try {
                    con = DriverManager.getConnection(url, user, passwd);
                } catch (SQLException ex) {
                    Logger.getLogger(DataManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            throw new Exception("Properties file is missing or invalid.");
        }
    }

    /**
     * Uses a string based query to extract row data
     * 
     * @param query an SQL query in string form ready to execute 
     * @return a resultset from the executed query
     */
    public ResultSet Execute(String query) {
        rs = null;

        try {
            Statement stmt = con.createStatement();
            rs = stmt.executeQuery(query);
        } catch (SQLException ex) {
            Logger.getLogger(DataManager.class.getName()).log(Level.SEVERE, null, ex);

            //System.out.println("\n\n" + query);
        }

        return rs;
    }

    public void Update(String query) {
        rs = null;

        try {
            Statement stmt = con.createStatement();
            stmt.execute(query);
        } catch (SQLException ex) {
            Logger.getLogger(DataManager.class.getName()).log(Level.SEVERE, null, ex);
            //System.out.println("\n\n" + query);
        }

        //return rs;
    }    
    
    
    /**
     * Simply dump the resultset out to console for debugging
     * 
     * @param rs resultset of records to dump to console
     */
    public void DisplayRecords(ResultSet rs) {
        try {
            rs.beforeFirst();
            ResultSetMetaData metaData = rs.getMetaData();

            while (rs.next()) {
                for (int idx = 1; idx <= metaData.getColumnCount(); idx++) {
                    if (rs.getObject(idx) != null) {
                        System.out.print(rs.getObject(idx).toString() + "\t");
                    }                    
                }
                System.out.println("");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("");
    }
}
