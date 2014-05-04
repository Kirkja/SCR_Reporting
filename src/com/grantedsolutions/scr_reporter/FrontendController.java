/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.grantedsolutions.scr_reporter;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
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

        LoadAccounts();
        
        
    }

    @FXML
    private ComboBox accounts;
    
    @FXML
    private ListView projects;
        
    @FXML
    private CheckBox projectLevelCheckbox;
           
    @FXML
    private CheckBox siteLevelCheckbox;
        
    
    
    @FXML
    private void GenerateReports(ActionEvent event) {
                
        System.out.printf("\nGenerate Reports: "
                + "\nAccount: %s"
                + "\nProject: %s "
                + "\n\tproject level reporting: %s "
                + "\n\tite level reporting: %s",
                accounts.getSelectionModel().getSelectedItem().toString(),
                projects.getSelectionModel().getSelectedItem().toString(),
                projectLevelCheckbox.isSelected(),
                siteLevelCheckbox.isSelected()        
        );
        
        
        
    }
    
    
    
    
    @FXML
    private void FindProjects(ActionEvent event) {
        DropdownData selectedAccount = (DropdownData) accounts.getSelectionModel().getSelectedItem();
        LoadProjects(selectedAccount);
    }    
    
    
    
    
    @FXML
    private void Close(ActionEvent event) {
        main.app.close();
    }

    
    
    
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
