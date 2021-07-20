/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.andreortega.controller;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import org.andreortega.bd.Conexion;
import org.andreortega.bean.Platos;
import org.andreortega.bean.Producto;
import org.andreortega.bean.Productos_has_Platos;
import org.andreortega.bean.Servicios_has_Platos;
import org.andreortega.system.MainApp;

/**
 * FXML Controller class
 *
 * @author javir
 */
public class ProductoshasPlatosController implements Initializable {
    
    private MainApp sceneMain;
    
    @FXML private Button closeButton;

    @FXML private JFXButton btnNuevo;
    
    @FXML private JFXButton btnEliminar;

    @FXML private ComboBox cbProducto;

    @FXML private ComboBox cbPlato;

    @FXML private TableView tblPhP;

    @FXML private TableColumn colProducto;

    @FXML private TableColumn colPlatos;

    private ObservableList<Producto> listaProductos;

    private ObservableList<Platos> listaPlatos;
    
    private ObservableList<Productos_has_Platos> listaProductos_has_Platos;

    private enum  Operacion{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, NINGUNO};
    private Operacion tipoOperacion = Operacion.NINGUNO;
        
    public void desactivarControles(){
        cbProducto.setDisable(true);
        cbPlato.setDisable(true);
    }
    
    public void activarControles(){
        cbProducto.setDisable(false);
        cbPlato.setDisable(false);
    }
    
    private void limpiarControles(){
        cbProducto.getSelectionModel().clearSelection();
        cbPlato.getSelectionModel().clearSelection();
    }
   
        public void nuevo(){
        switch(tipoOperacion){
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                tipoOperacion= Operacion.GUARDAR;
                    break;
            case GUARDAR:
                guardar();
                desactivarControles();
                cargarDatos();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Cancelar");
                tipoOperacion= Operacion.NINGUNO;
                    break;         
    }
}            
        public void eliminar(){
        switch(tipoOperacion){
            case GUARDAR:
            desactivarControles();
            limpiarControles();
            btnNuevo.setText("Nuevo");
            btnEliminar.setText("Cancelar");
            tipoOperacion= Operacion.NINGUNO;
                    break;
        }
        }
 
    public void guardar(){  
   
     if(cbProducto.getSelectionModel().getSelectedItem() !=null  && cbPlato.getSelectionModel().getSelectedItem() !=null){
        Productos_has_Platos PhPN = new Productos_has_Platos();
        PhPN.setProductos_codigoProducto(((Producto)cbProducto.getSelectionModel().getSelectedItem()).getCodigoProducto());
        PhPN.setPlatos_codigoPlato(((Platos)cbPlato.getSelectionModel().getSelectedItem()).getCodigoPlato());
         try{
            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarProductos_has_platos(?,?)}");
            sp.setInt(1, PhPN.getProductos_codigoProducto());
            sp.setInt(2, PhPN.getPlatos_codigoPlato());
            sp.execute();
            listaProductos_has_Platos.add(PhPN); 
                }catch(Exception e){
                 e.printStackTrace();
            }
        }else{
        ImageIcon icon = new ImageIcon(EmpresasController.class.getResource("/org/andreortega/img/alto.png"));
        JOptionPane.showMessageDialog(null, "Esta vacio, Selecciona los 2 datos","Aviso",0,icon); 
        }
    }
    
    
    public Producto buscarProducto(int codigoProducto){
        Producto resultado = null;
        try{
            PreparedStatement pd= Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarProducto(?)}");
            pd.setInt(1, codigoProducto);
            ResultSet reg= pd.executeQuery();
            while(reg.next()){
                resultado = new Producto (reg.getInt("codigoProducto"), 
                     reg.getString("nombreProducto"), 
                     reg.getInt("cantidad"));                            
            }
        }catch(Exception e){
            e.printStackTrace();    
        }
            return resultado;
    }
            
    public Platos buscarPlato(int codigoPlato){
        Platos resultado = null;
        try{
            PreparedStatement pd= Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarPlatos(?)}");
            pd.setInt(1, codigoPlato);
            ResultSet reg= pd.executeQuery();
            while(reg.next()){
                resultado = new Platos (reg.getInt("codigoPlato"),
                reg.getString("nombrePlato"),
                reg.getString("descripcionPlato"),
                reg.getInt("cantidad"),
                reg.getDouble("precioPlato"),
                reg.getInt("tipoplato_codigoTipoPlato"));
                                        
            }
        }catch(Exception e){
            e.printStackTrace();
        }
            return resultado;
    }
    
    public void seleccionarElementos(){
        cbProducto.getSelectionModel().select(buscarProducto(((Productos_has_Platos)tblPhP.getSelectionModel().getSelectedItem()).getProductos_codigoProducto()));
        cbPlato.getSelectionModel().select(buscarPlato(((Productos_has_Platos)tblPhP.getSelectionModel().getSelectedItem()).getPlatos_codigoPlato()));
    }
        
        public void cargarDatos(){
        tblPhP.setItems(getPhP());
        colProducto.setCellValueFactory(new PropertyValueFactory<Productos_has_Platos, Integer>("Productos_codigoProducto"));
        colPlatos.setCellValueFactory(new PropertyValueFactory<Productos_has_Platos, Integer>("Platos_codigoPlato"));
        cbProducto.setItems(getProducto());
        cbPlato.setItems(getPlatos());
        desactivarControles();
    }
    
        public ObservableList<Productos_has_Platos> getPhP(){
        ArrayList<Productos_has_Platos> lista = new ArrayList<Productos_has_Platos>();
        try{
            PreparedStatement ps= Conexion.getInstance().getConexion().prepareCall("{call sp_listarProductos_has_Platos()}");
            ResultSet resultado = ps.executeQuery();
            while(resultado.next()){
            lista.add(new Productos_has_Platos(resultado.getInt("Productos_codigoProducto"),
                resultado.getInt("Platos_codigoPlato")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    return listaProductos_has_Platos = FXCollections.observableArrayList(lista);
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
    
    public ObservableList<Producto> getProducto(){
         ArrayList<Producto> lista = new ArrayList<Producto>();
        try{
             PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_listarProductos()}");
             ResultSet resultado = procedimiento.executeQuery();
             while(resultado.next()){
             lista.add(new Producto(resultado.getInt("codigoProducto"), 
                     resultado.getString("nombreProducto"), 
                     resultado.getInt("cantidad")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaProductos = FXCollections.observableArrayList(lista);    
}
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
}
