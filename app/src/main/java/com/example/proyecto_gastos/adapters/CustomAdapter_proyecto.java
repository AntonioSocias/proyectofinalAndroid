package com.example.proyecto_gastos.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.proyecto_gastos.models.Proyecto;
import com.example.proyecto_gastos.R;

import java.util.List;

public class CustomAdapter_proyecto extends ArrayAdapter {
    List<Proyecto> listaProyectos;
    public CustomAdapter_proyecto(@NonNull Context context, List<Proyecto> lista) {
        super(context, 0, lista);
        this.listaProyectos = lista;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getDropDownView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View vistaPersonalizada = inflater.inflate(R.layout.activity_customadapter_proyecto, parent, false);
        TextView textViewNombre = vistaPersonalizada.findViewById(R.id.textViewNombreProyecto);
        TextView textViewDescripcion = vistaPersonalizada.findViewById(R.id.textViewDescripcionProyecto);

        Proyecto currentItem = listaProyectos.get(position);

        if (currentItem != null){
            textViewNombre.setText(currentItem.getTitulo());
            textViewDescripcion.setText(currentItem.getDescripcion());
        }

        return vistaPersonalizada;
    }
}
