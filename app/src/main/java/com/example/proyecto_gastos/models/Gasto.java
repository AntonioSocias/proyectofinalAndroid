package com.example.proyecto_gastos.models;

import com.example.proyecto_gastos.R;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Gasto implements Serializable{
    int id;
    String titulo, pagador;
    Float cantidad;
    Date fecha;


    public Gasto(int id, String titulo, String pagador, Float cantidad, Date fecha) {
        this.id=id;
        this.titulo = titulo;
        this.pagador = pagador;
        this.cantidad = cantidad;
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getPagador() {
        return pagador;
    }

    public void setPagador(String pagado) {
        this.pagador = pagado;
    }

    public Float getCantidad() {
        return cantidad;
    }

    public String getCantidadFormateada(){
        String cadena = String.format("%.2f", getCantidad());
        return cadena;
    }

    public void setCantidad(Float cantidad) {
        this.cantidad = cantidad;
    }

    public Date getFecha() {
        return fecha;
    }

    public String getFechaFormateada() {
        SimpleDateFormat sdf = new SimpleDateFormat( "dd/MM/yyyy", Locale.getDefault());

        // Formatear la fecha
        String fechaFormateada = sdf.format(getFecha());

        return fechaFormateada;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
