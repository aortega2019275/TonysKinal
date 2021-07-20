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
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import org.andreortega.bd.Conexion;
import org.andreortega.bean.Empresa;
import org.andreortega.bean.Presupuesto;
import org.andreortega.system.MainApp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javafx.scene.layout.GridPane;
import org.andreortega.report.GenerarReporte;

/**
 * FXML Controller class
 *
 * @author javir
 */
public class PresupuestoController implements Initializable {
    
    private final String log="/org/andreortega/img/IconB.png";
    private final String bot="/org/andreortega/img/fondo1.png";
    private final String top="/org/andreortega/img/fondo2.png";
    
    private MainApp sceneMain;
    
    @FXML private JFXButton btnNuevo;

    @FXML private JFXButton btnEditar;

    @FXML private JFXButton btnEliminar;

    @FXML private JFXButton btnReportar;

    @FXML private JFXTextField codigo;

    @FXML private JFXTextField cantidad;

    @FXML private DatePicker fechaS;

    @FXML private TableColumn colCodigoPre;

    @FXML private TableColumn colFechaSoli;

    @FXML private TableColumn colCantidadPre;
    
    @FXML private TableColumn colCodigoEmp;
    
    @FXML private TableView tblPresupuesto;
    
    @FXML private ComboBox cbEmpresa;
    
    @FXML private GridPane grpFechaS;
    
    
    private enum  Operacion{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, NINGUNO};
    private Operacion tipoOperacion = Operacion.NINGUNO;
    
    private ObservableList<Presupuesto> listaPresupuesto;
    
    private ObservableList<Empresa> listaEmpresa;
    
    
    
    public void desactivarControles(){
        codigo.setEditable(false);
        cantidad.setEditable(false);
        cbEmpresa.setDisable(true);
        grpFechaS.setDisable(true);

    }
    
    public void activarControles(){
        //codigo.setEditable(false);
        cantidad.setEditable(true);
        cbEmpresa.setDisable(false);
        grpFechaS.setDisable(false);
    }
    
