/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.andreortega.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import org.andreortega.bd.Conexion;
import org.andreortega.bean.Empleado;
import org.andreortega.bean.Servicio;
import org.andreortega.bean.Servicios_has_Empleados;
import org.andreortega.system.MainApp;

public class ServicioshasEmpleadosController implements Initializable {
    
    private enum  Operacion{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, NINGUNO};
    private Operacion tipoOperacion = Operacion.NINGUNO;
    
    private MainApp sceneMain;
    
    @FXML private JFXButton btnNuevo;

    @FXML private JFXButton btnEditar;

    @FXML private JFXButton btnEliminar;

    @FXML private GridPane grpFechaS;

    @FXML private DatePicker fechaS;

    @FXML private JFXTextField hora;

    @FXML private JFXTextField codigo;

    @FXML private JFXTextField lugar;

    @FXML private ComboBox cbServicio;

    @FXML private ComboBox cbEmpleado;

    @FXML private TableView tblShE;

    @FXML private TableColumn colCodigo;

    @FXML private TableColumn colFecha;

    @FXML private TableColumn colHora;

    @FXML private TableColumn colLugar;

    @FXML private TableColumn colServicio;

    @FXML private TableColumn colEmpleado;
    
    private ObservableList<Servicio> listaServicio;

    private ObservableList<Empleado> listaEmpleado;
    
    private ObservableList<Servicios_has_Empleados> listaServicios_has_Empleados;
    
    
        public void desactivarControles(){
        codigo.setEditable(false);
        lugar.setEditable(false);
        hora.setEditable(false);
        cbServicio.setDisable(true);
        cbEmpleado.setDisable(true);
        grpFechaS.setDisable(true);
    }
    
    public void activarControles(){
        //codigo.setEditable(false);
        lugar.setEditable(true);
        hora.setEditable(true);
        lugar.setEditable(true);
        cbServicio.setDisable(false);
        cbEmpleado.setDisable(false);
        grpFechaS.setDisable(false);
    }
    
    private void limpiarControles(){
        codigo.setText("");
        lugar.setText("");
        hora.setText(""); 
        cbServicio.getSelectionModel().clearSelection();
        cbEmpleado.getSelectionModel().clearSelection();
    }
    
    
    public void nuevo(){
        switch(tipoOperacion){
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
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
                tipoOperacion= Operacion.NINGUNO;
                    break;         
    }
}
    
