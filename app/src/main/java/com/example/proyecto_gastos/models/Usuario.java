package com.example.proyecto_gastos.models;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class Usuario implements Serializable {
    int id;
    int proyecto;
    String nombre, password;



    public Usuario(int id, String nombre,String password, int proyecto) {
        this.id = id;
        this.nombre = nombre;
        this.proyecto = proyecto;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