    private void limpiarControles(){
        codigo.setText("");
        cantidad.setText("");
        cbEmpresa.getSelectionModel().clearSelection();
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
            if(cbEmpresa.getSelectionModel().getSelectedItem() !=null){
            Map parametros = new HashMap();
            int codEmpresa = Integer.valueOf(((Empresa)cbEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
            parametros.put("log", this.getClass().getResourceAsStream(log));
            parametros.put("bot", this.getClass().getResourceAsStream(bot));
            parametros.put("top", this.getClass().getResourceAsStream(top));
            parametros.put("codEmpresa", codEmpresa);
            GenerarReporte.mostrarReporte("ReportePresupuesto.jasper", "Reporte de Presusupuesto", parametros);
            }else{
                ImageIcon icon = new ImageIcon(EmpresasController.class.getResource("/org/andreortega/img/alto.png"));
                JOptionPane.showMessageDialog(null, "debe seleccionar Un registro para generar un reporte","Aviso",0,icon);
            }
        }
        
    public void guardar(){  
   
     if(cantidad.getText().length()==0){
            ImageIcon icon = new ImageIcon(EmpresasController.class.getResource("/org/andreortega/img/alto.png"));
        JOptionPane.showMessageDialog(null, "Esta vacio, tienes que colocar datos","Aviso",0,icon);  
        }else{

        
        Presupuesto PresupuestoN = new Presupuesto();
        PresupuestoN.setFechaSolicitud(fechaS.getSelectedDate());
        PresupuestoN.setCantidadPresupuesto(Double.parseDouble(cantidad.getText()));
        PresupuestoN.setCodigoEmpresa(((Empresa)cbEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
         try{
            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarPresupuesto(?,?,?)}");
            sp.setDate(1, new java.sql.Date(PresupuestoN.getFechaSolicitud().getTime()));
            sp.setDouble(2,PresupuestoN.getCantidadPresupuesto());
            sp.setInt(3, PresupuestoN.getCodigoEmpresa());
            sp.execute();
            listaPresupuesto.add(PresupuestoN); 
                }catch(Exception e){
                 e.printStackTrace();
            }
        }
    } 
    
        
    public void editar(){
        switch(tipoOperacion){
            case NINGUNO:
                if(tblPresupuesto.getSelectionModel().getSelectedItem() !=null){
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
                limpiarControles();
                btnEditar.setText("Editar");
                btnEliminar.setText("Eliminar");
                btnReportar.setDisable(false);
                btnNuevo.setDisable(false);
                tipoOperacion = Operacion.NINGUNO;
                cargarDatos();
                    break;
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
                    if(tblPresupuesto.getSelectionModel().getSelectedItem() !=null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar?","Eliminar  Empresa",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                        if(respuesta == JOptionPane.YES_OPTION){
                            try{
                                
                                PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarPresupuesto(?)}");
                                sp.setInt(1, ((Presupuesto)tblPresupuesto.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
                                sp.execute();
                                listaPresupuesto.remove(tblPresupuesto.getSelectionModel().getSelectedIndex());
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
        PreparedStatement pd = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarPresupuesto(?,?,?)}");
        Presupuesto PresuAct = ((Presupuesto)tblPresupuesto.getSelectionModel().getSelectedItem());

        PresuAct.setCodigoPresupuesto(Integer.parseInt(codigo.getText()));
        PresuAct.setFechaSolicitud(fechaS.getSelectedDate());
        PresuAct.setCantidadPresupuesto(Double.parseDouble(cantidad.getText()));

        pd.setInt(1, PresuAct.getCodigoPresupuesto());
        pd.setDate(2, new java.sql.Date(PresuAct.getFechaSolicitud().getTime()));
        pd.setDouble(3, PresuAct.getCantidadPresupuesto());


        pd.execute();
        JOptionPane.showMessageDialog(null, "actualizado");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void seleccionarElementos(){
        codigo.setText(String.valueOf(((Presupuesto)tblPresupuesto.getSelectionModel().getSelectedItem()).getCodigoPresupuesto()));
        fechaS.selectedDateProperty().set(((Presupuesto)tblPresupuesto.getSelectionModel().getSelectedItem()).getFechaSolicitud());
        cantidad.setText(String.valueOf(((Presupuesto)tblPresupuesto.getSelectionModel().getSelectedItem()).getCantidadPresupuesto()));
        cbEmpresa.getSelectionModel().select(buscarEmpresa(((Presupuesto)tblPresupuesto.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
    }  
    
    
    public void cargarDatos(){
        tblPresupuesto.setItems(getPresupuesto());
        colCodigoPre.setCellValueFactory(new PropertyValueFactory<Presupuesto, Integer>("codigoPresupuesto"));
        colFechaSoli.setCellValueFactory(new PropertyValueFactory<Presupuesto, Date>("fechaSolicitud"));
        colCantidadPre.setCellValueFactory(new PropertyValueFactory<Presupuesto, String>("cantidadPresupuesto"));
        colCodigoEmp.setCellValueFactory(new PropertyValueFactory<Presupuesto, Integer>("codigoEmpresa"));
        cbEmpresa.setItems(getEmpresa());
        desactivarControles();
    }
    
    
    public ObservableList<Presupuesto> getPresupuesto(){
        ArrayList<Presupuesto> lista = new ArrayList<Presupuesto>();
        try{
            PreparedStatement ps= Conexion.getInstance().getConexion().prepareCall("{call sp_listarPresupuesto()}");
            ResultSet resultado = ps.executeQuery();
            while(resultado.next()){
            lista.add(new Presupuesto(resultado.getInt("codigoPresupuesto"),
                resultado.getDate("fechaSolicitud"),
                resultado.getDouble("cantidadPresupuesto"),
                resultado.getInt("codigoEmpresa")));  
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    return listaPresupuesto = FXCollections.observableArrayList(lista);
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
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
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
