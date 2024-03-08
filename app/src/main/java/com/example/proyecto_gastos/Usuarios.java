package com.example.proyecto_gastos;

import com.example.proyecto_gastos.models.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Usuarios {
    @GET("usuarios")
    Call<List<Usuario>> obtenerUsuarios();

    @GET("usuarios/{usuario_id}")
    Call<Usuario> obtenerUsuario(@Path("usuario_id") int usuario_id);

    @POST("usuarios")
    Call<Usuario>crearUsuario(@Body Usuario usuario );

    @DELETE("usuarios/{usuario_id}")
    Call<Void> borrarUsuario(@Path("usuario_id") int usuario_id);
}
