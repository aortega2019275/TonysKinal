/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.andreortega.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import org.andreortega.bd.Conexion;
import org.andreortega.system.MainApp;
import org.andreortega.bean.Empleado;
import org.andreortega.bean.TipoEmpleado;

public class EmpleadosController implements Initializable {
    
    private MainApp sceneMain;
    
    @FXML private Button btnTipoEmp;
    
    private enum Operacion{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, NINGUNO};
    
    private Operacion tipoOperacion = Operacion.NINGUNO;
    
    @FXML private Button rett;
    
    @FXML private JFXTextField codigo;

    @FXML private JFXTextField numeros;

    @FXML private JFXTextField apellidos;

    @FXML private JFXTextField nombres;

    @FXML private JFXTextField direccion;

    @FXML private JFXTextField telefono;

    @FXML private JFXTextField grado;

    @FXML private JFXTextField tipoempleado;

    @FXML private JFXButton btnNuevo;

    @FXML private JFXButton btnEditar;

    @FXML private JFXButton btnEliminar;

    @FXML private JFXButton btnReportar;

    @FXML private TableColumn colCodigoempleado;

    @FXML private TableColumn colNumeroempleado;

    @FXML private TableColumn colApellidosempleado;

    @FXML private TableColumn colNombreempleado;

    @FXML private TableColumn colDireccionempleado;

    @FXML private TableColumn coltelefonoempleado;

    @FXML private TableColumn colGradococinero;

    @FXML private TableColumn colTipoempleado;

    @FXML private TableView tblEmpleado;
    
    private ObservableList<Empleado> listaEmpleado;
    
    private ObservableList<TipoEmpleado> listaTipoEmpleado;

    @FXML private ComboBox<TipoEmpleado> comboBox;
   
    //personalizacion
    public void desactivarControles(){
        codigo.setEditable(false);
        numeros.setEditable(false);
        apellidos.setEditable(false);
        nombres.setEditable(false);
        direccion.setEditable(false);
        telefono.setEditable(false);
        grado.setEditable(false);     
        comboBox.setDisable(true);
    }
    
    public void activarControles(){
        numeros.setEditable(true);
        apellidos.setEditable(true);
        nombres.setEditable(true);
        direccion.setEditable(true);
        telefono.setEditable(true);
        grado.setEditable(true);
        comboBox.setDisable(false);
        
    }
    
    private void limpiarControles(){
        codigo.setText("");
        numeros.setText("");
        apellidos.setText("");
        nombres.setText("");
        direccion.setText("");
        telefono.setText("");
        grado.setText("");
        comboBox.getSelectionModel().clearSelection();
    }
    
    //CRUD : AGREGAR 
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
   
