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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import org.andreortega.bd.Conexion;
import org.andreortega.bean.TipoPlato;

public class TipoPlatoController implements Initializable {

    private enum  Operacion{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, NINGUNO};
    
    private Operacion tipoOperacion = Operacion.NINGUNO;
    
    @FXML private Button closeButton;

    @FXML private JFXButton btnNuevo;

    @FXML private JFXButton btnEditar;

    @FXML private JFXButton btnEliminar;

    @FXML private JFXTextField codigo;

    @FXML private JFXTextField descripcion;

    @FXML private TableView tblTipoPlato;

    @FXML private TableColumn colCodigo;

    @FXML private TableColumn colDescripcion;
    
        private ObservableList<TipoPlato> listaTipoPlato;
    
    
    public void desactivarControles(){
        codigo.setEditable(false);
        descripcion.setEditable(false);
    }
    
    public void activarControles(){
        descripcion.setEditable(true);
    }
    
    private void limpiarControles(){
        codigo.setText("");
        descripcion.setText("");
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
        
            public void guardar(){  
   
        if(descripcion.getText().length()==0){
            ImageIcon icon = new ImageIcon(EmpresasController.class.getResource("/org/andreortega/img/alto.png"));
        JOptionPane.showMessageDialog(null, "Esta vacio, tienes que colocar datos","Aviso",0,icon);  
                
        }else{
        TipoPlato TipoPlatoN = new TipoPlato();
        TipoPlatoN.setDescripcionTipo(descripcion.getText());

         try{
            PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarTipoPlato(?)}");
            sp.setString(1,TipoPlatoN.getDescripcionTipo());
            sp.execute();
            listaTipoPlato.add(TipoPlatoN);
            
            
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
                    if(tblTipoPlato.getSelectionModel().getSelectedItem() !=null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar?","Eliminar  Empresa",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                        if(respuesta == JOptionPane.YES_OPTION){
                            try{
                                
                                PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarTipoPlato(?)}");
                                sp.setInt(1, ((TipoPlato)tblTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
                                sp.execute();
                                listaTipoPlato.remove(tblTipoPlato.getSelectionModel().getSelectedIndex());
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
            
             public void editar(){
        switch(tipoOperacion){
            case NINGUNO:
                if(tblTipoPlato.getSelectionModel().getSelectedItem() !=null){
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
             
             
            public void actualizar(){
        
        try{
        PreparedStatement sp = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarTipoPlato(?,?)}");
        TipoPlato tiEmpAct = ((TipoPlato)tblTipoPlato.getSelectionModel().getSelectedItem());
        //Obtener Datos para DB
        tiEmpAct.setDescripcionTipo(descripcion.getText());

        
        // Enviar Datos DB
        sp.setInt(1, tiEmpAct.getCodigoTipoPlato());
        sp.setString(2, tiEmpAct.getDescripcionTipo());

        sp.execute();
        JOptionPane.showMessageDialog(null, "actualizado");
        }catch(Exception e){
            e.printStackTrace();
        }
    }         
    public void cargarDatos(){
        tblTipoPlato.setItems(getTipoPlato());
        colCodigo.setCellValueFactory(new PropertyValueFactory<TipoPlato, Integer>("codigoTipoPlato"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<TipoPlato, String>("descripcionTipo"));
    }
    
    public void seleccionarElementos(){
        codigo.setText(String.valueOf(((TipoPlato)tblTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato()));
        descripcion.setText(((TipoPlato)tblTipoPlato.getSelectionModel().getSelectedItem()).getDescripcionTipo());
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
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cargarDatos();
    }    
    public void handleCloseButtonAction(ActionEvent event) {
    Stage stage = (Stage) closeButton.getScene().getWindow();
    stage.close();
}
}
