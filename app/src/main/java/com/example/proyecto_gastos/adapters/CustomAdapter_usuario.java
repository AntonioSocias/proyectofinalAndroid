package com.example.proyecto_gastos.adapters;

import static com.example.proyecto_gastos.Detalles_Proyectos.lista_gastos_proyecto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.proyecto_gastos.Detalles_Proyectos;
import com.example.proyecto_gastos.Proyectos;
import com.example.proyecto_gastos.R;
import com.example.proyecto_gastos.models.Gasto;
import com.example.proyecto_gastos.models.Usuario;

import java.text.DecimalFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CustomAdapter_usuario extends ArrayAdapter {
    List<Usuario> listaUsuarios;
    Float gastoCliente;
    //CONTEXTO PARA PODE USAR getString()
    private Context mContext;
    public CustomAdapter_usuario(@NonNull Context context, List<Usuario> lista) {
        super(context, 0, lista);
        this.listaUsuarios = lista;
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
        final View vistaPersonalizada = inflater.inflate(R.layout.activity_customadapter_usuario, parent, false);
        TextView textViewNombre = vistaPersonalizada.findViewById(R.id.textViewNombreUsuario);
        TextView textViewGasto = vistaPersonalizada.findViewById(R.id.textViewGastoUsuario);

        Usuario currentItem = listaUsuarios.get(position);

        if (currentItem != null){
            textViewNombre.setText(currentItem.getNombre());
            /**
             * Obtengo el nombre del usuario
             */
            Proyectos proyectosService = retrofit.create(Proyectos.class);
            Call<List<Gasto>> llamada = proyectosService.obtenerGastosProyecto(currentItem.getProyecto());
            llamada.enqueue(new Callback<List<Gasto>>() {
                @Override
                public void onResponse(Call<List<Gasto>> call, Response<List<Gasto>> response) {
                    for (Gasto gasto : response.body()) {
                        if (gasto.getPagador() == currentItem.getId()) {
                            gastoCliente=calcularGastosTotalUsuarios(currentItem);
                        }
                    }
                    textViewGasto.setText(String.format("%.2f", gastoCliente) + mContext.getString(R.string.moneda));
                }

                @Override
                public void onFailure(Call<List<Gasto>> call, Throwable t) {
                    textViewGasto.setText("No recogido");
                }
            });
        }

        return vistaPersonalizada;
    }
    public float calcularGastosTotalUsuarios(Usuario usuario){
        float acumGasto=0;
        for (Gasto gasto: Detalles_Proyectos.lista_gastos_proyecto) {
            if (gasto.getPagador()==usuario.getId()){
                acumGasto+=gasto.getCantidad();
            }
        }
        return acumGasto;
    }
}
