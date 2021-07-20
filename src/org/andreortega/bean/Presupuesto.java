/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.andreortega.bean;

import java.util.Date;

/**
 *
 * @author javir
 */
public class Presupuesto {
    
    private int codigoPresupuesto;
    private Date fechaSolicitud;
    private Double cantidadPresupuesto;
    private int codigoEmpresa;

    public Presupuesto(){
    }

    
    public Presupuesto(int codigoPresupuesto, Date fechaSolicitud, Double cantidadPresupuesto, int codigoEmpresa) {
        this.codigoPresupuesto = codigoPresupuesto;
        this.fechaSolicitud = fechaSolicitud;
        this.cantidadPresupuesto = cantidadPresupuesto;
        this.codigoEmpresa = codigoEmpresa;
    }

    
    
    
    public int getCodigoPresupuesto() {
        return codigoPresupuesto;
    }

    public void setCodigoPresupuesto(int codigoPresupuesto) {
        this.codigoPresupuesto = codigoPresupuesto;
    }

    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public Double getCantidadPresupuesto() {
        return cantidadPresupuesto;
    }

    public void setCantidadPresupuesto(Double cantidadPresupuesto) {
        this.cantidadPresupuesto = cantidadPresupuesto;
    }

    public int getCodigoEmpresa() {
        return codigoEmpresa;
    }

    public void setCodigoEmpresa(int codigoEmpresa) {
        this.codigoEmpresa = codigoEmpresa;
    }

    @Override
    public String toString() {
        return "Presupuesto{" + "codigoPresupuesto=" + codigoPresupuesto + ", fechaSolicitud=" + fechaSolicitud + ", cantidadPresupuesto=" + cantidadPresupuesto + ", codigoEmpresa=" + codigoEmpresa + '}';
    }
    
    
    
    
    
    
}
