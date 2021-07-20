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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
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
import javafx.scene.layout.GridPane;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import org.andreortega.bd.Conexion;
import org.andreortega.bean.Empresa;
import org.andreortega.bean.Servicio;
import org.andreortega.report.GenerarReporte;

import org.andreortega.system.MainApp;
public class ServiciosController implements Initializable {
    
    private final String log="/org/andreortega/img/IconB.png";
    private final String bot="/org/andreortega/img/fondo1.png";
    private final String top="/org/andreortega/img/fondo2.png";

    private enum  Operacion{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, NINGUNO};
    private Operacion tipoOperacion = Operacion.NINGUNO;
    
    @FXML private Button btnTipoEmp;   
    
    @FXML private Button rett;

    @FXML private JFXButton btnNuevo;

    @FXML private JFXButton btnEditar;

    @FXML private JFXButton btnEliminar;

    @FXML private JFXButton btnReportar;

    @FXML private DatePicker fechaS;
   
    @FXML private JFXTextField codigo;

    @FXML private JFXTextField lugar;
    
    @FXML private JFXTextField telefono;
    
    @FXML private JFXTextField hora;
    
    @FXML private JFXTextField tipoServicio;

    @FXML private ComboBox comboboxEmpresa;

    @FXML private TableView tblServicio;

    @FXML private TableColumn colCodigoServicios;
    
    @FXML private TableColumn colFechaServicios;

    @FXML private TableColumn colHoraServicios;

    @FXML private TableColumn colLugarServicios;

    @FXML private TableColumn colTelefonoContacto;

    @FXML private TableColumn colTipoServicios;
    
    @FXML private TableColumn colEmpresa;
    
    @FXML private GridPane grpFechaS;

    
    private MainApp sceneMain;
    
    private ObservableList<Servicio> listaServicio;
    
    private ObservableList<Empresa> listaEmpresa;

    
    public void desactivarControles(){
        codigo.setEditable(false);
        lugar.setEditable(false);
        telefono.setEditable(false);
        tipoServicio.setEditable(false);
        comboboxEmpresa.setDisable(true);
        grpFechaS.setDisable(true);
        hora.setDisable(true);
    }
    
    public void activarControles(){
        //codigo.setEditable(false);
        lugar.setEditable(true);
        telefono.setEditable(true);
        tipoServicio.setEditable(true);
        comboboxEmpresa.setDisable(false);
        grpFechaS.setDisable(false);
        hora.setDisable(false);
    }
    
