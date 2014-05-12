/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.grantedsolutions.scr_reporter;

import com.learnerati.datameme.DMemeGrid;
import com.learnerati.datameme.DataMeme;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User1
 */
public class SCR_data {
    
    
    public SCR_data() {}
    
    
    
    
    public DMemeGrid getStandardData(
              String projectID
            , String siteID
            , String gradeLevel) 
    {
        
        DMemeGrid dmg = new DMemeGrid();
                       
        //dmg.setLabel("Standards: English Grade 2");
        dmg.setColDescriptor("Collected Content Areas");
        dmg.setRowDescriptor("Drift in Grade Level");   
            
        Integer total = 0;
                    
        String sql = "SELECT \n" +
                    "  MSP.projectID\n" +
                    ", BP.name AS projectName\n" +
                    ", BSite.disname AS districtName\n" +
                    ", BSite.schname AS schoolName\n" +
                    ", MSP.siteID\n" +
                    ", MCSite.collectorID\n" +
                    ", MSC.sampleID\n" +
                    ", BC.gradeLevel, BC.subjectArea\n" +
                    ", RD.groupingID, RD.dataName, RD.dataValue, RD.dataType\n" +
                    "FROM map_site_project AS MSP\n" +
                    "LEFT JOIN bank_site 		AS BSite 	ON BSite.id =  MSP.siteID\n" +
                    "LEFT JOIN bank_project		AS BP		ON BP.id = MSP.projectID\n" +
                    "LEFT JOIN map_collector_site 	AS MCSite 	ON MCSite.siteID = MSP.siteID\n" +
                    "LEFT JOIN map_sample_collector 	AS MSC 		ON MSC.collectorID = MCSite.collectorID\n" +
                    "LEFT JOIN review_data		AS RD 		ON (\n" +
                    "   RD.sampleID = MSC.sampleID\n" +
                    "   AND RD.projectID = MSP.projectID\n" +
                    ")\n" +
                    "LEFT JOIN bank_collector 	AS BC 		ON BC.id = MSC.collectorID\n" +
                    "WHERE MSC.sampleID IS NOT NULL\n" +
                    "AND MSP.active = 'y'\n" +
                    "AND MCSite.active = 'y'\n" +
                    "AND BC.active = 'y'\n" +
                    "AND MSC.active = 'y'\n" +
                    "AND RD.active = 'y'\n" +
                    "AND RD.dataValue <> ''\n" +
                    "AND MSP.projectID = " + projectID + "\n" +
                    "AND MCSite.siteID = " + siteID + "\n" +
                    "AND BC.gradeLevel = "+ gradeLevel +"\n" +
                    //"AND BC.subjectArea = 'Mathematics'\n" +
                    "AND RD.dataName IN ('standard', 'counter')\n" +
                    "ORDER BY BP.name, BSite.disname, BSite.schname, \n" +
                    "BC.gradeLevel, BC.subjectArea, MSC.sampleID, \n" +
                    "RD.groupingID, RD.dataName, RD.dataValue ";
            
            

            ResultSet rs = main.DM.Execute(sql);
        
            Map<String, String> item    = new HashMap<>();
            Map<Integer, Integer> sample = new TreeMap<>(Collections.reverseOrder());
                         
            Map<String, Map<Integer, Integer>> sampleB = new TreeMap<>();
                                                 
        try {
            rs.beforeFirst();
            
            String currentCollectorID   = new String();
            String currentSampleID      = new String();
            String currentStandard      = new String(); 
            String currentSubjectArea   = new String();
            int currentGroupingID       = 0;  
            int currentGradeLevel       = 0;
            Boolean jump                = false;

                    
            while (rs.next()) {
                if (currentGroupingID > 0) {

                    if (rs.getInt("groupingID") != currentGroupingID) {
                        //currentGroupingID = rs.getString("groupingID");                        
                        jump = true;
                    }
                }
                
                if (jump == true) {

                    String stnd     = item.get("standard");
                    Integer hits    = new Integer(item.get("counter"));                    
                    String[] sagl   = stnd.split("_")[1].split("\\.");
                                         
                    total += hits;
                    
                    Integer agl = 0;
                    
                    if (sagl[0].equals("K")) {
                        ; //
                    } else {
                        agl = new Integer(sagl[0]);
                    }
                    
                    Integer drift = agl -currentGradeLevel;
                    
                    /*
                    if (sample.containsKey(drift)) {
                        Integer c = sample.get(drift);
                        sample.put(drift, c + hits);
                    }
                    else {
                        sample.put(drift, hits);
                    }
                    */
                    
                    if(sampleB.containsKey(currentSubjectArea)) {
                        if (sampleB.get(currentSubjectArea).containsKey(drift)) {
                            Integer c = sampleB.get(currentSubjectArea).get(drift);
                            sampleB.get(currentSubjectArea).put(drift, c + hits);
                        }
                        else {
                            sampleB.get(currentSubjectArea).put(drift, hits);
                        }
                    } else {
                        Map<Integer, Integer> dmap = new TreeMap<>(Collections.reverseOrder());
                        dmap.put(drift, hits);
                        sampleB.put(currentSubjectArea, dmap);
                    }

                    item.clear();
                    jump = false;
                }

                item.put(rs.getString("dataName"), rs.getString("dataValue"));

                currentCollectorID  = rs.getString("collectorID");
                currentSampleID     = rs.getString("sampleID");
                currentGroupingID   = rs.getInt("groupingID");               
                currentGradeLevel   = rs.getInt("gradeLevel");
                currentSubjectArea  = reduceSubject(rs.getString("subjectArea"));
            }                        
        } catch (SQLException ex) {
            Logger.getLogger(SCR_data.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        DMemeGrid g2 = convertToGridB(sampleB);
        //g2.DumpGrid();
        
        //System.out.println("\nTotal = " + total);
        
        return g2;
    }
    
    private String reduceSubject(String str) {
        String reduced = str;
        
        switch (str) {
            case "Mathematics":
                reduced = "Math";
                break;
                
            default:
                reduced = str;
                break;
        }
        
        return reduced;
    }
    
    
    
    private DMemeGrid convertToGridB(Map mp) {
        
        DMemeGrid grid = new DMemeGrid();
        
        int rowIdx = 0;
        int colIdx = 0;
        
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            
            Map<Integer, Integer> tmp = (TreeMap<Integer,Integer>)pairs.getValue();

            grid.addColLabel(pairs.getKey().toString());
            
            Iterator it2 = tmp.entrySet().iterator();
            rowIdx = 0;
            while (it2.hasNext()) {
               Map.Entry item = (Map.Entry) it2.next();
               
               Integer i = new Integer(item.getKey().toString());
               String label = item.getKey().toString();
               
               if (i > 0) {
                   label = String.format("+%d", i);
               }
               
               if (!grid.getRowLabels().contains(label)) {
                   grid.addRowLabel(label);
               }
                
               grid.addItem(rowIdx, colIdx, new DataMeme(item.getValue()));
               
               rowIdx++; 
            }
           colIdx++; 
        }
                        
        return grid;
    }
        
    
}
