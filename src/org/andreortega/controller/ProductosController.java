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
import org.andreortega.bd.Conexion;
import org.andreortega.bean.Producto;
import org.andreortega.system.MainApp;


public class ProductosController implements Initializable {
    
 private MainApp sceneMain;
 
private enum  Operacion{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, NINGUNO};
private Operacion tipoOperacion = Operacion.NINGUNO;
    
    @FXML private Button btnTipoEmp;  

    @FXML private Button rett;

    @FXML private JFXTextField codigo;

    @FXML private JFXTextField nombre;

    @FXML private JFXTextField cantidad;

    @FXML private TableView tblProductos;

    @FXML private TableColumn colCodigo;

    @FXML private TableColumn colNombre;

    @FXML private TableColumn  colCantidad;

    @FXML private JFXButton btnNuevo;

    @FXML private JFXButton btnEditar;

    @FXML private JFXButton btnEliminar;

    @FXML private JFXButton btnReportar;

    private ObservableList<Producto> listaProducto;
    
    
    public void desactivarControles(){
        codigo.setEditable(false);
        nombre.setEditable(false);
        cantidad.setEditable(false);
    }
    
    public void activarControles(){
        //codigo.setEditable(false);
        nombre.setEditable(true);
        cantidad.setEditable(true);
    }
    
    private void limpiarControles(){
        codigo.setText("");
        nombre.setText("");
        cantidad.setText(""); 
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
    
    public void editar(){
        switch(tipoOperacion){
            case NINGUNO:
                if(tblProductos.getSelectionModel().getSelectedItem() !=null){
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
    
    public void guardar(){  
   
         if(nombre.getText().length()==0){
                ImageIcon icon = new ImageIcon(EmpresasController.class.getResource("/org/andreortega/img/alto.png"));
                JOptionPane.showMessageDialog(null, "Esta vacio, tienes que colocar datos","Aviso",0,icon);  
            }else{
                Producto ProductoN = new Producto();
                ProductoN.setNombreProducto(nombre.getText());
                ProductoN.setCantidad(Integer.parseInt(cantidad.getText()));
                try{
                    PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarProducto(?,?)}");
                    sp.setString(1,ProductoN.getNombreProducto());
                    sp.setInt(2,ProductoN.getCantidad());
                    sp.execute();
                    listaProducto.add(ProductoN); 
                }catch(Exception e){
                 e.printStackTrace();
                    }
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
                    if(tblProductos.getSelectionModel().getSelectedItem() !=null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar?","Eliminar  Empresa",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                        if(respuesta == JOptionPane.YES_OPTION){
                            try{
                                
                                PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarProducto(?)}");
                                sp.setInt(1, ((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto());
                                sp.execute();
                                listaProducto.remove(tblProductos.getSelectionModel().getSelectedIndex());
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
        public void actualizar(){
        
        try{
        PreparedStatement pd = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarProducto(?,?,?)}");
        Producto ProdAct = ((Producto)tblProductos.getSelectionModel().getSelectedItem());

        ProdAct.setCodigoProducto(Integer.parseInt(codigo.getText()));
        ProdAct.setNombreProducto(nombre.getText());
        ProdAct.setCantidad(Integer.parseInt(cantidad.getText()));


        pd.setInt(1, ProdAct.getCodigoProducto());
        pd.setString(2, ProdAct.getNombreProducto());
        pd.setInt(3, ProdAct.getCantidad());

        pd.execute();
        JOptionPane.showMessageDialog(null, "actualizado");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void seleccionarElementos(){
        codigo.setText(String.valueOf(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto()));
        nombre.setText(String.valueOf(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getNombreProducto()));
        cantidad.setText(String.valueOf(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCantidad()));
    }  
    
    public void cargarDatos(){
        tblProductos.setItems(getProducto());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("codigoProducto"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Producto, String>("nombreProducto"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("cantidad"));
        desactivarControles();
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
        return listaProducto = FXCollections.observableArrayList(lista);    
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
}
