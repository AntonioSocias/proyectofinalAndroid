package com.example.proyecto_gastos;

import com.example.proyecto_gastos.models.Gasto;
import com.example.proyecto_gastos.models.Proyecto;
import com.example.proyecto_gastos.models.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Proyectos {
    @GET("proyectos")
    Call<List<Proyecto>> obtenerProyectos();
    @GET("proyectos/{proyecto_id}/gastos")
    Call<List<Gasto>> obtenerGastosProyecto(@Path("proyecto_id") int proyecto_id);
    @GET("proyectos/{proyecto_id}/usuarios")
    Call<List<Usuario>> obtenerUsuariosProyecto(@Path("proyecto_id") int proyecto_id);

    @GET("proyectos/{proyecto_id}")
    Call<Proyecto> obtenerProyecto(@Path("proyecto_id") int proyecto_id);

    @POST("proyectos")
    Call<Proyecto>crearProyecto(@Body Proyecto proyecto );

    @DELETE("proyectos/{proyecto_id}")
    Call<Void> borrarProyecto(@Path("proyecto_id") int proyecto_id);

}
