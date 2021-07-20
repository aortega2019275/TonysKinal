
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
import org.andreortega.bean.Servicio;
import org.andreortega.bean.Servicios_has_Platos;
import org.andreortega.system.MainApp;



public class ServicioshasPlatosController implements Initializable {
    
    private MainApp sceneMain;

   

    @FXML private JFXButton btnNuevo;

    @FXML private JFXButton btnEliminar;

    @FXML private ComboBox cbServicio;

    @FXML private ComboBox cbPlato;

    @FXML private TableView tblShP;

    @FXML private TableColumn colServicios;

    @FXML private TableColumn colPlatos;
    
    private ObservableList<Servicio> listaServicio;

    private ObservableList<Platos> listaPlatos;
    
    private ObservableList<Servicios_has_Platos> listaServicios_has_Platos;
            
    private enum  Operacion{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, NINGUNO};
    private Operacion tipoOperacion = Operacion.NINGUNO;
        
    public void desactivarControles(){
        cbServicio.setDisable(true);
        cbPlato.setDisable(true);
    }
    
    public void activarControles(){
        cbServicio.setDisable(false);
        cbPlato.setDisable(false);
    }
    
    private void limpiarControles(){
        cbServicio.getSelectionModel().clearSelection();
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
   
     if(cbServicio.getSelectionModel().getSelectedItem() !=null  && cbPlato.getSelectionModel().getSelectedItem() !=null){
        Servicios_has_Platos ShPN = new Servicios_has_Platos();
        ShPN.setServicios_codigoServicio(((Servicio)cbServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
        ShPN.setPlatos_codigoPlato(((Platos)cbPlato.getSelectionModel().getSelectedItem()).getCodigoPlato());
         try{
            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_Agregarservicio_has_platos(?,?)}");
            sp.setInt(1, ShPN.getServicios_codigoServicio());
            sp.setInt(2, ShPN.getPlatos_codigoPlato());
            sp.execute();
            listaServicios_has_Platos.add(ShPN); 
                }catch(Exception e){
                 e.printStackTrace();
            }
        }else{
        ImageIcon icon = new ImageIcon(EmpresasController.class.getResource("/org/andreortega/img/alto.png"));
        JOptionPane.showMessageDialog(null, "Esta vacio, Selecciona los 2 datos","Aviso",0,icon); 
        }
    }
    

    
    
    public Servicio buscarServicio(int codigoServicio){
        Servicio resultado = null;
        try{
            PreparedStatement pd= Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarServicios(?)}");
            pd.setInt(1, codigoServicio);
            ResultSet reg= pd.executeQuery();
            while(reg.next()){
                resultado = new Servicio (reg.getInt("codigoServicio"),
                reg.getDate("fechaServicio"),
                reg.getString("tipoServicio"),
                reg.getString("horaServicio"),
                reg.getString("lugarServicio"),
                reg.getString("telefonoContacto"),
                reg.getInt("empresas_codigoEmpresa"));                              
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
        cbServicio.getSelectionModel().select(buscarServicio(((Servicios_has_Platos)tblShP.getSelectionModel().getSelectedItem()).getServicios_codigoServicio()));
        cbPlato.getSelectionModel().select(buscarPlato(((Servicios_has_Platos)tblShP.getSelectionModel().getSelectedItem()).getPlatos_codigoPlato()));
    }
        
        public void cargarDatos(){
        tblShP.setItems(getShP());
        colServicios.setCellValueFactory(new PropertyValueFactory<Servicios_has_Platos, Integer>("Servicios_codigoServicio"));
        colPlatos.setCellValueFactory(new PropertyValueFactory<Servicios_has_Platos, Integer>("Platos_codigoPlato"));
        cbServicio.setItems(getServicio());
        cbPlato.setItems(getPlatos());
        desactivarControles();
    }
    
        public ObservableList<Servicios_has_Platos> getShP(){
        ArrayList<Servicios_has_Platos> lista = new ArrayList<Servicios_has_Platos>();
        try{
            PreparedStatement ps= Conexion.getInstance().getConexion().prepareCall("{call sp_listarServicios_has_Platos()}");
            ResultSet resultado = ps.executeQuery();
            while(resultado.next()){
            lista.add(new Servicios_has_Platos(resultado.getInt("Servicios_codigoServicio"),
                resultado.getInt("Platos_codigoPlato")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    return listaServicios_has_Platos = FXCollections.observableArrayList(lista);
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
    
    public ObservableList<Servicio> getServicio(){
        ArrayList<Servicio> lista = new ArrayList<Servicio>();
        try{
            PreparedStatement ps= Conexion.getInstance().getConexion().prepareCall("{call sp_listarServicios()}");
            ResultSet resultado = ps.executeQuery();
            while(resultado.next()){
            lista.add(new Servicio(resultado.getInt("codigoServicio"),
                resultado.getDate("fechaServicio"),
                resultado.getString("tipoServicio"),
                resultado.getString("horaServicio"),
                resultado.getString("lugarServicio"),
                resultado.getString("telefonoContacto"),
                resultado.getInt("empresas_codigoEmpresa")));  
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    return listaServicio = FXCollections.observableArrayList(lista);
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
