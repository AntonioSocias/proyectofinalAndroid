package com.example.proyecto_gastos;

import com.example.proyecto_gastos.models.Gasto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Gastos {
    @GET("gastos")
    Call<List<Gasto>> obtenerGasto();

    @GET("gastos/{gasto_id}")
    Call<Gasto> obtenerGasto(@Path("gasto_id") int gasto_id);

    @POST("gastos")
    Call<Gasto>crearGasto(@Body Gasto gasto );

    @DELETE("gastos/{gasto_id}")
    Call<Void> borrarGasto(@Path("gasto_id") int gasto_id);
}
