package com.example.jasson.bibliotecasapp.datosAppi;


import com.example.jasson.bibliotecasapp.models.Biblioteca;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface SitiosService {

    @GET("in3j-awgi.json")
    Call<List<Biblioteca>> obtenerlistabibliotecas();//obtenerlistadesitios


}
