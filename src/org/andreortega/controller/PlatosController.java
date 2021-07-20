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
import org.andreortega.system.MainApp;
import org.andreortega.bd.Conexion;
import org.andreortega.bean.Platos;
import org.andreortega.bean.TipoPlato;

/**
 * FXML Controller class
 *
 * @author javir
 */
public class PlatosController implements Initializable {
    
    private MainApp sceneMain;
    
    private enum  Operacion{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, NINGUNO};
    
    private Operacion tipoOperacion = Operacion.NINGUNO;
    
    private ObservableList<Platos> listaPlatos;
    
    private ObservableList<TipoPlato> listaTipoPlato;
    
    @FXML private JFXTextField codigo;

    @FXML private JFXTextField nombre;

    @FXML private JFXTextField descripcion;

    @FXML private JFXTextField cantidad;
    
    @FXML private JFXTextField precio;

    @FXML private JFXButton btnNuevo;

    @FXML private JFXButton btnEditar;

    @FXML private JFXButton btnEliminar;

    @FXML private JFXButton btnReportar;

    @FXML private Button rett;

    @FXML private TableView tblPlatos;

    @FXML private TableColumn colcodigoPlato;

    @FXML private TableColumn colnombrePlato;

    @FXML private TableColumn coldescipcionPlato;

    @FXML private TableColumn colcantidad;

    @FXML private TableColumn colprecio;

    @FXML private TableColumn coltipoPlato;



    @FXML private ComboBox cbtipoplato;

    
      //personalizacion
    public void desactivarControles(){
        codigo.setEditable(false);
        nombre.setEditable(false);
        descripcion.setEditable(false);
        cantidad.setEditable(false);
        precio.setEditable(false);
        cbtipoplato.setDisable(true);
    }
    
    public void activarControles(){
        nombre.setEditable(true);
        descripcion.setEditable(true);
        cantidad.setEditable(true);
        precio.setEditable(true);
        cbtipoplato.setDisable(false); 
    }
    
