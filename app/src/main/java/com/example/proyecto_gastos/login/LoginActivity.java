package com.example.proyecto_gastos.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyecto_gastos.Detalles_Proyectos;
import com.example.proyecto_gastos.MainActivity;
import com.example.proyecto_gastos.Proyectos;
import com.example.proyecto_gastos.R;
import com.example.proyecto_gastos.Usuarios;
import com.example.proyecto_gastos.adapters.CustomAdapter_usuario;
import com.example.proyecto_gastos.models.Usuario;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    EditText txt_nombre,txt_contra;
    TextView txt_aviso;
    Button btn_iniciarSesion, btn_registrarse;
    List<Usuario> lista_usuarios;
    Retrofit retrofit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txt_nombre = findViewById(R.id.editTextNombreUsuario);
        txt_contra = findViewById(R.id.editTextTextPassword);
        txt_aviso = findViewById(R.id.textViewAviso);
        btn_iniciarSesion = findViewById(R.id.login);
        btn_registrarse = findViewById(R.id.register);

        retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.url_domain))//VOLVER A PONER QUE ACCEDA A STRINGS
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        obtenerUsuarios();

        /**
         * COMPROBACIONES DE QUE LA CONTRASEÑA SEA SUPERIOR
         * A 5 CARACTERES Y EL CAMPO DE NOMBRE TENGA ALGUN VALOR
         */
        txt_contra.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 5 && txt_nombre.getText().length()>0) {
                    txt_aviso.setText("");
                    btn_iniciarSesion.setEnabled(true);
                    btn_registrarse.setEnabled(true);
                } else {
                    txt_aviso.setText("La contraseña debe tener más de 5 caracteres");
                    btn_iniciarSesion.setEnabled(false);
                    btn_registrarse.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        btn_iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * SUPONGO QUE SE HA COMPROBADO Y LAS CREDENCIALES COINCIDEN OBTENIENDO EL USUARIO COMPLETO
                 */
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Boolean valido = false;
                for (Usuario usuario: lista_usuarios) {
                    if (usuario.getNombre().equals(txt_nombre.getText().toString())
                            && usuario.getPassword().equals(txt_contra.getText().toString())){
                        valido=true;
                    }
                }
                if (valido){
                    Usuario usuarioPrueba = new Usuario(
                            0,txt_nombre.getText().toString(),
                            txt_contra.getText().toString(), 0);

                    String usuarioString = usuarioPrueba.getNombre();

                    editor.putString("usuario", usuarioString);
                    editor.apply();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    txt_aviso.setText("Credenciales no válidas");
                }
            }
        });

        btn_registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Boolean existe = false;
                for (Usuario usuario: lista_usuarios) {
                    if (usuario.getNombre().equals(txt_nombre.getText().toString())){
                        existe=true;
                    }
                }
                if (!existe){
                    /**
                     * no existe asi que puedo crearlo
                     */
                    Usuario usuarioNuevo = new Usuario(
                            0, txt_nombre.getText().toString(),
                            txt_contra.getText().toString(),0);

                    Usuarios usuariosService = retrofit.create(Usuarios.class);
                    Call<Usuario> llamada = usuariosService.crearUsuario(usuarioNuevo);
                    llamada.enqueue(new Callback<Usuario>() {
                        @Override
                        public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                            Toast.makeText(LoginActivity.this, "Usuario subido a BD", Toast.LENGTH_SHORT).show();
                            String usuarioString = usuarioNuevo.getNombre();

                            editor.putString("usuario", usuarioString);
                            editor.apply();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(Call<Usuario> call, Throwable t) {

                        }
                    });
                }else{
                    /**
                     * existe asi que no puedo crearlo
                     */
                    txt_aviso.setText("El usuario ya existe");
                }
            }
        });

    }

    /**
     * OBTENGO TODOS LOS USUARIOS DEL SISTEMA
     */
    private void obtenerUsuarios(){
        Usuarios usuariosService = retrofit.create(Usuarios.class);
        Call<List<Usuario>> llamada = usuariosService.obtenerUsuarios();
        llamada.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                lista_usuarios = response.body();
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {

                Log.e("API_REQUEST_FAILURE", "Error: " + t.getMessage());
            }
        });
    }
    /**
     * METODO CREAR USUARIO AL REGISTRARSE
     */

}