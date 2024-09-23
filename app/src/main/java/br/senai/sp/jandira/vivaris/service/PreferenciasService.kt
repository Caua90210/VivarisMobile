package br.senai.sp.jandira.vivaris.service

import br.senai.sp.jandira.vivaris.model.Preferencias
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PreferenciasService {


    @GET("preferencias")
    fun getAllPreferencias(): Call<List<Preferencias>>


    @GET("preferencias/{id}")
    fun getPreferenciasById(@Path("id") id: Int): Call<Preferencias>

}
