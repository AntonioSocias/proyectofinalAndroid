package com.example.proyecto_gastos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

import com.example.proyecto_gastos.adapters.CustomAdapter_gasto;
import com.example.proyecto_gastos.adapters.CustomAdapter_proyecto;
import com.example.proyecto_gastos.adapters.CustomAdapter_usuario;
import com.example.proyecto_gastos.models.Gasto;
import com.example.proyecto_gastos.models.Proyecto;
import com.example.proyecto_gastos.models.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Detalles_Proyectos extends AppCompatActivity {
    TextView txTitulo, txtGastoTotal, txtAdministrador;
    ListView lv_gastos, lv_usuarios;
    String administrador;
    Float gastoTotal;
    Proyecto proyecto;
    FloatingActionButton btn_crearGasto, btn_crearParticipante;
    Retrofit retrofit;
    public static List<Gasto> lista_gastos_proyecto;
    static List<Usuario> lista_usuarios_proyecto, lista_usuarios;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_proyectos);
        proyecto = (Proyecto) getIntent().getSerializableExtra("proyecto");
        retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.url_domain))//VOLVER A PONER QUE ACCEDA A STRINGS
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        txTitulo = findViewById(R.id.textView2);
        txtGastoTotal = findViewById(R.id.textViewGastoTotal);
        txtAdministrador = findViewById(R.id.textViewAdministrador);
        lv_gastos = findViewById(R.id.lv_listado_gastos);
        btn_crearGasto = findViewById(R.id.btn_crearGasto);
        btn_crearParticipante = findViewById(R.id.btn_crearParticipante);

        txTitulo.setText(proyecto.getTitulo());
        gastoTotal= proyecto.getTotal_gastos();

        crearListViewGastos(proyecto.getId());
        crearListViewUsuarios(proyecto.getId());
        obtenerUsuarios();

        /**
         * BOTON PARA CREAR UN NUEVO GASTO
         */
        btn_crearGasto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Detalles_Proyectos.this, Form_Creacion_Gasto.class);
                intent.putExtra("proyecto", proyecto);
                startActivityForResult(intent, 1);
            }
        });
        btn_crearParticipante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoAñadirUsuario();
            }
        });
    }

    /**
     * OBTENGO LOS GASTOS ASOCIADOS AL PROYECTO AL DEL CUAL PASO EL ID COMO PARÁMETRO
     * @param id_proyecto
     */
    private void crearListViewGastos(int id_proyecto){
        Proyectos proyectoService = retrofit.create(Proyectos.class);
        Call<List<Gasto>> llamada = proyectoService.obtenerGastosProyecto(id_proyecto);
        llamada.enqueue(new Callback<List<Gasto>>() {
            @Override
            public void onResponse(Call<List<Gasto>> call, Response<List<Gasto>> response) {
                lista_gastos_proyecto = response.body();
                lv_gastos = findViewById(R.id.lv_listado_gastos);
                //COMENTO CUSTOR ADAPTER
                lv_gastos.setAdapter(new CustomAdapter_gasto(Detalles_Proyectos.this, lista_gastos_proyecto));

                /** YA LO HE ASIGNADO EL VALOR DE ORIGEN, SOLO QUEDA MOFICAR
                for (Gasto gasto: lista_gastos_proyecto) {
                    gastoTotal += gasto.getCantidad();
                }
                 */
                /**
                 * MUESTRO EL VALOR A MANO
                 */
                String gastoTotalStr = String.format("%.2f", gastoTotal);
                txtGastoTotal.setText("Total: " + gastoTotalStr + getResources().getString(R.string.moneda));

                // Resto los gastos de cada participante y muestro el déficit

                lv_gastos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        Intent intent = new Intent(Detalles_Proyectos.this, Form_Creacion_Gasto.class);
                        Gasto gasto = (Gasto) lv_gastos.getAdapter().getItem(position);
                        intent.putExtra("gasto", gasto);
                        startActivityForResult(intent, 1);
                    }
                });
                lv_gastos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Detalles_Proyectos.this);
                        builder.setMessage("¿Quiere eliminar el gasto " + lista_gastos_proyecto.get(i).getTitulo() + "?")
                                .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Gastos proyectoService = retrofit.create(Gastos.class);
                                        Call<Void> llamada = proyectoService.borrarGasto(lista_gastos_proyecto.get(i).getId());
                                        llamada.enqueue(new Callback<Void>() {
                                            @Override
                                            public void onResponse(Call<Void> call, Response<Void> response) {
                                                lista_gastos_proyecto.remove(i);
                                                actualizarGastoTotal();
                                                ((CustomAdapter_gasto) lv_gastos.getAdapter()).notifyDataSetChanged();
                                                ((CustomAdapter_usuario) lv_usuarios.getAdapter()).notifyDataSetChanged();
                                            }

                                            @Override
                                            public void onFailure(Call<Void> call, Throwable t) {
                                            }
                                        });
                                    }
                                })
                                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                });
                        builder.create().show();
                        return true;
                    }
                });
            }

            @Override
            public void onFailure(Call<List<Gasto>> call, Throwable t) {
                Log.e("API_REQUEST_FAILURE", "Error: " + t.getMessage());
            }
        });
    }

    private void crearListViewUsuarios(int id_proyecto){
        Proyectos proyectoService = retrofit.create(Proyectos.class);
        Call<List<Usuario>> llamada = proyectoService.obtenerUsuariosProyecto(id_proyecto);
        llamada.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                lista_usuarios_proyecto = response.body();
                lv_usuarios = findViewById(R.id.lv_listado_participantes);
                //COMENTO CUSTOR ADAPTER
                lv_usuarios.setAdapter(new CustomAdapter_usuario(Detalles_Proyectos.this, lista_usuarios_proyecto));

                /**
                 * ELIMINAR USUARIO
                 */
                lv_usuarios.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Detalles_Proyectos.this);
                        builder.setMessage("¿Quiere eliminar el usuario " + lista_usuarios_proyecto.get(i).getNombre() + "?")
                                .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        /**
                                         * CÓDIGO DE ELIMINAR EL ELEMENTO
                                         */
                                        Usuarios proyectoService = retrofit.create(Usuarios.class);
                                        Call<Void> llamada = proyectoService.borrarUsuario(lista_usuarios_proyecto.get(i).getId());
                                        llamada.enqueue(new Callback<Void>() {
                                            @Override
                                            public void onResponse(Call<Void> call, Response<Void> response) {
                                                Toast.makeText(Detalles_Proyectos.this, "Usuario borrado de la base de datos", Toast.LENGTH_SHORT).show();
                                                lista_usuarios_proyecto.remove(i);
                                                /**
                                                 * Indico al adapter del listview de gastos que he alterado
                                                 * la lista y debe refrescarse
                                                 */
                                                ((CustomAdapter_usuario) lv_usuarios.getAdapter()).notifyDataSetChanged();

                                            }

                                            @Override
                                            public void onFailure(Call<Void> call, Throwable t) {
                                                Toast.makeText(Detalles_Proyectos.this, "No se ha podido borrar de la base de datos", Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<List<Usuario>> call, Throwable t) {

                Log.e("API_REQUEST_FAILURE", "Error: " + t.getMessage());
            }
        });
    }
    private void mostrarDialogoAñadirUsuario() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecciona usuarios");

        List<String> nombresUsuariosDisponibles = new ArrayList<>();
        for (Usuario usuario : lista_usuarios) {
            boolean usuarioEnProyecto = false;
            for (Usuario usuarioProyecto : lista_usuarios_proyecto) {
                if (usuario.getNombre().equals(usuarioProyecto.getNombre())) {
                    usuarioEnProyecto = true;
                    break;
                }
            }
            if (!usuarioEnProyecto) {
                nombresUsuariosDisponibles.add(usuario.getNombre());
            }
        }

        // Convertir lista de nombres de usuario disponibles a arreglo
        final String[] elementos = nombresUsuariosDisponibles.toArray(new String[0]);
        final boolean[] seleccionados = new boolean[elementos.length];

        builder.setMultiChoiceItems(elementos, seleccionados, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                // Actualiza el estado de selección del elemento en el arreglo
                seleccionados[which] = isChecked;
            }
        });

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Procesa los elementos seleccionados
                for (int i = 0; i < elementos.length; i++) {
                    if (seleccionados[i]) {
                        for (Usuario usuario:lista_usuarios) {
                            if (usuario.getNombre().equals(elementos[i])){
                                lista_usuarios_proyecto.add(usuario);
                            }
                        }
                        crearListViewUsuarios(proyecto.getId());
                        ((CustomAdapter_usuario) lv_usuarios.getAdapter()).notifyDataSetChanged();
                    }
                }
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // Cierra el diálogo sin hacer nada
            }
        });

        // Crea y muestra el diálogo
        AlertDialog dialog = builder.create();
        dialog.show();

    }
    private void obtenerUsuarios(){
        Usuarios usuariosService = retrofit.create(Usuarios.class);
        Call<List<Usuario>> llamada = usuariosService.obtenerUsuarios();
        llamada.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                lista_usuarios = response.body();
                /**
                 * ASIGNACION DE ADMINISTRADOR
                 */
                for (Usuario usuario : lista_usuarios) {
                    if (usuario.getId() == proyecto.getId()){
                        administrador = usuario.getNombre();
                    }
                }
                txtAdministrador.setText("Administrador: " + administrador);
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {

                Log.e("API_REQUEST_FAILURE", "Error: " + t.getMessage());
            }
        });
    }

    /**
     * ESPERAR RESPUESTA DE ACTIVITYFORRESULT
     */
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // Aquí puedes realizar cualquier acción adicional después de que se complete la segunda actividad
                Toast.makeText(this, "Gasto creado correctamente", Toast.LENGTH_SHORT).show();
                lista_gastos_proyecto.add((Gasto)data.getSerializableExtra("gastoCreado"));
                ((CustomAdapter_gasto) lv_gastos.getAdapter()).notifyDataSetChanged();
                actualizarGastoTotal();
                ((CustomAdapter_usuario) lv_usuarios.getAdapter()).notifyDataSetChanged();
            }
        }
    }
    public void actualizarGastoTotal(){
        /**
         * CALCULO EL GASTO TOTAL, LUEGO SE LO TENGO QUE RESTAR AL GASTO DE CADA PARTICIPANTE
         * Y MOSTRAR EL DEFICIT DE CADA UNO
         */
        Float acum=0f;
        for (Gasto gasto : lista_gastos_proyecto) {
            acum += gasto.getCantidad();
        }
        String gastoTotalStr = String.format("%.2f", acum);

        txtGastoTotal.setText("Total: " + gastoTotalStr + getResources().getString(R.string.moneda));

        /**
         * MODIFICAR PROYECTO BD
         */
        proyecto.setTotal_gastos(acum);

        Proyectos proyectoService = retrofit.create(Proyectos.class);
        Call<Proyecto> call = proyectoService.actualizarProyecto(proyecto.getId(), proyecto);
        call.enqueue(new Callback<Proyecto>() {
            @Override
            public void onResponse(Call<Proyecto> call, Response<Proyecto> response) {
                Toast.makeText(Detalles_Proyectos.this, "Se ha actualizado la BD", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<Proyecto> call, Throwable t) {
                Toast.makeText(Detalles_Proyectos.this, "No se ha actualizado en la BD", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent();
        intent.putExtra("proyectoModificado", proyecto);
        setResult(Detalles_Proyectos.RESULT_OK, intent);
        finish();
        super.onBackPressed();
    }
}