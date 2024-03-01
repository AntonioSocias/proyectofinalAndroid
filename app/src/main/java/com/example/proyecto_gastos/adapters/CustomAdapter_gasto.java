package com.example.proyecto_gastos.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.proyecto_gastos.Proyectos;
import com.example.proyecto_gastos.R;
import com.example.proyecto_gastos.models.Gasto;
import com.example.proyecto_gastos.models.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CustomAdapter_gasto extends ArrayAdapter {
    List<Gasto> listaGastos;
    String nombrePagador;
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
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mContext.getResources().getString(R.string.url_domain))//VOLVER A PONER QUE ACCEDA A STRINGS
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final View vistaPersonalizada = inflater.inflate(R.layout.activity_customadapter_gastos, parent, false);
        TextView textViewTitulo = vistaPersonalizada.findViewById(R.id.textViewTituloGasto);
        TextView textViewPagado = vistaPersonalizada.findViewById(R.id.textViewPagadoGasto);
        TextView textViewCantidad = vistaPersonalizada.findViewById(R.id.textViewCantidadGasto);
        TextView textViewFecha = vistaPersonalizada.findViewById(R.id.textViewFechaGasto);

        Gasto currentItem = listaGastos.get(position);

        if (currentItem != null){
            textViewTitulo.setText(currentItem.getTitulo());
            /**
             * Obtengo el nombre del usuario
             */
            Proyectos usuariosService = retrofit.create(Proyectos.class);
            Call<List<Usuario>> llamada = usuariosService.obtenerUsuariosProyecto(listaGastos.get(0).getProyecto());
            llamada.enqueue(new Callback<List<Usuario>>() {
                @Override
                public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                    boolean found = false;
                    for (Usuario usuario : response.body()) {
                        if (usuario.getId() == currentItem.getPagador()) {
                            nombrePagador = usuario.getNombre();
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        nombrePagador = "Pagador Desconocido";
                    }
                    textViewPagado.setText("Ha sido pagado por " + nombrePagador);
                }

                @Override
                public void onFailure(Call<List<Usuario>> call, Throwable t) {
                    System.out.println("eee");
                }
            });
            textViewCantidad.setText(
                    currentItem.getCantidadFormateada() + mContext.getString(R.string.moneda));
            textViewFecha.setText(currentItem.getFechaFormateada());
        }

        return vistaPersonalizada;
    }
}