    private void limpiarControles(){
        codigo.setText("");
        nombre.setText("");
        descripcion.setText("");
        cantidad.setText("");
        precio.setText("");
        cbtipoplato.getSelectionModel().clearSelection();
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
    public void guardar(){
        if(nombre.getText().length()==0){
            ImageIcon icon = new ImageIcon(EmpresasController.class.getResource("/org/andreortega/img/alto.png"));
        JOptionPane.showMessageDialog(null, "Esta vacio, tienes que colocar datos","Aviso",0,icon);  
        }else{
            Platos PlatoN = new Platos();
            PlatoN.setCantidad(Integer.parseInt(cantidad.getText()));
            PlatoN.setNombrePlato(nombre.getText());
            PlatoN.setDescripcionPlato(descripcion.getText());
            PlatoN.setPrecioPlato(Double.parseDouble(precio.getText()));
            PlatoN.setTipoplato_codigoTipoPlato(((TipoPlato)cbtipoplato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
        
            try{
                PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarPlatos(?,?,?,?,?)}");
                    sp.setInt(1, PlatoN.getCantidad());
                    sp.setString(2, PlatoN.getNombrePlato());
                    sp.setString(3, PlatoN.getDescripcionPlato());
                    sp.setDouble(4, PlatoN.getPrecioPlato());
                    sp.setInt(5, PlatoN.getTipoplato_codigoTipoPlato());
                    sp.execute();
                    listaPlatos.add(PlatoN);
                    
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
    }
        public void editar(){
        switch(tipoOperacion){
            case NINGUNO:
                if(tblPlatos.getSelectionModel().getSelectedItem() !=null){
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
        PreparedStatement pd = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarPlatos(?,?,?,?,?)}");
        Platos platAct = ((Platos)tblPlatos.getSelectionModel().getSelectedItem());
        
        platAct.setCodigoPlato(Integer.parseInt(codigo.getText()));
        platAct.setCantidad(Integer.parseInt(cantidad.getText()));
        platAct.setNombrePlato(nombre.getText());
        platAct.setDescripcionPlato(descripcion.getText());
        platAct.setPrecioPlato(Double.parseDouble(precio.getText()));

        pd.setInt(1, platAct.getCodigoPlato());
        pd.setInt(2, platAct.getCantidad());
        pd.setString(3, platAct.getNombrePlato());
        pd.setString(4, platAct.getDescripcionPlato());
        pd.setDouble(5, platAct.getPrecioPlato());
        pd.execute();
        JOptionPane.showMessageDialog(null, "actualizado");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
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
                default:
                    if(tblPlatos.getSelectionModel().getSelectedItem() !=null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar?","Eliminar  Empresa",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                        if(respuesta == JOptionPane.YES_OPTION){
                            try{
                                
                                PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarPlatos(?)}");
                                sp.setInt(1, ((Platos)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato());
                                sp.execute();
                                listaPlatos.remove(tblPlatos.getSelectionModel().getSelectedIndex());
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
    public void cargarDatos(){
        tblPlatos.setItems(getPlatos());
        colcodigoPlato.setCellValueFactory(new PropertyValueFactory<Platos, Integer>("codigoPlato"));
        colnombrePlato.setCellValueFactory(new PropertyValueFactory<Platos, String>("nombrePlato"));
        coldescipcionPlato.setCellValueFactory(new PropertyValueFactory<Platos, String>("descripcionPlato"));
        colcantidad.setCellValueFactory(new PropertyValueFactory<Platos, Integer>("cantidad"));
        colprecio.setCellValueFactory(new PropertyValueFactory<Platos, Double>("precioPlato"));
        coltipoPlato.setCellValueFactory(new PropertyValueFactory<Platos, Integer>("tipoplato_codigoTipoPlato"));
        cbtipoplato.setItems(getTipoPlato());
        desactivarControles();
    }
    public void seleccionarElementos(){
        codigo.setText(String.valueOf(((Platos)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
        cantidad.setText(String.valueOf(((Platos)tblPlatos.getSelectionModel().getSelectedItem()).getCantidad()));
        nombre.setText(String.valueOf(((Platos)tblPlatos.getSelectionModel().getSelectedItem()).getNombrePlato()));
        descripcion.setText(String.valueOf(((Platos)tblPlatos.getSelectionModel().getSelectedItem()).getDescripcionPlato()));
        precio.setText(String.valueOf(((Platos)tblPlatos.getSelectionModel().getSelectedItem()).getPrecioPlato()));
        cbtipoplato.getSelectionModel().select(buscarTipoPlato(((Platos)tblPlatos.getSelectionModel().getSelectedItem()).getTipoplato_codigoTipoPlato()));
    }  
     
    
        public TipoPlato buscarTipoPlato(int codigoTipoPlato){
        TipoPlato resultado = null;
        try{
            PreparedStatement pd= Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarTipoPlato(?)}");
            pd.setInt(1, codigoTipoPlato);
            ResultSet reg= pd.executeQuery();
            while(reg.next()){
                resultado = new TipoPlato (reg.getInt("codigoTipoPlato"),
                                        reg.getString("descripcionTipo"));
            }
        }catch(Exception e){
            e.printStackTrace();
            }
            return resultado;
    }
       public ObservableList<Platos> getPlatos(){
        ArrayList<Platos> lista = new ArrayList<Platos>();
        try{
            PreparedStatement ps= Conexion.getInstance().getConexion().prepareCall("{call sp_listarPlatos()}");
            ResultSet resultado = ps.executeQuery();
            while(resultado.next()){
            lista.add(new Platos(resultado.getInt("codigoPlato"),
                resultado.getString("nombrePlato"),
                resultado.getString("descripcionPlato"),
                resultado.getInt("cantidad"),
                resultado.getDouble("precioPlato"),
                resultado.getInt("tipoplato_codigoTipoPlato")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    return listaPlatos = FXCollections.observableArrayList(lista);
    }
    
       
    public ObservableList<TipoPlato> getTipoPlato(){
        ArrayList<TipoPlato> lista = new ArrayList<TipoPlato>();
        try{
             PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_listarTipoPlato()}");
             ResultSet resultado = procedimiento.executeQuery();
             while(resultado.next()){
             lista.add(new TipoPlato(resultado.getInt("codigoTipoPlato"), 
                     resultado.getString("descripcionTipo")));     
                }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaTipoPlato = FXCollections.observableArrayList(lista);  
          }
          
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cargarDatos();
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/andreortega/view/TipoPlato.fxml"));
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
