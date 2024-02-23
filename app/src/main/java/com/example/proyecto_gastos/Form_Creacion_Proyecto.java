package com.example.proyecto_gastos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.proyecto_gastos.adapters.CustomAdapter_proyecto;
import com.example.proyecto_gastos.models.Proyecto;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Form_Creacion_Proyecto extends AppCompatActivity {
    EditText et_titulo, et_desc;
    Button btn_aceptar, btn_cancelar;
    Spinner spn_moneda;

    Retrofit retrofit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_creacion_proyecto);
        retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.url_domain))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        et_titulo =findViewById(R.id.editTextTitulo);
        et_desc =findViewById(R.id.editTextDescripcion);
        spn_moneda = findViewById(R.id.spn_moneda);
        btn_aceptar = findViewById(R.id.btn_aceptar);
        btn_cancelar = findViewById(R.id.btn_cancelar);

        /**
         * CAMPOS DE TEXTOS
         */

        /**
         * LISTA MONEDAS
         */

        //RECUPERO EL ArrayAdapter con el recurso
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.tipos_monedas,
                android.R.layout.simple_spinner_item
        );

        //INDICO EL FORMATO XML
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //LOS ENLAZO
        spn_moneda.setAdapter(adapter);

        /**
         * BOTONES
         */

        btn_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearProyectoForm();
            }
        });

        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Form_Creacion_Proyecto.this.setVisible(false);
            }
        });

    }
    public void crearProyectoForm(){
        int id = 1;
        /**
         * ME FALTARIA VER EL TEMA DEL ADMINISTRADOR -----------------------------------------
         */
        int administrador =1;
        /**
         * 1 -> EURO, 2 -> DOLAR, 3 -> YEN
         */
        int moneda = spn_moneda.getSelectedItemPosition()+1;
        /**
         * Se inicializa el total de gastos a 0, segun se añadan gastos se le sumará la cantidad
         */
        int total_gastos =0;
        String titulo =et_titulo.getText().toString();
        String descripcion =et_desc.getText().toString();


        Projects proyectoService = retrofit.create(Projects.class);
        Call<Proyecto> llamada = proyectoService.crearProyecto(new Proyecto(id, administrador, moneda, total_gastos, titulo, descripcion));
        llamada.enqueue(new Callback<Proyecto>() {
            @Override
            public void onResponse(Call<Proyecto> call, Response<Proyecto> response) {
                /**
                 * Dialogo que indica al usuario que se ha creado correctamente
                 */
                AlertDialog.Builder builder = new AlertDialog.Builder(Form_Creacion_Proyecto.this);
                builder.setMessage("Proyecto creado con exito")
                        .setTitle("Creación proyecto");
                builder.create().show();
                Intent intent = new Intent(Form_Creacion_Proyecto.this, MainActivity.class);
                startActivity(intent);
            }


            @Override
            public void onFailure(Call<Proyecto> call, Throwable t) {

                String error = "Error";
            }
        });

    }
}