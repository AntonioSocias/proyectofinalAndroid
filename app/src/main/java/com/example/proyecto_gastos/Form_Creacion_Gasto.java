package com.example.proyecto_gastos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.proyecto_gastos.models.Gasto;
import com.example.proyecto_gastos.models.Proyecto;
import com.example.proyecto_gastos.models.Usuario;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Form_Creacion_Gasto extends AppCompatActivity {
    EditText et_concepto, et_cantidad, et_fecha;
    Spinner spn_pagador;
    Button btn_aceptar, btn_cancelar;
    Retrofit retrofit;
    String nombreUsuarioLogeado;
    List<Usuario> lista_usuarios;
    Usuario usuarioLogeado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_creacion_gasto);
        Proyecto proyecto = (Proyecto) getIntent().getSerializableExtra("proyecto");
        retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.url_domain))//VOLVER A PONER QUE ACCEDA A STRINGS
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        et_concepto = findViewById(R.id.editTextConcepto);
        et_cantidad = findViewById(R.id.editTextCantidad);
        et_fecha = findViewById(R.id.editTextDate);
        spn_pagador = findViewById(R.id.spn_Pagador);
        btn_aceptar = findViewById(R.id.buttonAceptar);
        btn_cancelar = findViewById(R.id.buttonCancelar);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        /**
         * RECUPERO EL USUARIOlOGEADO
         */
        nombreUsuarioLogeado = sharedPreferences.getString("usuario", "");

        /**
         * OBTENGO LOS USUARIOS PERTENECIENTES AL PROYECTO
         * Y LOS METO EN EL LISTVIEW
         */
        obtenerUsariosProyecto(proyecto);

        /**
         * obtengo los datos en caso de que se haya venido a editar un gasto
         */
        Intent intent = getIntent();
        Gasto gastoRecuperado = (Gasto) intent.getSerializableExtra("gasto");
        if (gastoRecuperado!=null){
            et_concepto.setText(gastoRecuperado.getTitulo());
            et_cantidad.setText(String.valueOf(gastoRecuperado.getCantidad()));
            et_fecha.setText(gastoRecuperado.getFechaFormateada());

            /**
             * COMPROBAR RECUPERAR EL PAGADOR Y PONERLO
             * COMO SELECCIONADO EN EL SPINNER
             */
            //spn_pagador.setSelection(lista_usuarios.get(seleccion));

            btn_aceptar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String concepto = et_concepto.getText().toString();
                        /**
                         * CONVERSION CANTIDAD
                         */
                        String cantidadString = et_cantidad.getText().toString();
                        Float cantidadFloat = null;
                        cantidadFloat = Float.parseFloat(cantidadString);
                        /**
                         * CONVERSION FECHA ---------MIRAR DE USAR UN DATEPICKER
                         */
                        String fechaString = et_fecha.getText().toString();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");


                        Date fechaDate = dateFormat.parse(fechaString);

                        /**
                         * MODIFICAR PAGADOR SPINNER
                         */
                        String pagadorNombre = spn_pagador.getSelectedItem().toString();
                        int pagadorId = -1;
                        for (Usuario usuario : lista_usuarios) {
                            if (usuario.getNombre().equals(pagadorNombre)){
                                pagadorId = usuario.getId();
                            }
                        }

                        Gasto gasto = new Gasto(1, concepto, pagadorId, cantidadFloat, fechaDate, proyecto.getId());
                        crearGasto(gasto);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }else{
            btn_aceptar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String concepto = et_concepto.getText().toString();
                        /**
                         * CONVERSION CANTIDAD
                         */
                        String cantidadString = et_cantidad.getText().toString();
                        Float cantidadFloat = null;
                        cantidadFloat = Float.parseFloat(cantidadString);
                        /**
                         * CONVERSION FECHA ---------MIRAR DE USAR UN DATEPICKER
                         */
                        String fechaString = et_fecha.getText().toString();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");


                        Date fechaDate = dateFormat.parse(fechaString);

                        /**
                         * MODIFICAR PAGADOR SPINNER
                         */
                        String pagadorNombre = spn_pagador.getSelectedItem().toString();
                        int pagadorId = -1;
                        for (Usuario usuario : lista_usuarios) {
                            if (usuario.getNombre().equals(pagadorNombre)){
                                pagadorId = usuario.getId();
                            }
                        }

                        Gasto gasto = new Gasto(1, concepto, pagadorId, cantidadFloat, fechaDate, proyecto.getId());
                        crearGasto(gasto);

                    } catch (ParseException e) {
                        e.printStackTrace();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            btn_cancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }


    }

    public void crearGasto(Gasto gasto) {
        Gastos gastosService = retrofit.create(Gastos.class);
        Call<Gasto> llamada = gastosService.crearGasto(gasto);
        llamada.enqueue(new Callback<Gasto>() {
            @Override
            public void onResponse(Call<Gasto> call, Response<Gasto> response) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("gastoCreado", gasto); // Suponiendo que 'gasto' es el objeto Gasto creado
                setResult(Form_Creacion_Gasto.RESULT_OK, resultIntent);
                finish();
            }
            @Override
            public void onFailure(Call<Gasto> call, Throwable t) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }
    public void editarGasto(Gasto gasto) {
        Gastos gastosService = retrofit.create(Gastos.class);
        Call<Gasto> llamada = gastosService.editarGasto(gasto.getId(), gasto);
        llamada.enqueue(new Callback<Gasto>() {
            @Override
            public void onResponse(Call<Gasto> call, Response<Gasto> response) {
                if (response.isSuccessful()) {
                    Gasto gastoActualizado = response.body();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("gastoActualizado", gastoActualizado);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    setResult(RESULT_CANCELED);
                    finish();
                }
            }
            @Override
            public void onFailure(Call<Gasto> call, Throwable t) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    public void obtenerUsariosProyecto(Proyecto proyecto) {
        Proyectos usuariosService = retrofit.create(Proyectos.class);
        Call<List<Usuario>> llamada = usuariosService.obtenerUsuariosProyecto(proyecto.getId());
        llamada.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                lista_usuarios = response.body();
                List<String> lista_nombres_usuarios = new ArrayList<>();
                for (Usuario usuario:lista_usuarios) {
                    lista_nombres_usuarios.add(usuario.getNombre());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(Form_Creacion_Gasto.this, android.R.layout.simple_spinner_item, lista_nombres_usuarios);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spn_pagador.setAdapter(adapter);
            }


            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                Toast.makeText(Form_Creacion_Gasto.this, "No se ha localizado usuarios", Toast.LENGTH_SHORT).show();
            }
        });
    }
}