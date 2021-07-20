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
public class Servicios_has_Platos {
    
    private int Servicios_codigoServicio;
    private int Platos_codigoPlato;

    public Servicios_has_Platos() {
    }

    public Servicios_has_Platos(int Servicios_codigoServicio, int Platos_codigoPlato) {
        this.Servicios_codigoServicio = Servicios_codigoServicio;
        this.Platos_codigoPlato = Platos_codigoPlato;
    }

    public int getServicios_codigoServicio() {
        return Servicios_codigoServicio;
    }

    public void setServicios_codigoServicio(int Servicios_codigoServicio) {
        this.Servicios_codigoServicio = Servicios_codigoServicio;
    }

    public int getPlatos_codigoPlato() {
        return Platos_codigoPlato;
    }

    public void setPlatos_codigoPlato(int Platos_codigoPlato) {
        this.Platos_codigoPlato = Platos_codigoPlato;
    }
    
    
    
}
