package com.example.proyecto_gastos.models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RelacionProyectoUsuario implements Serializable{
    int id, proyecto_id,usuario_id;


    public RelacionProyectoUsuario(int id, int proyecto_id, int usuario_id) {
        this.id=id;
        this.proyecto_id = proyecto_id;
        this.usuario_id = usuario_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProyecto_id() {
        return proyecto_id;
    }

    public void setProyecto_id(int proyecto_id) {
        this.proyecto_id = proyecto_id;
    }

    public int getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(int usuario_id) {
        this.usuario_id = usuario_id;
    }
}
