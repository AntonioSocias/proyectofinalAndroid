package com.example.proyecto_gastos.models;

import com.example.proyecto_gastos.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Gasto {
    String titulo, pagado;
    Float cantidad;
    Date fecha;


    public Gasto(String titulo, String pagado, Float cantidad, Date fecha) {
        this.titulo = titulo;
        this.pagado = pagado;
        this.cantidad = cantidad;
        this.fecha = fecha;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getPagado() {
        return pagado;
    }

    public void setPagado(String pagado) {
        this.pagado = pagado;
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