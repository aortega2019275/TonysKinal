/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.andreortega.system;


import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;


import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.andreortega.controller.EmpleadosController;
import org.andreortega.controller.EmpresasController;
import org.andreortega.controller.LoginController;
import org.andreortega.controller.Main_MenuController;
import org.andreortega.controller.PlatosController;
import org.andreortega.controller.PresupuestoController;
import org.andreortega.controller.ProductosController;
import org.andreortega.controller.ProductoshasPlatosController;
import org.andreortega.controller.ServiciosController;
import org.andreortega.controller.ServicioshasEmpleadosController;
import org.andreortega.controller.ServicioshasPlatosController;


/**
 *
 * @author javir
 */
public class MainApp extends Application {
    
    
    private final String Package_View = "/org/andreortega/view/";
    private Stage sceneMain;
    private Scene scene;
    
    @Override
    public void start(Stage sceneMain) throws Exception{


        
        this.sceneMain = sceneMain;
        this.sceneMain.setTitle("Tonys kinal");
        sceneMain.initStyle(StageStyle.TRANSPARENT);
        sceneMain.getIcons().add(new Image("/org/andreortega/img/logoP.png"));        
        mainMenu();
        sceneMain.show();
        
        
       
      /*  try {
           Parent root =FXMLLoader.load(getClass().getResource("/org/andreortega/view/Main_Menu.fxml"));
            
            Scene scene = new Scene(root);
            scene.getStylesheets().addAll(getClass().getResource("/org/andreortega/css/main_menu.css").toExternalForm());
            primaryStage.initStyle(StageStyle.TRANSPARENT);
            primaryStage.setTitle("Menu principal");
            primaryStage.setScene(scene);
            primaryStage.show();
            
         
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }*/
    }
   public void mainMenu(){
    try{
        Main_MenuController mainMenu = (Main_MenuController)nextScene("Main_Menu.fxml",1280,720);
        mainMenu.SetSceneMain(this);
    }catch(Exception e){
        e.printStackTrace();
    }
      
   
}
    public void ventanaPlatos(){
        try{
            PlatosController platosv = (PlatosController)nextScene("Platos.fxml",1280,720);
            platosv.setSceneMain(this);
            
        }catch(Exception e){
            e.printStackTrace();
        }
    
    }
    
    public void ventanaEmpleados(){
        try{
            EmpleadosController empleadosv= (EmpleadosController)nextScene("Empleados.fxml",1280,720);
            empleadosv.setSceneMain(this);
        
        
        }catch(Exception e){
            e.printStackTrace();
           }
    
    }
   
   public void  ventanaprueba(){
       try{
           LoginController login = (LoginController)nextScene("DatosP.fxml",1229,616);
           login.setSceneMain(this);
       
       }catch(Exception e){
           e.printStackTrace();
       }
   }
   
      public void ventanaEmpresas(){
       try{
           EmpresasController empresa = (EmpresasController)nextScene("Empresas.fxml",1280,720);
           empresa.setSceneMain(this);
       }catch(Exception e){
           e.printStackTrace();
       }
    }
      
      public void ventanaServicios(){
        try{
            ServiciosController servicio = (ServiciosController)nextScene("Servicios.fxml",1280,720);
            servicio.setSceneMain(this);
        }catch(Exception e){
            e.printStackTrace();
        }
      }
     
     public void ventanaProductos(){
         try{
            ProductosController productos = (ProductosController)nextScene("Productos.fxml",1280,720);
            productos.setSceneMain(this);
         
        }catch(Exception e){
            e.printStackTrace();
        }
     }
     
     public void ventanaPresupuesto(){
        try{
            PresupuestoController presupuesto = (PresupuestoController)nextScene("Presupuesto.fxml",1280,720);
            presupuesto.setSceneMain(this);
        }catch(Exception e){
            e.printStackTrace();
        }
     }
     
     public void ventanaServicioshasPlatos(){
        try{
            ServicioshasPlatosController ShasP = (ServicioshasPlatosController)nextScene("ServicioshasPlatos.fxml",800,600);
            ShasP.setSceneMain(this);
        }catch(Exception e){
            e.printStackTrace();
        }
     }
     
     public void ventanaProductos_has_Platos(){
        try{
            ProductoshasPlatosController PhasP = (ProductoshasPlatosController)nextScene("ProductoshasPlatos.fxml",800,600);
            PhasP.setSceneMain(this);
        }catch(Exception e){
            e.printStackTrace();
        }
     }
     
    public void ventanaServicioshasEmpleados(){
        try{
            ServicioshasEmpleadosController ShasE = (ServicioshasEmpleadosController)nextScene("ServicioshasEmpleados.fxml",1280,720);
            ShasE.setSceneMain(this);
      }catch(Exception e){
            e.printStackTrace();
        }
     }
    /** 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
        
    }
    public Initializable nextScene(String fxml,int ancho, int alto) throws Exception{
        

        Initializable result = null;
        FXMLLoader loadFXML = new FXMLLoader();
        InputStream archivo = MainApp.class.getResourceAsStream(Package_View+fxml);
        loadFXML.setBuilderFactory(new JavaFXBuilderFactory());
        loadFXML.setLocation(MainApp.class.getResource(Package_View+fxml));
        scene = new Scene((AnchorPane)loadFXML.load(archivo),ancho,alto);
        
        sceneMain.setScene(scene);
        sceneMain.sizeToScene();
        result = (Initializable)loadFXML.getController(); 
        return result;
    }
}