    public void guardar(){
        if(numeros.getText().length()==0){
            ImageIcon icon = new ImageIcon(EmpresasController.class.getResource("/org/andreortega/img/alto.png"));
        JOptionPane.showMessageDialog(null, "Esta vacio, tienes que colocar datos","Aviso",0,icon);  
        }else{
            Empleado empleadoNuevo = new Empleado();
            int topoemp= comboBox.getSelectionModel().getSelectedItem().getCodigoTipoEmpleado();
            empleadoNuevo.setNumeroEmpleado(Integer.parseInt(numeros.getText()));
            empleadoNuevo.setApellidosEmpleado(apellidos.getText());
            empleadoNuevo.setNombresEmpleado(nombres.getText());
            empleadoNuevo.setDireccionEmpleado(direccion.getText());
            empleadoNuevo.setTelefonoContacto(telefono.getText());
            empleadoNuevo.setGradoCocinero(grado.getText());
            empleadoNuevo.setTipoEmpleado_codigoTipoEmpleado(topoemp);
        
            try{
                PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarEmpleados(?,?,?,?,?,?,?)}");
                    sp.setInt(1, empleadoNuevo.getNumeroEmpleado());
                    sp.setString(2, empleadoNuevo.getApellidosEmpleado());
                    sp.setString(3, empleadoNuevo.getNombresEmpleado());
                    sp.setString(4, empleadoNuevo.getDireccionEmpleado());
                    sp.setString(5, empleadoNuevo.getTelefonoContacto());
                    sp.setString(6, empleadoNuevo.getGradoCocinero());
                    sp.setInt(7, empleadoNuevo.getTipoEmpleado_codigoTipoEmpleado());
                    sp.execute();
                    listaEmpleado.add(empleadoNuevo);
                    
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
    }
    
   // CRUD: ELIMINAR
    public void eliminar(){
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
                    if(tblEmpleado.getSelectionModel().getSelectedItem() !=null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar?","Eliminar  Empresa",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                        if(respuesta == JOptionPane.YES_OPTION){
                            try{
                                
                                PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarEmpleados(?)}");
                                sp.setInt(1, ((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
                                sp.execute();
                                listaEmpleado.remove(tblEmpleado.getSelectionModel().getSelectedIndex());
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
    
    //CRUD: EDITAR
    public void editar(){
        switch(tipoOperacion){
            case NINGUNO:
                if(tblEmpleado.getSelectionModel().getSelectedItem() !=null){
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
                btnEditar.setText("Editar");
                btnEliminar.setText("Eliminar");
                btnReportar.setDisable(false);
                btnNuevo.setDisable(false);
                tipoOperacion = Operacion.NINGUNO;
                cargarDatos();
                break;
        }
    } 
    
            
    public void actualizar(){
        
        try{
        PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarEmpleados(?,?,?,?,?,?,?,?)}");
        Empleado empleAct = ((Empleado)tblEmpleado.getSelectionModel().getSelectedItem());
        int topoemp= comboBox.getSelectionModel().getSelectedItem().getCodigoTipoEmpleado();
        //Obtener Datos para DB
        empleAct.setNumeroEmpleado(Integer.parseInt(numeros.getText()));
        empleAct.setApellidosEmpleado(apellidos.getText());
        empleAct.setNombresEmpleado(nombres.getText());
        empleAct.setDireccionEmpleado(direccion.getText());
        empleAct.setTelefonoContacto(telefono.getText());
        empleAct.setGradoCocinero(grado.getText());
        empleAct.setTipoEmpleado_codigoTipoEmpleado(topoemp);
        
        // Enviar Datos DB
        sp.setInt(1, empleAct.getCodigoEmpleado());
        sp.setInt(2, empleAct.getNumeroEmpleado());
        sp.setString(3, empleAct.getApellidosEmpleado());
        sp.setString(4, empleAct.getNombresEmpleado());
        sp.setString(5, empleAct.getDireccionEmpleado());
        sp.setString(6, empleAct.getTelefonoContacto());
        sp.setString(7, empleAct.getGradoCocinero());
        sp.setInt(8, empleAct.getTipoEmpleado_codigoTipoEmpleado());
        sp.execute();
        JOptionPane.showMessageDialog(null, "actualizado");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    
    public void cargarDatos(){
        tblEmpleado.setItems(getEmpleado());
        colCodigoempleado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("codigoEmpleado"));
        colNumeroempleado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("numeroEmpleado"));
        colApellidosempleado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("apellidosEmpleado"));
        colNombreempleado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("nombresEmpleado"));
        colDireccionempleado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("direccionEmpleado"));
        coltelefonoempleado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("telefonoContacto"));
        colGradococinero.setCellValueFactory(new PropertyValueFactory<Empleado, String>("gradoCocinero"));
        colTipoempleado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("tipoEmpleado_codigoTipoEmpleado"));
        desactivarControles();
    }
    
    public void seleccionarElementos(){
        codigo.setText(String.valueOf(((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
        numeros.setText(String.valueOf(((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getNumeroEmpleado()));
        apellidos.setText(((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getApellidosEmpleado());
        nombres.setText(((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getNombresEmpleado());
        direccion.setText(((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getDireccionEmpleado());
        telefono.setText(((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getTelefonoContacto());
        grado.setText(((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getGradoCocinero());
        comboBox.getSelectionModel().select(buscarTipoEmpleado(((Empleado)tblEmpleado.getSelectionModel().getSelectedItem()).getTipoEmpleado_codigoTipoEmpleado()));
    }
    
        public TipoEmpleado buscarTipoEmpleado(int codigoTipoEmpleado){
        TipoEmpleado resultado = null;
        try{
            PreparedStatement pd= Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarTipoEmpleado(?)}");
            pd.setInt(1, codigoTipoEmpleado);
            ResultSet reg= pd.executeQuery();
            while(reg.next()){
                resultado = new TipoEmpleado (reg.getInt("codigoTipoEmpleado"),
                                        reg.getString("descripcion"));
                                        
            }
        }catch(Exception e){
            e.printStackTrace();
        }
            return resultado;
    }
    
    public void initCombos(){
        TipoEmpleado te = new TipoEmpleado();
        
        ObservableList<TipoEmpleado> teTipoEmpleado = te.getTipoEmpleado();
        this.comboBox.setItems(teTipoEmpleado);
    }
    
    public ObservableList<Empleado> getEmpleado(){
        ArrayList<Empleado> lista = new ArrayList<Empleado>();
        try{
            PreparedStatement ps= Conexion.getInstance().getConexion().prepareCall("{call sp_listarEmpleados()}");
            ResultSet resultado = ps.executeQuery();
            while(resultado.next()){
            lista.add(new Empleado(resultado.getInt("codigoEmpleado"),
                resultado.getInt("numeroEmpleado"),
                resultado.getString("apellidosEmpleado"),
                resultado.getString("nombresEmpleado"),
                resultado.getString("direccionEmpleado"),
                resultado.getString("telefonoContacto"),
                resultado.getString("gradoCocinero"),
                resultado.getInt("tipoEmpleado_codigoTipoEmpleado")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    return listaEmpleado = FXCollections.observableArrayList(lista);
    }
         
    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
        initCombos();
    }
  
  
    public MainApp getSceneMain() {
        return sceneMain;
    }

    public void setSceneMain(MainApp sceneMain) {
        this.sceneMain = sceneMain;
    }
    
    public void mainMenu(){   
    sceneMain.mainMenu();
    }  
    

  
     
    public void btnTipoEmp(ActionEvent event){
         try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/andreortega/view/TipoEmpleado.fxml"));
            Parent root;
            root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.getIcons().add(new Image("/org/andreortega/img/tipoempleado.png")); 

            stage.setScene(scene); 
            stage.show();
            
            
        } catch (IOException ex) {
            Logger.getLogger(EmpleadosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
            
}
