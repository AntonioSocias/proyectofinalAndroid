package com.example.proyecto_gastos.models;

import java.io.Serializable;

public class Usuario implements Serializable {
    int id;
    String Nombre;

    public Usuario(int id, String nombre) {
        this.id = id;
        this.Nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }
}
