package com.example.proyecto_gastos.models;

import java.io.Serializable;

public class Proyecto implements Serializable {
    int id, administrador, moneda;
    float total_gastos;
    String titulo, descripcion;

    public Proyecto(int id, int administrador, int moneda, float total_gastos, String titulo, String descripcion) {
        this.id = id;
        this.administrador = administrador;
        this.moneda = moneda;
        this.total_gastos = total_gastos;
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAdministrador() {
        return administrador;
    }

    public void setAdministrador(int administrador) {
        this.administrador = administrador;
    }

    public int getMoneda() {
        return moneda;
    }

    public void setMoneda(int moneda) {
        this.moneda = moneda;
    }

    public float getTotal_gastos() {
        return total_gastos;
    }

    public void setTotal_gastos(int total_gastos) {
        this.total_gastos = total_gastos;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
