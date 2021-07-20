/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.andreortega.bean;

/**
 *
 * @author javir
 */
public class Platos {
    
    private int codigoPlato;
    private String nombrePlato;
    private String descripcionPlato;
    private int cantidad;
    private double precioPlato;
    private int tipoplato_codigoTipoPlato;


    public Platos(){
    }

    public Platos(int codigoPlato, String nombrePlato, String descripcionPlato, int cantidad, double precioPlato, int tipoplato_codigoTipoPlato) {
        this.codigoPlato = codigoPlato;
        this.nombrePlato = nombrePlato;
        this.descripcionPlato = descripcionPlato;
        this.cantidad = cantidad;
        this.precioPlato = precioPlato;
        this.tipoplato_codigoTipoPlato = tipoplato_codigoTipoPlato;
    }

    public int getCodigoPlato() {
        return codigoPlato;
    }

    public void setCodigoPlato(int codigoPlato) {
        this.codigoPlato = codigoPlato;
    }

    public String getNombrePlato() {
        return nombrePlato;
    }

    public void setNombrePlato(String nombrePlato) {
        this.nombrePlato = nombrePlato;
    }

    public String getDescripcionPlato() {
        return descripcionPlato;
    }

    public void setDescripcionPlato(String descripcionPlato) {
        this.descripcionPlato = descripcionPlato;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioPlato() {
        return precioPlato;
    }

    public void setPrecioPlato(double precioPlato) {
        this.precioPlato = precioPlato;
    }

    public int getTipoplato_codigoTipoPlato() {
        return tipoplato_codigoTipoPlato;
    }

    public void setTipoplato_codigoTipoPlato(int tipoplato_codigoTipoPlato) {
        this.tipoplato_codigoTipoPlato = tipoplato_codigoTipoPlato;
    }



    @Override
    public String toString() {
        return getCodigoPlato()+"  "+getNombrePlato();
    }
    
    
    
    
    
    
    
    
    
}