    public void editar(){
        switch(tipoOperacion){
            case NINGUNO:
                if(tblShE.getSelectionModel().getSelectedItem() !=null){
                btnEditar.setText("Actualizar");
                btnEliminar.setText("Cancelar");
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
                btnNuevo.setDisable(false);
                tipoOperacion = Operacion.NINGUNO;
                cargarDatos();
                    break;
        }
    } 
    
    
    
    public void guardar(){  
   
     if(lugar.getText().length()==0){
            ImageIcon icon = new ImageIcon(EmpresasController.class.getResource("/org/andreortega/img/alto.png"));
        JOptionPane.showMessageDialog(null, "Esta vacio, tienes que colocar datos","Aviso",0,icon);  
        }else{
        Servicios_has_Empleados ShEN = new Servicios_has_Empleados();
        ShEN.setFechaEvento(fechaS.getSelectedDate());
        ShEN.setLugarevento(lugar.getText());
        ShEN.setHoraEvento(hora.getText());
        ShEN.setServicios_codigoServicio(((Servicio)cbServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
        ShEN.setEmpleados_codigoEmpleado(((Empleado)cbEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
         try{
            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_Agregarservicio_has_empleado(?,?,?,?,?)}");
            sp.setInt(1, ShEN.getServicios_codigoServicio());
            sp.setInt(2, ShEN.getServicios_codigoServicio());
            sp.setDate(3, new java.sql.Date(ShEN.getFechaEvento().getTime()));
            sp.setString(4,ShEN.getHoraEvento());
            sp.setString(5,ShEN.getLugarevento());
   
            
            sp.execute();
            listaServicios_has_Empleados.add(ShEN); 
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
            tipoOperacion= Operacion.NINGUNO;
                    break;
                default:
                    if(tblShE.getSelectionModel().getSelectedItem() !=null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar?","Eliminar  Empresa",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                        if(respuesta == JOptionPane.YES_OPTION){
                            try{
                                
                                PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_Eliminarservicio_has_empleado(?)}");
                                sp.setInt(1, ((Servicios_has_Empleados)tblShE.getSelectionModel().getSelectedItem()).getCodigoServicio_has_Empleado());
                                sp.execute();
                                listaServicios_has_Empleados.remove(tblShE.getSelectionModel().getSelectedIndex());
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
        PreparedStatement pd = Conexion.getInstance().getConexion().prepareCall("{call sp_Actualizarservicio_has_empleado(?,?,?,?,?,?)}");
        Servicios_has_Empleados ShEAct = ((Servicios_has_Empleados)tblShE.getSelectionModel().getSelectedItem());

        ShEAct.setCodigoServicio_has_Empleado(Integer.parseInt(codigo.getText()));
        ShEAct.setFechaEvento(fechaS.getSelectedDate());
        ShEAct.setHoraEvento(hora.getText());
        ShEAct.setLugarevento(lugar.getText());
        ShEAct.setServicios_codigoServicio(((Servicio)cbServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
        ShEAct.setEmpleados_codigoEmpleado(((Empleado)cbEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado());

        pd.setInt(1, ShEAct.getCodigoServicio_has_Empleado());
        pd.setDate(4, new java.sql.Date(ShEAct.getFechaEvento().getTime()));
        pd.setString(5, ShEAct.getHoraEvento());
        pd.setString(6, ShEAct.getLugarevento());
        pd.setInt(2, ShEAct.getServicios_codigoServicio());
        pd.setInt(3, ShEAct.getEmpleados_codigoEmpleado());

        pd.execute();
        JOptionPane.showMessageDialog(null, "actualizado");
        }catch(Exception e){
            e.printStackTrace();
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
            
    public Empleado buscarEmpleado(int codigoEmpleado){
        Empleado resultado = null;
        try{
            PreparedStatement pd= Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarEmpleados(?)}");
            pd.setInt(1, codigoEmpleado);
            ResultSet reg= pd.executeQuery();
            while(reg.next()){
                resultado = new Empleado (reg.getInt("codigoEmpleado"),
                reg.getInt("numeroEmpleado"),
                reg.getString("apellidosEmpleado"),
                reg.getString("nombresEmpleado"),
                reg.getString("direccionEmpleado"),
                reg.getString("telefonoContacto"),
                reg.getString("gradoCocinero"),
                reg.getInt("tipoEmpleado_codigoTipoEmpleado"));                         
            }
        }catch(Exception e){
            e.printStackTrace();
        }
            return resultado;
    }
    
    public void seleccionarElementos(){
        codigo.setText(String.valueOf(((Servicios_has_Empleados)tblShE.getSelectionModel().getSelectedItem()).getCodigoServicio_has_Empleado()));
        fechaS.selectedDateProperty().set(((Servicios_has_Empleados)tblShE.getSelectionModel().getSelectedItem()).getFechaEvento());
        hora.setText(String.valueOf(((Servicios_has_Empleados)tblShE.getSelectionModel().getSelectedItem()).getHoraEvento()));
        lugar.setText(String.valueOf(((Servicios_has_Empleados)tblShE.getSelectionModel().getSelectedItem()).getLugarevento()));
        cbServicio.getSelectionModel().select(buscarServicio(((Servicios_has_Empleados)tblShE.getSelectionModel().getSelectedItem()).getServicios_codigoServicio()));
        cbEmpleado.getSelectionModel().select(buscarEmpleado(((Servicios_has_Empleados)tblShE.getSelectionModel().getSelectedItem()).getEmpleados_codigoEmpleado()));
    }
        
        public void cargarDatos(){
        tblShE.setItems(getShE());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Servicios_has_Empleados, Integer>("codigoServicio_has_Empleado"));
        colServicio.setCellValueFactory(new PropertyValueFactory<Servicios_has_Empleados, Integer>("Servicios_codigoServicio"));
        colEmpleado.setCellValueFactory(new PropertyValueFactory<Servicios_has_Empleados, Integer>("Empleados_codigoEmpleado"));
        colFecha.setCellValueFactory(new PropertyValueFactory<Servicios_has_Empleados, Date>("fechaEvento"));
        colHora.setCellValueFactory(new PropertyValueFactory<Servicios_has_Empleados, String>("horaEvento"));
        colLugar.setCellValueFactory(new PropertyValueFactory<Servicios_has_Empleados, String>("lugarevento"));
        cbServicio.setItems(getServicio());
        cbEmpleado.setItems(getEmpleado());
        desactivarControles();
    }
    
        public ObservableList<Servicios_has_Empleados> getShE(){
        ArrayList<Servicios_has_Empleados> lista = new ArrayList<Servicios_has_Empleados>();
        try{
            PreparedStatement ps= Conexion.getInstance().getConexion().prepareCall("{call sp_listarservicio_has_empleado()}");
            ResultSet resultado = ps.executeQuery();
            while(resultado.next()){
            lista.add(new Servicios_has_Empleados(resultado.getInt("codigoServicio_has_Empleado"),
                resultado.getInt("Servicios_codigoServicio"),
                resultado.getInt("Empleados_codigoEmpleado"),
                resultado.getDate("fechaEvento"),
                resultado.getString("horaEvento"),
                resultado.getString("lugarevento"))); 
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    return listaServicios_has_Empleados = FXCollections.observableArrayList(lista);
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
        
        fechaS = new DatePicker(Locale.ENGLISH);
        fechaS.setDateFormat(new SimpleDateFormat("yyy-MM-dd"));
        fechaS.getCalendarView().todayButtonTextProperty().set("Today");
        fechaS.getCalendarView().setShowWeeks(false);
        grpFechaS.add(fechaS, 0, 0);
        fechaS.getStylesheets().add("org/andreortega/css/DatePicker.css");
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
