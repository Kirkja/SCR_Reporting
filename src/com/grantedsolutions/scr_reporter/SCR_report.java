/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grantedsolutions.scr_reporter;

import com.learnerati.sql.DataManager;
import com.learnerati.utilities.DocBuilder;
import com.learnerati.utilities.ReadProperties;
import com.learnerati.utilities.XMLBase;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Element;

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

        props = new ReadProperties("output.properties").Fetch();        
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

        String sqlA = "SELECT\n"
                + "  BP.accountID, BA.title AS accountName\n"
                + ", BP.id AS projectID, BP.name AS projectName\n"
                + ", MSP.siteID, BS.disname AS districtName, BS.schName AS siteName\n"
                + "FROM map_site_project AS MSP\n"
                + "LEFT JOIN bank_project AS BP ON BP.id = MSP.projectID\n"
                + "LEFT JOIN bank_account AS BA ON BA.id = BP.accountID\n"
                + "LEFT JOIN bank_site AS BS ON BS.id = MSP.siteID\n"
                + "WHERE MSP.active = 'y'\n"
                + "AND BP.active = 'y'\n"
                + "AND BP.accountID = " + accountID + " \n"
                + "AND BP.id = " + projectID + " \n"
                + "GROUP BY BP.accountID, BP.id, MSP.siteID";

        
        //System.out.println(sqlA);
        
        ResultSet rs = DM.Execute(sqlA);
        
        try {
            rs.beforeFirst();

            while (rs.next()) {

                String projectName  = rs.getString("projectName");
                String siteID       = rs.getString("siteID");
                String districtName = rs.getString("districtName");
                String siteName     = rs.getString("siteName");
                                
                System.out.printf("\n%s\t%s\t%s\t%s", projectName, districtName, siteName, siteID);
                
                
                
                Map<String, String> configOptions = new HashMap<>();
                configOptions.put("base-image-directory",   "c:/GS_ROOT/images/");
                configOptions.put("base-font-directory",    "c:/GS_ROOT/fonts/");
                configOptions.put("base-doc-directory",     "c:/GS_ROOT/docs/");
                configOptions.put("number-of-columns",      "2"); 
                
                configOptions.put("chart-directory",     "c:/GS_ROOT/charts/");
                configOptions.put("table-directory",     "c:/GS_ROOT/tables/");

                Map<String, String> coverOptions = new HashMap<>();
                coverOptions.put("title-A", projectName);
                coverOptions.put("title-B", districtName);
                coverOptions.put("title-C", "SCR Analysis");        
                coverOptions.put("title-sub", siteName);
                coverOptions.put("published-by", "Granted Solutions");

                Map<String, String> abstractOptions = new HashMap<>();
                abstractOptions.put("title", "abstract title here");
                abstractOptions.put("body", "abstract body here");
                               
                                
                XMLBase xmlBase = new XMLBase().Create("document");
                
                DocBuilder doc = new DocBuilder(xmlBase);
                doc.SetConfig(configOptions);
                doc.SetCover(coverOptions);
                doc.SetAbstract(abstractOptions);
                
                setDocBase(doc);
                
                siteReport(
                        accountID,
                        projectID,
                        projectName,
                        districtName,
                        siteName,
                        siteID
                );
                
                
                xmlBase.AsFile("C:/GS_ROOT/xml", siteName + ".xml");
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(SCR_report.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    
    
    private void siteReport(
        String accountID,
        String projectID,
        String projectName,
        String districtName,
        String siteName,
        String siteID
    ) {
        
        
        Element section_I = base.CreateSection("Introduction");
        Element section_II = base.CreateSection("Brief Overview");
        Element section_III = base.CreateSection("Reading the Report");
        Element section_IV;// = base.CreateSection("Results");
        Element appendix = base.CreateSection("Appendices");

        section_I.appendChild(base.PullExternal(props.getProperty("file.inbase") + "insert_files/CCSS_Intro.txt"));
        section_II.appendChild(base.PullExternal(props.getProperty("file.inbase") + "insert_files/CCSS_Intro.txt"));
        section_III.appendChild(base.PullExternal(props.getProperty("file.inbase") + "insert_files/CCSS_Intro.txt"));
        //section_IV.appendChild(base.PullExternal(props.getProperty("file.inbase") + "insert_files/CCSS_Intro.txt"));

        //Map<String, Element> mainResults = gatherCCSSResults();        
        //section_IV = mainResults.get("data");
        
        Map<String, Element> results = parseSite(accountID, projectID, siteID);
        section_IV = results.get("data");

        base.root.appendChild(section_I);
        base.root.appendChild(section_II);
        base.root.appendChild(section_III);
        base.root.appendChild(section_IV);
        base.root.appendChild(appendix);         
                
    }
    
    
    
    private Map<String, Element> parseSite(
          String accountID
        , String projectID
        , String siteID) 
    {
        
        Map<String, Element> tmp = new HashMap<>();
        
        
        String sqlA = "SELECT \n" +
                    //"  MSP.projectID \n" +
                    //", MSP.siteID, BS.disname, BS.schname \n" +
                    "  BC.gradeLevel \n" +
                    ", BC.subjectArea \n" +
                    "FROM `bank_collector` AS BC \n" +
                    "LEFT JOIN `map_sample_collector` AS MSC ON MSC.collectorID = BC.id \n" +
                    "LEFT JOIN map_collector_site MCS ON MCS.collectorID = MSC.collectorID \n" +
                    "LEFT JOIN map_site_project MSP ON MSP.siteID = MCS.siteID \n" +
                    "LEFT JOIN bank_site AS BS ON BS.id = MSP.siteID\n" +
                    "WHERE BS.active = 'y' \n" +
                    "AND MSP.active = 'y' \n" +
                    "AND MCS.active = 'y' \n" +
                    "AND MSC.active = 'y' \n" +
                    "AND BC.active = 'y' \n" +
                    "AND MSP.projectID 	= " + projectID +"\n" +
                    "AND MSP.siteID     = " + siteID + "\n" +
                    "GROUP BY BS.disname, BS.schname, BC.gradeLevel, BC.subjectArea \n" +
                    "ORDER BY BS.disname, BS.schname, BC.gradeLevel, BC.subjectArea";
        
        ResultSet rs = DM.Execute(sqlA);
        
        String curGradeLevel    = "";
        String curSubjectArea   = "";
        
        Element curGrade        = null;
        Element curSubject      = null;
        
        Element holder          = base.CreateSection("School Results");

        try {
            rs.beforeFirst();

            while (rs.next()) {
                                
                if (curGrade == null) {
                    curGradeLevel = rs.getString("gradeLevel");
                    
                    curGrade = base.CreateSection("Grade " + rs.getString("gradeLevel"));
                    //curGrade.setAttribute("bump", "true");
                    curGrade.appendChild(base.PullExternal(props.getProperty("file.inbase") + "insert_files/grade_filler.txt"));
                    
                    System.out.println("\tGrade: " + rs.getString("gradeLevel")); 
                    
                    curGrade.appendChild(
                        base.Base().ImportFragmentString(
                            createChart(
                                 "Grade Level Analysis"   
                                , projectID
                                , siteID
                                , curGradeLevel
                                , "gla"
                            )
                        )
                    );   
                        
                    curGrade.appendChild(
                        base.Base().ImportFragmentString(
                            createTable(
                                  projectID
                                , siteID
                                , curGradeLevel
                                , curSubjectArea
                                , "simple"
                            )
                        )
                    );                         
                }  
                
                if (curSubject == null) {
                    curSubjectArea = rs.getString("subjectArea");
                    curSubject = base.CreateSection(rs.getString("subjectArea"));
                    curSubject.appendChild(base.PullExternal(props.getProperty("file.inbase") + "insert_files/subject_filler.txt"));
                    System.out.println("\tSubject: " + rs.getString("subjectArea"));  
                    
                    curSubject.appendChild(
                        base.Base().ImportFragmentString(
                            createChart(
                                  String.format("Cognitive Rigor: %s (Grade %s)",curSubjectArea , curGradeLevel) 
                                , projectID
                                , siteID
                                , curGradeLevel
                                , curSubjectArea)
                        )
                    ); 
                    
                    curSubject.appendChild(
                        base.Base().ImportFragmentString(
                            createTable(
                                projectID
                                , siteID
                                , curGradeLevel
                                , curSubjectArea
                                , "detail"
                            )
                        )
                    );                     
                    
                    
                }                 
                
                
                
                if (!curSubjectArea.equals(rs.getString("subjectArea"))) {

                    curGrade.appendChild(curSubject);
                    
                    curSubject = base.CreateSection(rs.getString("subjectArea"));
                    //curSubject.setAttribute("bump", "true");
                    curSubject.appendChild(base.PullExternal(props.getProperty("file.inbase") + "insert_files/subject_filler.txt"));

                    System.out.println("\t\tSubject area: " + rs.getString("subjectArea")); 
                    System.out.println("\t\t[Generate CR for " + rs.getString("subjectArea")  +"]");
                    
                    curSubject.appendChild(
                        base.Base().ImportFragmentString(
                            createChart(
                                  String.format("Cognitive Rigor: %s (Grade %s)",curSubjectArea , curGradeLevel) 
                                , projectID
                                , siteID
                                , curGradeLevel
                                , curSubjectArea)
                        )
                    ); 
                    
                    curSubject.appendChild(
                        base.Base().ImportFragmentString(
                            createTable(
                                projectID
                                , siteID
                                , curGradeLevel
                                , curSubjectArea
                                , "detail"                                    
                            )
                        )
                    );                    
                }
                 
                
                if (!curGradeLevel.equals(rs.getString("gradeLevel"))) {

                    curGrade.appendChild(curSubject);
                    holder.appendChild(curGrade);
                    
                    curGrade = base.CreateSection("Grade " + rs.getString("gradeLevel"));
                    //curGrade.setAttribute("bump", "true");
                    curGrade.appendChild(base.PullExternal(props.getProperty("file.inbase") + "insert_files/grade_filler.txt"));

                    System.out.println("\tGrade level: " + rs.getString("gradeLevel"));
                                                           
                    curGrade.appendChild(
                        base.Base().ImportFragmentString(
                            createChart(
                                  "Grade Level Anlaysis"
                                , projectID
                                , siteID
                                , curGradeLevel
                                , "gla")
                        )
                    ); 
                    
                    curGrade.appendChild(
                        base.Base().ImportFragmentString(
                            createTable(
                                projectID
                                , siteID
                                , curGradeLevel
                                , curSubjectArea
                                , "simple"
                            )
                        )
                    ); 
                    
                    
                }               
                
                
                
                curGradeLevel = rs.getString("gradeLevel");
                curSubjectArea = rs.getString("subjectArea");                
            }
            
            curGrade.appendChild(curSubject);                
            holder.appendChild(curGrade);            
            
        } catch (SQLException ex) {
            Logger.getLogger(SCR_report.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        tmp.put("data", holder);
        
        return tmp;
    }

    
    

    private String createChart(
              String title
            , String projectID
            , String siteID
            , String gradeLevel
            , String subjectArea) 
    {
        StringBuilder str = new StringBuilder();
        
        
        String chartName = "";
        
        switch(subjectArea.toLowerCase()) {
            case "mathematics":
                chartName = "chart_holder_math.svg";
                break;
                
            case "math":
                chartName = "chart_holder_mth.svg";
                break;
                
            case "english":
                chartName = "chart_holder_ela.svg";
                break;
                
            case "science":
                chartName = "chart_holder_sci.svg";
                break;
                
            case "history":
                chartName = "chart_holder_soc.svg";
                break;
                
            case "gla":
                chartName = "chart_holder_gla.svg";
                break;
                
            case "dok-drift":
                chartName = "chart_holder_dok-drift.svg";
                break; 
                
            case "blm-drift":
                chartName = "chart_holder_blm-drift.svg";
                break; 
                
            default:
                 chartName = "chart_holder.svg";
                break;
        }
        
        
        
        str.append("<chart index='true'>");
        str.append("<title>");
        str.append(title);
        str.append("</title>");
        str.append("<width>100%</width>");
        str.append("<source>");
        str.append(chartName); 
        str.append("</source>");
        str.append("<caption>");
        str.append("Some caption here");
        str.append("</caption>");
        str.append("</chart>");
        
        return str.toString();
    }
    
    
    
    
    private String createTable(String projectID
            , String siteID
            , String gradeLevel
            , String subjectArea
            , String type) 
    {
        String str = "";
        
        switch (type) {
            case "detail":
                 str = base.loadAsString("C:/GS_ROOT/tables/table_holder_detail.xml");
                break;
                
            case "simple":
                str = base.loadAsString("C:/GS_ROOT/tables/table_holder_simple.xml");
                break;
                
            default:
                str = base.loadAsString("C:/GS_ROOT/tables/table_holder_simple.xml");
                break;
        }
               
        return str;
    }    
    
    
    
    
}
