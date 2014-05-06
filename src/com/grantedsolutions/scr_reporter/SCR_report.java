/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grantedsolutions.scr_reporter;

import com.learnerati.sql.DataManager;
import com.learnerati.utilities.DocBuilder;
import com.learnerati.utilities.ReadProperties;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author User1
 */
public class SCR_report {

    private static DataManager DM;

    protected Properties props;

    private DocBuilder base;

    public SCR_report() {
        DM = new DataManager();

        try {
            DM.initPackage(DataManager.class, "database2.properties");
        } catch (Exception ex) {
            ;
        }

        //props = new ReadProperties("output.properties").Fetch();        
    }

    public void setDocBase(DocBuilder base) {
        this.base = base;
    }

    public void generate(Map<String, String> reportParams) {

        System.out.println("Start building the report framework.");

        if (reportParams.containsKey("projectReport")) {
            if (reportParams.get("projectReport").equals("true")) {

                if (reportParams.containsKey("siteBreakouts")) {
                    if (reportParams.get("siteBreakouts").equals("true")) {                        
                        generateProjectWithSites(reportParams.get("accountID"), reportParams.get("projectID"));                        
                    } else {                        
                        generateProjectOnly(reportParams.get("accountID"), reportParams.get("projectID"));                        
                    }
                } else {
                    System.out.println("No site breakouts requested.");
                }
            } else {
                System.out.println("No project summary report requested.");
            }
        } else {
            System.out.println("No project summary report requested.");
        }

        if (reportParams.containsKey("siteReports")) {
            if (reportParams.get("siteReports").equals("true")) {
                generateSites(reportParams.get("accountID"), reportParams.get("projectID"));                
            } else {
                System.out.println("No site reports requested.");
            }
        } else {
            System.out.println("No site reports requested.");
        }

    }
    
    
    
    private void generateProjectWithSites(String accountID, String projectID) {
        System.out.println("Generate project report with site breakouts.");
        
        
    }
    
    private void generateProjectOnly(String accountID, String projectID) {
        System.out.println("Generate only project report.");
        
        
    }
    
    
    private void generateSites(String accountID, String projectID) {
        System.out.println("Generate individual site reports.");
        
        String sql = "SELECT\n" +
                    "  BP.accountID, BA.title AS accountName\n" +
                    ", BP.id AS projectID, BP.name AS projectName\n" +
                    ", MSP.siteID, BS.disname AS districtName, BS.schName AS schoolName\n" +
                    "FROM map_site_project AS MSP\n" +
                    "LEFT JOIN bank_project AS BP ON BP.id = MSP.projectID\n" +
                    "LEFT JOIN bank_account AS BA ON BA.id = BP.accountID\n" +
                    "LEFT JOIN bank_site AS BS ON BS.id = MSP.siteID\n" +
                    "WHERE MSP.active = 'y'\n" +
                    "AND BP.active = 'y'\n" +
                    "AND BP.accountID = '" + accountID + "' " +
                    "AND BP.id ='"+  projectID + "' " +
                    "GROUP BY BP.accountID, BP.id, MSP.siteID";
        
        
    }    
    
    

}
