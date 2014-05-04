/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.grantedsolutions.scr_reporter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import com.learnerati.sql.DataManager;

/**
 *
 * @author Ben S. Jones
 */
public class main extends Application {
    
    public static Stage app;
    public static DataManager DM;
    
    
    @Override
    public void start(Stage stage) throws Exception {
        
        DM = new DataManager();

        try {
            DM.initPackage(DataManager.class, "database2.properties");
        } catch (Exception ex) {
            ;
        }        
        
        
        
        Parent root = FXMLLoader.load(getClass().getResource("FrontendGUI.fxml"));
        
        Scene scene = new Scene(root);
        stage.setTitle("SCR Reporting");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/grantedsolutions/resources/Logo_16x16.png")));
        
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        
        app = stage;        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
