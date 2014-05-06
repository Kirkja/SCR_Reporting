/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grantedsolutions.scr_reporter;

import com.learnerati.utilities.DocBuilder;
import com.learnerati.utilities.XMLBase;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;

/**
 *
 * @author Ben S. Jones
 */
public class FrontendController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Initialize the list of available accounts
        LoadAccounts();                
    }

    @FXML
    private ComboBox accounts;
    
    @FXML
    private ListView projects;
        
    @FXML
    private CheckBox projectReports;
           
    @FXML
    private CheckBox includeSiteBreakouts;
        
           
    @FXML
    private CheckBox siteReports;
          
    /**
     * 
     * @param event 
     */
    @FXML
    private void GenerateReports(ActionEvent event) {
         
        if (!accounts.getSelectionModel().isEmpty() &&
            !projects.getSelectionModel().isEmpty()) {
            
            
            System.out.printf("\nGenerate Reports: "
                   + "\nAccount: %s"
                   + "\nProject: %s "
                   + "\n\tproject level reporting: %s "
                   + "\n\tsite level breackouts: %s"
                   + "\nSite Reports: %s\n",
                   accounts.getSelectionModel().getSelectedItem().toString(),
                   projects.getSelectionModel().getSelectedItem().toString(),
                   projectReports.isSelected(),
                   includeSiteBreakouts.isSelected(),
                   siteReports.isSelected()
           );           
            
            Map<String, String> reportParams = new HashMap<>();
            reportParams.put("accountID", accounts.getSelectionModel().getSelectedItem().toString());
            reportParams.put("projectID", projects.getSelectionModel().getSelectedItem().toString());
            reportParams.put("projectReport", projectReports.isSelected() ? "true" : "false");
            reportParams.put("siteBreakouts", includeSiteBreakouts.isSelected() ? "true" : "false");
            reportParams.put("siteReports", siteReports.isSelected() ? "true" : "false");
            
            XMLBase xmlBase = new XMLBase().Create("document");
            
            SCR_report SCR = new SCR_report();
            SCR.setDocBase(new DocBuilder(xmlBase));
            SCR.generate(reportParams);
            
            
            
            
        }
        
        

        
        
        
    }
    
    
    
    /**
     * 
     * @param event 
     */
    @FXML
    private void FindProjects(ActionEvent event) {
        DropdownData selectedAccount = (DropdownData) accounts.getSelectionModel().getSelectedItem();
        LoadProjects(selectedAccount);
    }    
    
    
    
    /**
     * 
     * @param event 
     */
    @FXML
    private void Close(ActionEvent event) {
        main.app.close();
    }

    
    
    /**
     * 
     */
    private void LoadAccounts() {

        ObservableList<DropdownData> accountItems = FXCollections.observableArrayList();

        ResultSet rs = main.DM.Execute("SELECT * FROM bank_account ORDER BY title");

        try {
            rs.beforeFirst();

            while (rs.next()) {
                accountItems.add(new DropdownData(rs.getString("id"), rs.getString("title")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(FrontendController.class.getName()).log(Level.SEVERE, null, ex);
        }

        accounts.setItems(accountItems);
    }
    
    /**
     * 
     * @param selectedAccount 
     */
    private void LoadProjects(DropdownData selectedAccount) {

        ObservableList<DropdownData> projectItems = FXCollections.observableArrayList();

        ResultSet rs = main.DM.Execute("SELECT * FROM bank_project WHERE accountID='" + selectedAccount.value + "' ORDER BY name");

        try {
            rs.beforeFirst();

            while (rs.next()) {
                projectItems.add(new DropdownData(rs.getString("id"), rs.getString("name")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(FrontendController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        projects.setItems(projectItems);
    }    

}
