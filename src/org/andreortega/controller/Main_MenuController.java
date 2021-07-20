/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.andreortega.controller;

import com.jfoenix.controls.JFXButton;


import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import org.andreortega.system.MainApp;

/**
 * FXML Controller class
 *
 * @author javir
 */
public class Main_MenuController implements Initializable {
    
    @FXML private JFXButton mini;
    
    private MainApp sceneMain;
    
    @FXML
    private void minimizar(ActionEvent event){  
        Stage myStage = (Stage) this.mini.getScene().getWindow();
            myStage.setIconified(true);
    }

    @FXML
    private void exitprog(ActionEvent event){  
    System.exit(0);
    }

   @FXML
    private void loginbt(ActionEvent event){
     /*   try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/andreortega/view/login.fxml"));
            Parent root = loader.load();
            
            LoginController controler = loader.getController();
            Scene scene = new Scene(root);
            Stage stage = new Stage();

            stage.setScene(scene); 
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initOwner(primaryStage);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            stage.setOnCloseRequest(e -> controler.closeWindows());
            
            

                
            
        } catch (IOException ex) {
            Logger.getLogger(Main_MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
    }
        
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO
    }    
    
    public MainApp getSceneMain(){
        return sceneMain;
    }
    
    public void SetSceneMain(MainApp sceneMain){
        this.sceneMain = sceneMain;
    }
    
    public void ventanaprueba(){
        sceneMain.ventanaprueba();
    }
    
    public void ventanaEmpresas(){
        sceneMain.ventanaEmpresas();
    }
    
    public void ventanaPlatos(){
        sceneMain.ventanaPlatos();
    }
    
    public void ventanaEmpleados(){
        sceneMain.ventanaEmpleados();
    }
    
    public void ventanaServicios(){
        sceneMain.ventanaServicios();
    }
    
    public void ventanaProductos(){
        sceneMain.ventanaProductos();
    }
   
    public void ventanaPresupuesto(){
        sceneMain.ventanaPresupuesto();
    }
    
    public void ventanaServicioshasPlatos(){
        sceneMain.ventanaServicioshasPlatos();
    }
    
    public void ventanaProductos_has_Platos(){
        sceneMain.ventanaProductos_has_Platos();
    }
    
    public void ventanaServicioshasEmpleados(){
        sceneMain.ventanaServicioshasEmpleados();
    }
}
