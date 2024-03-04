package com.example.proyecto_gastos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.proyecto_gastos.models.Proyecto;
import com.example.proyecto_gastos.models.Usuario;

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
    String usuarioString;
    static Usuario administrador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_creacion_proyecto);

        usuarioString = getIntent().getStringExtra("usuario");
        retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.url_domain))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        et_titulo =findViewById(R.id.editTextTitulo);
        et_desc =findViewById(R.id.editTextDescripcion);
        spn_moneda = findViewById(R.id.spn_moneda);
        btn_aceptar = findViewById(R.id.btn_aceptar);
        btn_cancelar = findViewById(R.id.btn_cancelar);

        administrador = recuperarAdministradorProyecto(usuarioString);
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
                crearProyecto();
            }
        });

        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Form_Creacion_Proyecto.this.setVisible(false);
            }
        });

    }
    public void crearProyecto(){
        int id = 1;
        /**
         * ME FALTARIA VER EL TEMA DEL ADMINISTRADOR -----------------------------------------
         */
        int administrador = Form_Creacion_Proyecto.administrador.getId();
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


        Proyectos proyectoService = retrofit.create(Proyectos.class);
        Call<Proyecto> llamada = proyectoService.crearProyecto(new Proyecto(id, administrador, moneda, total_gastos, titulo, descripcion));
        llamada.enqueue(new Callback<Proyecto>() {
            @Override
            public void onResponse(Call<Proyecto> call, Response<Proyecto> response) {
                /**
                 * Dialogo que indica al usuario que se ha creado correctamente
                 */
                Toast.makeText(Form_Creacion_Proyecto.this, "Proyecto creado con exito", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Form_Creacion_Proyecto.this, MainActivity.class);
                startActivity(intent);
            }


            @Override
            public void onFailure(Call<Proyecto> call, Throwable t) {
                Toast.makeText(Form_Creacion_Proyecto.this, "El proyecto no se ha creado", Toast.LENGTH_SHORT).show();

            }
        });

    }
    private Usuario recuperarAdministradorProyecto(String usuarioString){
        Usuarios usuarioService = retrofit.create(Usuarios.class);
        Call<List<Usuario>> llamada = usuarioService.obtenerUsuarios();
        final Usuario[] usuarioAdministrador = new Usuario[1];
        llamada.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                List<Usuario> lista_usuarios = response.body();
                for (Usuario usuario: lista_usuarios) {
                    if (usuario.getNombre().equals(usuarioString)){
                        usuarioAdministrador[0] = usuario;
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {

            }
        });
        return usuarioAdministrador[0];
    }
}