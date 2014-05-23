/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grantedsolutions.scr_reporter;

import com.grantedsolutions.chart.GridToChart;
import com.grantedsolutions.chart.GridToGLA;
import com.grantedsolutions.chart.GridToRigor;
import com.grantedsolutions.chart.GridToTable;
import com.grantedsolutions.chart.SVGBase;
import com.learnerati.datameme.DMemeGrid;
import com.learnerati.sql.DataManager;
import com.learnerati.utilities.DocBuilder;
import com.learnerati.utilities.ReadProperties;
import com.learnerati.utilities.XMLBase;
import com.learnerati.utilities.recoding;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.DocumentFragment;
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

    
    
    
    /**
     * 
     * @param accountID
     * @param projectID 
     */
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
                configOptions.put("chart-directory",        "c:/GS_ROOT/charts/");
                configOptions.put("table-directory",        "c:/GS_ROOT/tables/");                
                configOptions.put("run-directory",          "c:/GS_ROOT/SCR_RUN/");

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
        //Map<String, Element> results = parseSite(accountID, projectID, siteID);
        
        Map<String, Element> results = parseSite2(accountID, projectID, siteID);
        
        section_IV = results.get("data");

        base.root.appendChild(section_I);
        base.root.appendChild(section_II);
        base.root.appendChild(section_III);
        base.root.appendChild(section_IV);
        base.root.appendChild(appendix);         
                
    }
    
    
    
    

    
    /*

    private String createChart(
              String title
            , String projectID
            , String siteID
            , String gradeLevel
            , String subjectArea) 
    {
        StringBuilder str = new StringBuilder();
        
        SCR_data data = new SCR_data();
        
        String chartName = "";
        String tableEmbed = "";
        
        switch(subjectArea.toLowerCase()) {
            case "mathematics":
                chartName = "chart_holder_math.svg";
                tableEmbed = "";
                break;
                
            case "math":
                chartName = "chart_holder_mth.svg";
                tableEmbed = "";
                break;
                
            case "english":
                chartName = "chart_holder_ela.svg";
                tableEmbed = "";
                break;
                
            case "science":
                chartName = "chart_holder_sci.svg";
                tableEmbed = "";
                break;
                
            case "history":
                chartName = "chart_holder_soc.svg";
                tableEmbed = "";
                break;
                
            case "gla":
                DMemeGrid dataGrid = data.getStandardData(projectID, siteID, gradeLevel);
                dataGrid.setRowDescriptor("Grade Level Drift");
                dataGrid.setColDescriptor("Collected Subject Areas");        
                                
                chartName  = glaChart(projectID, siteID, gradeLevel, dataGrid);
                tableEmbed = glaTable(projectID, siteID, gradeLevel, dataGrid);
                
                break;
                
            case "dok-drift":
                chartName = "chart_holder_dok-drift.svg";
                tableEmbed = "";
                break; 
                
            case "blm-drift":
                chartName = "chart_holder_blm-drift.svg";
                tableEmbed = "";
                break; 
                
            default:
                 chartName = "chart_holder.svg";
                 tableEmbed = "";
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
    */
    
    /*
    
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
    
    */
    
    
    
    
    //=======================================================================
    //
    //
    
    private Map<String, Element> parseSite2 (
              String accountID
            , String projectID
            , String siteID) 
    {
        
        //SCR_data data = new SCR_data();
        
        Map<String, Element> tmp = new HashMap<>();
        String curGradeLevel    = "";
        String curSubjectArea   = "";
        
        Element gradeSection    = null;
        Element subjectSection  = null;        
        Element holder          = base.CreateSection("Site Results");
                
        
        String sqlA = "SELECT \n" +
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
        
        boolean first = true;
        String gradeLevel = "";
        String subjectArea = "";
                
        try {
            rs.beforeFirst();

            while (rs.next()) {
                
                // Deal with first loop ---------------------------------------
                if (first) { 
                   
                   gradeLevel   = rs.getString("gradeLevel"); 
                   subjectArea  = rs.getString("subjectArea");
                   
                   gradeSection     = base.CreateSection(String.format("%s", recoding.GradelevelLabel(gradeLevel)));
                   subjectSection   = base.CreateSection(String.format("%s", subjectArea));
                   
                   // append in the needed GLA content
                   String title = String.format("Drift: %s", recoding.GradelevelLabel(gradeLevel));                   
                   gradeSection.appendChild(generateGLA(projectID, siteID, gradeLevel, title));
                                      
                   // append in the needed CR content
                   title = String.format("CR: %s", recoding.GradelevelLabel(gradeLevel));
                   subjectSection.appendChild(generateRigor(projectID, siteID, gradeLevel, subjectArea, title));
                                      
                   
                   first = false;                    
                }
                      
               
               // Deal with grade level changes -------------------------------  
               if (!rs.getString("gradeLevel").equals(gradeLevel)) {
                   // Append the current subject section to the current grade section
                   gradeSection.appendChild(subjectSection);
                   
                   // Append the current grade section to the holder element
                   holder.appendChild(gradeSection);
                   
                   // Reset the watch variables
                   gradeLevel   = rs.getString("gradeLevel"); 
                   subjectArea  = rs.getString("subjectArea");                   
                   
                   // Create new sections
                   gradeSection     = base.CreateSection(String.format("%s", recoding.GradelevelLabel(gradeLevel)));
                   subjectSection   = base.CreateSection(String.format("%s", subjectArea));                   
                      
                   // append in the needed GLA content
                   String title = String.format("Drift: %s", recoding.GradelevelLabel(gradeLevel));                   
                   gradeSection.appendChild(generateGLA(projectID, siteID, gradeLevel, title));    
                   
                   // append in the needed CR content
                   title = String.format("CR: %s", recoding.GradelevelLabel(gradeLevel));
                   subjectSection.appendChild(generateRigor(projectID, siteID, gradeLevel, subjectArea, title));
                                      
                   
               }
               
               
               // Deal with subject area changes ------------------------------
               if (!rs.getString("subjectArea").equals(subjectArea)) {
                   // Apend current subject section to current grade section
                   gradeSection.appendChild(subjectSection);
                   
                   // Reset watch variables
                   subjectArea  = rs.getString("subjectArea");
                   
                   // Create new sections
                   subjectSection   = base.CreateSection(String.format("%s", subjectArea));
                   
                   // append in the needed content
                   String title = String.format("CR: %s", recoding.GradelevelLabel(gradeLevel));
                   subjectSection.appendChild(generateRigor(projectID, siteID, gradeLevel, subjectArea, title));
                   
               }                               
            }   
                        
            // Append remaining open sections            
            gradeSection.appendChild(subjectSection);
            holder.appendChild(gradeSection);
            
        } catch (SQLException ex) {
            Logger.getLogger(SCR_report.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        tmp.put("data", holder);
        
        return tmp;
    }     
    
    
    
    private DocumentFragment generateRigor(
            String projectID
            , String siteID
            , String gradeLevel
            , String subjectArea
            , String title) 
    {
    
        String caption = "";
        StringBuilder str = new StringBuilder();
                
        SCR_data data = new SCR_data();
        
        DMemeGrid dataGrid = data.getRigorData(projectID, siteID, gradeLevel, subjectArea);
        dataGrid.setLabel(title);
        dataGrid.setRowDescriptor("Depth of Knowledge");
        dataGrid.setColDescriptor("Bloom's Taxonomy (Revised)"); 
                
        String source = crChart(projectID, siteID, gradeLevel, subjectArea, dataGrid);
        
        str.append("<chart index='true'>");
        str.append("<title>");
        str.append(title);
        str.append("</title>");
        str.append("<height>3.00in</height>");
        str.append("<width>75%</width>");
        str.append("<source>");
        str.append(source); 
        str.append("</source>");
        str.append("<caption>");
        str.append(caption);
        str.append("</caption>");
        str.append("</chart>");      
        
        str.append(crTable(projectID, siteID, gradeLevel, subjectArea, dataGrid));
        
        DocumentFragment df = base.base.ImportFragmentString(str.toString());
                
        return df;      
    }
    
    
    /**
     * 
     * @param projectID
     * @param siteID
     * @param gradeLevel
     * @param title
     * @return 
     */
    private DocumentFragment generateGLA(
            String projectID
            , String siteID
            , String gradeLevel
            , String title) 
    {
                
        String caption = "";
        
        SCR_data data = new SCR_data();
        
        DMemeGrid dataGrid = data.getStandardData(projectID, siteID, gradeLevel);
        dataGrid.setLabel(title);
        dataGrid.setRowDescriptor("Grade Level Drift");
        dataGrid.setColDescriptor("Collected Subject Areas"); 
                
        String source = glaChart(projectID, siteID, gradeLevel, dataGrid);
        
        
        int cols = dataGrid.Cols();
        
        int percent = 20 * cols;
        
        StringBuilder str = new StringBuilder();
        
        str.append("<chart index='true'>");
        str.append("<title>");
        str.append(title);
        str.append("</title>");
        str.append("<height>3.00in</height>");
        str.append("<width>");
        str.append(percent);
        str.append("%</width>");
        str.append("<source>");
        str.append(source); 
        str.append("</source>");
        str.append("<caption>");
        str.append(caption);
        str.append("</caption>");
        str.append("</chart>");      
        
        str.append(glaTable(projectID, siteID, gradeLevel, dataGrid));
                
        DocumentFragment df = base.base.ImportFragmentString(str.toString());
                
        return df;        
    }
    

    private String glaChart(
            String projectID, 
            String siteID, 
            String gradeLevel,
            DMemeGrid dataGrid) 
    {
                
        String path = String.format("%s/%s", projectID, siteID);
        String name = String.format("GLA_%s.svg",gradeLevel);
        String root = "C:/GS_ROOT/SCR_RUN/";
                
        // define some rules to use for the chart
        Map<String,Object> rules = new HashMap<>(); 
        rules.put("OutRoot", root);
        rules.put("OutFilePath", path);
        rules.put("OutFileName", name);
        rules.put("UseValueData", "true");
        rules.put("UseCountData", "false");    

        ChartWriter(dataGrid, rules); 
                
        //rules.put("OutFileName", String.format("GLA_%s_tbl.xml", gradeLevel));
        //TableWriter(dataGrid, rules);        
                
        return path +"/"+ name;
    }
    
    
    
    
    private String glaTable(
            String projectID, 
            String siteID, 
            String gradeLevel,
            DMemeGrid dataGrid) 
    {
        
        
        String path = String.format("%s/%s", projectID, siteID);
        String name = String.format("GLA_%s_tbl.xml", gradeLevel);
        String root = "C:/GS_ROOT/SCR_RUN/";
                
        // define some rules to use for the chart
        Map<String,Object> rules = new HashMap<>(); 
        rules.put("OutRoot", root);
        rules.put("OutFilePath", path);
        rules.put("OutFileName", name);
        rules.put("UseValueData", "true");
        rules.put("UseCountData", "false");    

        String table = TableWriter(dataGrid, rules);        
                
        return table;
    }
    
    
    private String crTable(
            String projectID, 
            String siteID, 
            String gradeLevel,
            String subjectArea,
            DMemeGrid dataGrid) 
    {
        
        
        String path = String.format("%s/%s", projectID, siteID);
        String name = String.format("CR_%s_%S_tbl.xml", gradeLevel, subjectArea);
        String root = "C:/GS_ROOT/SCR_RUN/";
                
        // define some rules to use for the chart
        Map<String,Object> rules = new HashMap<>(); 
        rules.put("OutRoot", root);
        rules.put("OutFilePath", path);
        rules.put("OutFileName", name);
        rules.put("UseValueData", "true");
        rules.put("UseCountData", "false");    

        String table = TableWriter(dataGrid, rules);        
                
        return table;
    }
    
    
    
    
    
    private void ChartWriter(DMemeGrid grid, Map<String, Object> rules) {
        
        Map<String, Object> params = new HashMap<>();
        params.put("vertical-anchor", "top");
        
        SVGBase svgb = new SVGBase().Create();

        GridToGLA graph = new GridToGLA(svgb);
        graph.setRules(rules);        
        graph.Build(grid);

        graph.ToFile(rules.get("OutRoot").toString()
                + rules.get("OutFilePath").toString()
                , rules.get("OutFileName").toString());        
    }   
    
    private void ChartWriterCR(DMemeGrid grid, Map<String, Object> rules) {
        
        Map<String, Object> params = new HashMap<>();
        params.put("vertical-anchor", "top");
        
        SVGBase svgb = new SVGBase().Create();

        GridToRigor graph = new GridToRigor(svgb);
        graph.setRules(rules);        
        graph.Build(grid);

        graph.ToFile(rules.get("OutRoot").toString()
                + rules.get("OutFilePath").toString()
                , rules.get("OutFileName").toString());        
    }      
    
    
    private String TableWriter(DMemeGrid grid, Map<String, Object> rules) {
        
        Map<String,String> special = new HashMap<>();
        special.put("col-header-width", "0.60in");
        XMLBase tableBase = new XMLBase().Create("table");
        
        GridToTable table = new GridToTable(tableBase);
        table.Index(true);
        table.Form("detail");
        table.setSpecial(special);
        table.Load(grid);        
        table.Build();
        
        table.ToFile(rules.get("OutRoot").toString() 
                + rules.get("OutFilePath").toString()
                , rules.get("OutFileName").toString());
        
        
        return table.Stripped();
    }
    
    

    private String crChart(
            String projectID, 
            String siteID, 
            String gradeLevel,
            String subjectArea,
            DMemeGrid dataGrid) 
    {
                
        String path = String.format("%s/%s", projectID, siteID);
        String name = String.format("CR_%s_%s.svg", subjectArea, gradeLevel);
        String root = "C:/GS_ROOT/SCR_RUN/";
                
        // define some rules to use for the chart
        Map<String,Object> rules = new HashMap<>(); 
        rules.put("OutRoot", root);
        rules.put("OutFilePath", path);
        rules.put("OutFileName", name);
        rules.put("UseValueData", "true");
        rules.put("UseCountData", "false");    

        ChartWriterCR(dataGrid, rules); 
                
        //rules.put("OutFileName", String.format("GLA_%s_tbl.xml", gradeLevel));
        //TableWriter(dataGrid, rules);        
                
        return path +"/"+ name;
    }
        
    
}
