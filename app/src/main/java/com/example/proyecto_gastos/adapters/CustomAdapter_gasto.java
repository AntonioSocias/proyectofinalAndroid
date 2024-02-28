package com.example.proyecto_gastos.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.proyecto_gastos.R;
import com.example.proyecto_gastos.models.Gasto;

import java.util.List;

public class CustomAdapter_gasto extends ArrayAdapter {
    List<Gasto> listaGastos;

    //CONTEXTO PARA PODE USAR getString()
    private Context mContext;
    public CustomAdapter_gasto(@NonNull Context context, List<Gasto> lista) {
        super(context, 0, lista);
        this.listaGastos = lista;
        this.mContext=context;
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
        final View vistaPersonalizada = inflater.inflate(R.layout.activity_customadapter_gastos, parent, false);
        TextView textViewTitulo = vistaPersonalizada.findViewById(R.id.textViewTituloGasto);
        TextView textViewPagado = vistaPersonalizada.findViewById(R.id.textViewPagadoGasto);
        TextView textViewCantidad = vistaPersonalizada.findViewById(R.id.textViewCantidadGasto);
        TextView textViewFecha = vistaPersonalizada.findViewById(R.id.textViewFechaGasto);

        Gasto currentItem = listaGastos.get(position);

        if (currentItem != null){
            textViewTitulo.setText(currentItem.getTitulo());
            textViewPagado.setText("Ha sido pagado por " + currentItem.getPagador());
            textViewCantidad.setText(
                    currentItem.getCantidadFormateada() + mContext.getString(R.string.moneda));
            textViewFecha.setText(currentItem.getFechaFormateada());
        }

        return vistaPersonalizada;
    }
}
