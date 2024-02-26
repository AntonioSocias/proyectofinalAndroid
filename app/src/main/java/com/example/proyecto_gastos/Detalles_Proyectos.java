package com.example.proyecto_gastos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.proyecto_gastos.adapters.CustomAdapter_gasto;
import com.example.proyecto_gastos.models.Gasto;
import com.example.proyecto_gastos.models.Proyecto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Detalles_Proyectos extends AppCompatActivity {
    TextView txTitulo;
    ListView lv_gastos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_proyectos);
        Proyecto proyecto = (Proyecto) getIntent().getSerializableExtra("proyecto");
        txTitulo = findViewById(R.id.textView2);
        lv_gastos = findViewById(R.id.lv_listado_gastos);

        txTitulo.setText(proyecto.getTitulo());

        /**
         * LISTA DE GASTOS
         */
        List listaGastos = new ArrayList();
        /**
         * METODO REST DONDE DEVUELVA TODOS LOS GASTOS ASOCIADOS DONDE EL CAMPO PROYECTO SEA
         * EL ID DEL PROYECTO AL QUE HE ENTRADO
         */
        listaGastos.add(new Gasto("Prueba 1", "Bruno", 200.2F, new Date()));
        int i=1;
        while (i<20){
            i++;
            listaGastos.add(new Gasto("Prueba "+i, "Bruno", 200.2F, new Date()));
        }
        lv_gastos.setAdapter(new CustomAdapter_gasto(Detalles_Proyectos.this, listaGastos));

        /**
         * LISTA DE PARTICIPANTES
         */
        List listaParticipantes = new ArrayList();

        /**
         * METODO REST DONDE DEVUELVA TODOS LOS PARTICIPANTES DONDE EL
         */
    }
}