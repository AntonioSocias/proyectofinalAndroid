package com.example.proyecto_gastos.models;

import java.io.Serializable;

public class Usuario implements Serializable {
    int id;
    int proyecto;
    String nombre;


    public Usuario(int id, String nombre, int proyecto) {
        this.id = id;
        this.nombre = nombre;
        this.proyecto = proyecto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        nombre = nombre;
    }
    public int getProyecto() {
        return proyecto;
    }

    public void setProyecto(int proyecto) {
        this.proyecto = proyecto;
    }
}
