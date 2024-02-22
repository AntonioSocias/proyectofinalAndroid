package com.example.proyecto_gastos;

import com.example.proyecto_gastos.models.Proyecto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Projects {
    @GET("projects")
    Call<List<Proyecto>> obtenerProyectos();

    @GET("projects/{proyecto_id}")
    Call<Proyecto> obtenerProyecto(@Path("proyecto_id") int proyecto_id);

    @POST("projects")
    Call<Proyecto>crearProyecto(@Body Proyecto proyecto );

}
