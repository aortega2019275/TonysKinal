/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.andreortega.controller;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import org.andreortega.system.MainApp;

/**
 * FXML Controller class
 *
 * @author javir
 */
public class LoginController implements Initializable {
    
    @FXML
    private JFXButton min;
    
    @FXML
    private JFXButton exit;
    
    @FXML
    private void exitp(ActionEvent event){  
    System.exit(0);
    }
    
    @FXML
    private void minimi(ActionEvent event){
        
        Stage myStage = (Stage) this.min.getScene().getWindow();
            myStage.setIconified(true);
            
 
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    void closeWindows() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    private MainApp sceneMain;

    public MainApp getSceneMain() {
        return sceneMain;
    }

    public void setSceneMain(MainApp sceneMain) {
        this.sceneMain = sceneMain;
    }

 public void mainMenu(){
     sceneMain.mainMenu();
 }
    
    
    
}
