/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.andreortega.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import org.andreortega.system.MainApp;
import org.andreortega.bd.Conexion;
import org.andreortega.bean.Empresa;
import org.andreortega.report.GenerarReporte;


public class EmpresasController implements Initializable {
    
    private final String log="/org/andreortega/img/IconB.png";
    private final String bot="/org/andreortega/img/fondo1.png";
    private final String top="/org/andreortega/img/fondo2.png";
    
    private MainApp sceneMain;
    @FXML private Button rett;

    private enum  Operacion{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, NINGUNO};
    private Operacion tipoOperacion = Operacion.NINGUNO;
    
    @FXML private TableColumn coltelefonoEmpresa;

    @FXML private TableColumn coldireccionEmpresa;

    @FXML private TableColumn colnombreEmpresa;

    @FXML private TableColumn colcodigoEmpresa;

    @FXML private JFXButton btnEliminar;
    
    @FXML private JFXButton btnReportar;

    @FXML private JFXButton btnNuevo;

    @FXML private JFXButton btnEditar;
    
    @FXML private JFXTextField telefono;

    @FXML private JFXTextField direccion;

    @FXML private JFXTextField nombre;
    
    @FXML private JFXTextField codigo;

    @FXML private TableView tblEmpresa;
    
    private ObservableList<Empresa> listaEmpresa;

    
    
    public void desactivarControles(){
        codigo.setEditable(false);
        nombre.setEditable(false);
        direccion.setEditable(false);
        telefono.setEditable(false);
        
    }
    public void activarControles(){
       // codigo.setEditable(true);
        nombre.setEditable(true);
        direccion.setEditable(true);
        telefono.setEditable(true); 
    }
    
    private void limpiarControles(){
        codigo.setText("");
        nombre.setText("");
        direccion.setText("");
        telefono.setText(""); 
    }
    
    public void nuevo(){
        switch(tipoOperacion){
        case NINGUNO:
                activarControles();
                limpiarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReportar.setDisable(true);
                tipoOperacion= Operacion.GUARDAR;
                
                    break;
        case GUARDAR:
                guardar();
                desactivarControles();
                cargarDatos();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReportar.setDisable(false);
                tipoOperacion= Operacion.NINGUNO;
                    break;     
    }
}
    public void generarReporte(){
    switch(tipoOperacion){
        case NINGUNO:
            imprimirReporte();
            break;
    }
}

    public void imprimirReporte(){  
    Map parametros = new HashMap();
    parametros.put("log", this.getClass().getResourceAsStream(log));
    parametros.put("bot", this.getClass().getResourceAsStream(bot));
    parametros.put("top", this.getClass().getResourceAsStream(top));
    parametros.put("codigoEmpresa", null);
    GenerarReporte.mostrarReporte("ReporteEmpresas.jasper", "Reporte de Empresas", parametros);
    }
    
    public void editarEmpresa(){
        switch(tipoOperacion){
            case NINGUNO:
                if(tblEmpresa.getSelectionModel().getSelectedItem() !=null){
                btnEditar.setText("Actualizar");
                btnEliminar.setText("Cancelar");
                btnReportar.setDisable(true);
                btnNuevo.setDisable(true);
                activarControles();
                tipoOperacion = Operacion.ACTUALIZAR;
                
                }else{
                
                JOptionPane.showMessageDialog(null, "debe seleccionar algun campo");
                }
                break;
                
            case ACTUALIZAR:
                actualizar();
                cargarDatos();
                desactivarControles();
                limpiarControles();
                btnEditar.setText("Editar");
                btnEliminar.setText("Eliminar");
                btnReportar.setDisable(false);
                btnNuevo.setDisable(false);
                tipoOperacion = Operacion.NINGUNO;
                
                break;
        }
    } 
    
