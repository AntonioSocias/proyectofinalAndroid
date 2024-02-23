package com.example.proyecto_gastos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.proyecto_gastos.models.Proyecto;
import com.example.proyecto_gastos.adapters.CustomAdapter_proyecto;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    Intent intent;
    ListView lv_proyectos;
    FloatingActionButton btn_crearProyecto;
    static List<Proyecto> lista_proyectos;
    Retrofit retrofit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv_proyectos = findViewById(R.id.lv_listado_proyectos);
        btn_crearProyecto = findViewById(R.id.btn_crearProyecto);
        /**
         * GENERO EL RETROFIT
         */
        retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.url_domain))//VOLVER A PONER QUE ACCEDA A STRINGS
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        /**
         * LLAMO A MÉTODO PARA RELLENAR EL LISTVIEW
         */
        crearListView();

        /**
         * FLOATIN BUTTON
         */
        btn_crearProyecto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * ENVIAR A ACTIVITY CON FORMULARIO DE CREACIÓN
                 */
                Intent intent = new Intent(MainActivity.this, Form_Creacion_Proyecto.class);
                startActivity(intent);
            }
        });
    }
    public void crearListView(){
        Projects proyectoService = retrofit.create(Projects.class);
        Call<List<Proyecto>> llamada = proyectoService.obtenerProyectos();
        llamada.enqueue(new Callback<List<Proyecto>>() {
            @Override
            public void onResponse(Call<List<Proyecto>> call, Response<List<Proyecto>> response) {
                lista_proyectos = response.body();
                lv_proyectos = findViewById(R.id.lv_listado_proyectos);
                //COMENTO CUSTOR ADAPTER
                lv_proyectos.setAdapter(new CustomAdapter_proyecto(MainActivity.this, lista_proyectos));//ADAPTER CON LA LISTA DE USUARIOS
                /**
                 * ACCIÓN DE LOS PROYECTOS
                 */
                lv_proyectos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        /** CÓDIGO A DEPURAR Y GENERAR ACCIÓN DE ELEMENTOS
                         * */
                        Intent intent = new Intent(MainActivity.this, Detalles_Proyectos.class);
                        //CREO UN CLIENTE CON EL ITEM DEL ADAPTER EN LA POSICIÓN
                        Proyecto proyecto = (Proyecto) lv_proyectos.getAdapter().getItem(position);
                        //PASO EL OBJETO
                        intent.putExtra("proyecto", proyecto);
                        //PRUEBA DNIADWI
                        startActivity(intent);
                    }
                });
                /**
                 * ELIMINAR PROYECTO
                 */
                lv_proyectos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("¿Quiere eliminar el proyecto " + lista_proyectos.get(i).getTitulo() + "?")
                                .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        /**
                                         * CÓDIGO DE ELIMINAR EL ELEMENTO
                                         */
                                        Call<Void> llamada = proyectoService.borrarProyecto(lista_proyectos.get(i).getId());
                                        llamada.enqueue(new Callback<Void>() {
                                            @Override
                                            public void onResponse(Call<Void> call, Response<Void> response) {
                                                Toast.makeText(MainActivity.this, "Proyecto borrado de la base de datos", Toast.LENGTH_SHORT).show();
                                                lista_proyectos.remove(i);
                                                /**
                                                 * Indico al adapter del listview de proyectos que he alterado
                                                 * la lista y debe refrescarse
                                                 */
                                                ((CustomAdapter_proyecto) lv_proyectos.getAdapter()).notifyDataSetChanged();

                                            }

                                            @Override
                                            public void onFailure(Call<Void> call, Throwable t) {
                                                Toast.makeText(MainActivity.this, "No se ha podido borrar de la base de datos", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }
                                })
                                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // Cancela la eliminación
                                        dialog.dismiss();
                                    }
                                });
                        // Muestra el diálogo
                        builder.create().show();
                        return true;
                    }
                });
            }

            @Override
            public void onFailure(Call<List<Proyecto>> call, Throwable t) {

                Log.e("API_REQUEST_FAILURE", "Error: " + t.getMessage());
            }
        });

    }

}