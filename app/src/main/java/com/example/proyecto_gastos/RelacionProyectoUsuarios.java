package com.example.proyecto_gastos;

import com.example.proyecto_gastos.models.RelacionProyectoUsuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RelacionProyectoUsuarios {
    @GET("relacion")
    Call<List<RelacionProyectoUsuario>> obtenerRelaciones();

    @GET("relacion/{proyecto_id}")
    Call<List<RelacionProyectoUsuario>> obtenerRelacionPorProyecto(@Path("proyecto_id") int proyecto_id);

    @POST("relacion")
    Call<RelacionProyectoUsuario>crearUsuario(@Body RelacionProyectoUsuario usuario );

    @DELETE("relacion/{id}")
    Call<Void> borrarRelacion(@Path("id") int id);
}
