
package org.andreortega.bean;

import java.util.Date;

public class Servicios_has_Empleados {
   
    private int codigoServicio_has_Empleado;
    
    private int Servicios_codigoServicio;
    
    private int Empleados_codigoEmpleado;
    
    private Date fechaEvento;
    
    private String horaEvento;
    
    private String lugarevento;

    public Servicios_has_Empleados() {
    }

    public Servicios_has_Empleados(int codigoServicio_has_Empleado, int Servicios_codigoServicio, int Empleados_codigoEmpleado, Date fechaEvento, String horaEvento, String lugarevento) {
        this.codigoServicio_has_Empleado = codigoServicio_has_Empleado;
        this.Servicios_codigoServicio = Servicios_codigoServicio;
        this.Empleados_codigoEmpleado = Empleados_codigoEmpleado;
        this.fechaEvento = fechaEvento;
        this.horaEvento = horaEvento;
        this.lugarevento = lugarevento;
    }

    
    public int getCodigoServicio_has_Empleado() {
        return codigoServicio_has_Empleado;
    }

    public void setCodigoServicio_has_Empleado(int codigoServicio_has_Empleado) {
        this.codigoServicio_has_Empleado = codigoServicio_has_Empleado;
    }

    public int getServicios_codigoServicio() {
        return Servicios_codigoServicio;
    }

    public void setServicios_codigoServicio(int Servicios_codigoServicio) {
        this.Servicios_codigoServicio = Servicios_codigoServicio;
    }

    public int getEmpleados_codigoEmpleado() {
        return Empleados_codigoEmpleado;
    }

    public void setEmpleados_codigoEmpleado(int Empleados_codigoEmpleado) {
        this.Empleados_codigoEmpleado = Empleados_codigoEmpleado;
    }

    public Date getFechaEvento() {
        return fechaEvento;
    }

    public void setFechaEvento(Date fechaEvento) {
        this.fechaEvento = fechaEvento;
    }

    public String getHoraEvento() {
        return horaEvento;
    }

    public void setHoraEvento(String horaEvento) {
        this.horaEvento = horaEvento;
    }

    public String getLugarevento() {
        return lugarevento;
    }

    public void setLugarevento(String lugarevento) {
        this.lugarevento = lugarevento;
    }
    
    
}
