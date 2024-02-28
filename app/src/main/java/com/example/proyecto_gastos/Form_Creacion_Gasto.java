package com.example.proyecto_gastos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.proyecto_gastos.models.Gasto;
import com.example.proyecto_gastos.models.Proyecto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_creacion_gasto);

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
                     * CONVERSION FECHA
                     */
                    String fechaString = et_fecha.getText().toString();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date fechaDate = null;

                    fechaDate = dateFormat.parse(fechaString);

                    /**
                     * MODIFICAR PAGADOR SPINNER
                     */
                    String pagador = et_concepto.getText().toString();

                    Gasto gasto = new Gasto(1, concepto, pagador, cantidadFloat, fechaDate);
                    crearGasto(gasto);

                }catch (ParseException e){
                    e.printStackTrace();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void crearGasto(Gasto gasto){
        Gastos gastosService = retrofit.create(Gastos.class);
        Call<Gasto> llamada = gastosService.crearGasto(gasto);
        llamada.enqueue(new Callback<Gasto>() {
            @Override
            public void onResponse(Call<Gasto> call, Response<Gasto> response) {
                /**
                 * Dialogo que indica al usuario que se ha creado correctamente
                 */
                Toast.makeText(Form_Creacion_Gasto.this, "Proyecto creado con exito", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Form_Creacion_Gasto.this, MainActivity.class);
                startActivity(intent);
            }


            @Override
            public void onFailure(Call<Gasto> call, Throwable t) {

            }
        });

    }
}