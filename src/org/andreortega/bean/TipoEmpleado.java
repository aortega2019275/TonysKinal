/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.andreortega.bean;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.andreortega.bd.Conexion;

/**
 *
 * @author javir
 */
public class TipoEmpleado {
    
    private int codigoTipoEmpleado;
    private String descripcion;
    
    public TipoEmpleado(){
    }
    
    public TipoEmpleado(int codigoTipoEmpleado, String descripcion){
        this.codigoTipoEmpleado=codigoTipoEmpleado;
        this.descripcion=descripcion;
    }
    

    public int getCodigoTipoEmpleado() {
        return codigoTipoEmpleado;
    }

    public void setCodigoTipoEmpleado(int codigoTipoEmpleado) {
        this.codigoTipoEmpleado = codigoTipoEmpleado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
    
    
        public ObservableList<TipoEmpleado> getTipoEmpleado(){
         ObservableList<TipoEmpleado> lista = FXCollections.observableArrayList();
        try{
             PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_listarTipoEmpleado()}");
             ResultSet resultado = procedimiento.executeQuery();
             while(resultado.next()){
                 String codigoTipoEmpleado = resultado.getString("codigoTipoEmpleado");
                 String descripcion = resultado.getString("descripcion");
                 
                 
                TipoEmpleado te= new TipoEmpleado(Integer.parseInt(codigoTipoEmpleado),descripcion);
                
                lista.add(te);
             }
             }catch(Exception e){
            e.printStackTrace();
        }
        return lista;    

}
}