    public void actualizar(){
        
        try{
        PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarEmpresas(?,?,?,?)}");
        Empresa empAct = ((Empresa)tblEmpresa.getSelectionModel().getSelectedItem());
        //Obtener Datos para DB
        empAct.setNombreEmpresa(nombre.getText());
        empAct.setDireccion(direccion.getText());
        empAct.setTelefono(telefono.getText());
        
        // Enviar Datos DB
        sp.setInt(1, empAct.getCodigoEmpresa());
        sp.setString(2, empAct.getNombreEmpresa());
        sp.setString(3, empAct.getDireccion());
        sp.setString(4, empAct.getTelefono());
        sp.execute();
        JOptionPane.showMessageDialog(null, "actualizado");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    @FXML
    public void eliminarEmpresa(){
    switch(tipoOperacion){
        case GUARDAR:
            desactivarControles();
            limpiarControles();
            btnNuevo.setText("Nuevo");
            btnEliminar.setText("Eliminar");
            btnEditar.setDisable(false);
            btnReportar.setDisable(false);
            tipoOperacion= Operacion.NINGUNO;
                    break;
        case ACTUALIZAR:  
                desactivarControles();
                limpiarControles();
                btnEditar.setText("Editar");
                btnEliminar.setText("Eliminar");
                btnReportar.setDisable(false);
                btnNuevo.setDisable(false);
                tipoOperacion = Operacion.NINGUNO;
                    break;
                default:
                    if(tblEmpresa.getSelectionModel().getSelectedItem() !=null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar?","Eliminar  Empresa",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                        if(respuesta == JOptionPane.YES_OPTION){
                            try{
                                
                                PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarEmpresas(?)}");
                                sp.setInt(1, ((Empresa)tblEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
                                sp.execute();
                                listaEmpresa.remove(tblEmpresa.getSelectionModel().getSelectedIndex());
                                limpiarControles();
                                ImageIcon icon = new ImageIcon(EmpresasController.class.getResource("/org/andreortega/img/like.png"));
                                JOptionPane.showMessageDialog(null, "eliminado","",0,icon);
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    }else{
                    ImageIcon icon = new ImageIcon(EmpresasController.class.getResource("/org/andreortega/img/alto.png"));
                    JOptionPane.showMessageDialog(null, "debe seleccionar un registro","Aviso",0,icon);
                }
               
            }
        }

        
        public void guardar(){  
   
        if(nombre.getText().length()==0){
            ImageIcon icon = new ImageIcon(EmpresasController.class.getResource("/org/andreortega/img/alto.png"));
        JOptionPane.showMessageDialog(null, "Esta vacio, tienes que colocar datos","Aviso",0,icon);  
        }else{
        Empresa empresaNueva = new Empresa();
        empresaNueva.setNombreEmpresa(nombre.getText());
        empresaNueva.setDireccion(direccion.getText());
        empresaNueva.setTelefono(telefono.getText());
         try{
            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarEmpresas(?,?,?)}");
            sp.setString(1,empresaNueva.getNombreEmpresa());
            sp.setString(2,empresaNueva.getDireccion());
            sp.setString(3,empresaNueva.getTelefono());
            sp.execute();
            listaEmpresa.add(empresaNueva);       
        }catch(Exception e){
            e.printStackTrace();
            }
        }
    } 
        
    public void cargarDatos(){
         tblEmpresa.setItems(getEmpresa());
         colcodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, Integer>("codigoEmpresa"));
         colnombreEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, String>("nombreEmpresa"));
         coldireccionEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, String>("direccion"));
         coltelefonoEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, String>("telefono"));
         desactivarControles();
        }
    
    @FXML
     public void seleccionarElementos(){
         
         codigo.setText(String.valueOf(((Empresa)tblEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
         nombre.setText(((Empresa)tblEmpresa.getSelectionModel().getSelectedItem()).getNombreEmpresa());
         direccion.setText(((Empresa)tblEmpresa.getSelectionModel().getSelectedItem()).getDireccion());
         telefono.setText(((Empresa)tblEmpresa.getSelectionModel().getSelectedItem()).getTelefono());
     }  
     
     public ObservableList<Empresa> getEmpresa(){
         ArrayList<Empresa> lista = new ArrayList<Empresa>();
        try{
             PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_listarEmpresas()}");
             ResultSet resultado = procedimiento.executeQuery();
             while(resultado.next()){
             lista.add(new Empresa(resultado.getInt("codigoEmpresa"), 
                     resultado.getString("nombreEmpresa"), 
                     resultado.getString("direccion"),
                     resultado.getString("telefono")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaEmpresa = FXCollections.observableArrayList(lista);    
}

     
    public MainApp getSceneMain() {
        return sceneMain;
    }

    public void setSceneMain(MainApp sceneMain) {
        this.sceneMain = sceneMain;
    }
    
    @FXML
     public void mainMenu(){  
     sceneMain.mainMenu();
    }   
     
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
    }    
}