    private void limpiarControles(){
        codigo.setText("");
        lugar.setText("");
        telefono.setText(""); 
        tipoServicio.setText("");
        comboboxEmpresa.getSelectionModel().clearSelection();
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
                if(tblServicio.getSelectionModel().getSelectedItem() !=null){
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
    
            public void generarReporte(){
            switch(tipoOperacion){
                case NINGUNO:
                    imprimirReporte();
                    break;
            }
        }
        
        public void imprimirReporte(){
            if(tblServicio.getSelectionModel().getSelectedItem() !=null){
            Map parametros = new HashMap();
            int codServicios = Integer.valueOf(codigo.getText());
            parametros.put("log", this.getClass().getResourceAsStream(log));
            parametros.put("bot", this.getClass().getResourceAsStream(bot));
            parametros.put("top", this.getClass().getResourceAsStream(top));
            parametros.put("codServicios", codServicios);
            GenerarReporte.mostrarReporte("ReporteServicio.jasper", "Reporte de Servicio", parametros);
            }else{
                ImageIcon icon = new ImageIcon(EmpresasController.class.getResource("/org/andreortega/img/alto.png"));
                JOptionPane.showMessageDialog(null, "debe seleccionar Un registro para generar un reporte","Aviso",0,icon);
            }
        }
    
public void guardar(){  
   
     if(lugar.getText().length()==0){
            ImageIcon icon = new ImageIcon(EmpresasController.class.getResource("/org/andreortega/img/alto.png"));
        JOptionPane.showMessageDialog(null, "Esta vacio, tienes que colocar datos","Aviso",0,icon);  
        }else{
        Servicio servicioNuevo = new Servicio();
        servicioNuevo.setFechaServicio(fechaS.getSelectedDate());
        servicioNuevo.setLugarServicio(lugar.getText());
        servicioNuevo.setHoraServicio(hora.getText());
        servicioNuevo.setTipoServicio(tipoServicio.getText());
        servicioNuevo.setTelefonoContacto(telefono.getText());
        servicioNuevo.setEmpresas_codigoEmpresa(((Empresa)comboboxEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
         try{
            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarServicios(?,?,?,?,?,?)}");
            sp.setDate(1, new java.sql.Date(servicioNuevo.getFechaServicio().getTime()));
            sp.setString(2,servicioNuevo.getTipoServicio());
            sp.setString(3,servicioNuevo.getHoraServicio());
            sp.setString(4, servicioNuevo.getLugarServicio());
            sp.setString(5, servicioNuevo.getTelefonoContacto());
            sp.setInt(6, servicioNuevo.getEmpresas_codigoEmpresa());
            sp.execute();
            listaServicio.add(servicioNuevo); 
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
                    if(tblServicio.getSelectionModel().getSelectedItem() !=null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar?","Eliminar  Empresa",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                        if(respuesta == JOptionPane.YES_OPTION){
                            try{
                                
                                PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarServicios(?)}");
                                sp.setInt(1, ((Servicio)tblServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
                                sp.execute();
                                listaServicio.remove(tblServicio.getSelectionModel().getSelectedIndex());
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
        PreparedStatement pd = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarServicios(?,?,?,?,?,?)}");
        Servicio servAct = ((Servicio)tblServicio.getSelectionModel().getSelectedItem());

        servAct.setCodigoServicio(Integer.parseInt(codigo.getText()));
        servAct.setFechaServicio(fechaS.getSelectedDate());
        servAct.setTipoServicio(tipoServicio.getText());
        servAct.setHoraServicio(hora.getText());
        servAct.setLugarServicio(lugar.getText());
        servAct.setTelefonoContacto(telefono.getText());

        pd.setInt(1, servAct.getCodigoServicio());
        pd.setDate(2, new java.sql.Date(servAct.getFechaServicio().getTime()));
        pd.setString(3, servAct.getTipoServicio());
        pd.setString(4, servAct.getHoraServicio());
        pd.setString(5, servAct.getLugarServicio());
        pd.setString(6, servAct.getTelefonoContacto());

        pd.execute();
        JOptionPane.showMessageDialog(null, "actualizado");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void seleccionarElementos(){
        codigo.setText(String.valueOf(((Servicio)tblServicio.getSelectionModel().getSelectedItem()).getCodigoServicio()));
        fechaS.selectedDateProperty().set(((Servicio)tblServicio.getSelectionModel().getSelectedItem()).getFechaServicio());
        tipoServicio.setText(String.valueOf(((Servicio)tblServicio.getSelectionModel().getSelectedItem()).getTipoServicio()));
        hora.setText(String.valueOf(((Servicio)tblServicio.getSelectionModel().getSelectedItem()).getHoraServicio()));
        lugar.setText(String.valueOf(((Servicio)tblServicio.getSelectionModel().getSelectedItem()).getLugarServicio()));
        telefono.setText(String.valueOf(((Servicio)tblServicio.getSelectionModel().getSelectedItem()).getTelefonoContacto()));
        comboboxEmpresa.getSelectionModel().select(buscarEmpresa(((Servicio)tblServicio.getSelectionModel().getSelectedItem()).getEmpresas_codigoEmpresa()));
    }  
    
    public void cargarDatos(){
        tblServicio.setItems(getServicio());
        colCodigoServicios.setCellValueFactory(new PropertyValueFactory<Servicio, Integer>("codigoServicio"));
        colFechaServicios.setCellValueFactory(new PropertyValueFactory<Servicio, Date>("fechaServicio"));
        colTipoServicios.setCellValueFactory(new PropertyValueFactory<Servicio, String>("tipoServicio"));
        colHoraServicios.setCellValueFactory(new PropertyValueFactory<Servicio, String>("horaServicio"));
        colLugarServicios.setCellValueFactory(new PropertyValueFactory<Servicio, String>("lugarServicio"));
        colTelefonoContacto.setCellValueFactory(new PropertyValueFactory<Servicio, String>("telefonoContacto"));
        colEmpresa.setCellValueFactory(new PropertyValueFactory<Servicio, Integer>("empresas_codigoEmpresa"));
        comboboxEmpresa.setItems(getEmpresa());
        desactivarControles();
    }
    
    
    public Empresa buscarEmpresa(int codigoEmpresa){
        Empresa resultado = null;
        try{
            PreparedStatement pd= Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarEmpresas(?)}");
            pd.setInt(1, codigoEmpresa);
            ResultSet reg= pd.executeQuery();
            while(reg.next()){
                resultado = new Empresa (reg.getInt("codigoEmpresa"),
                                        reg.getString("nombreEmpresa"),
                                        reg.getString("direccion"),
                                        reg.getString("telefono"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
            return resultado;
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
