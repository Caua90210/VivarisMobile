package br.senai.sp.jandira.vivaris.service

import br.senai.sp.jandira.vivaris.model.PreferenciaCliente
import br.senai.sp.jandira.vivaris.model.Preferencias
import br.senai.sp.jandira.vivaris.model.PreferenciasResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface PreferenciasService {


    @GET("preferencias")
    fun getAllPreferencias(): Call<PreferenciasResponse>


    @GET("preferencias/{id}")
    fun getPreferenciasById(@Path("id") id: Int): Call<Preferencias>

    @Headers("Content-Type: application/json")
    @POST("cliente/preferencias")
    fun cadastrarPreferenciaCliente(
        @Body preferenciaCliente: PreferenciaCliente// Este é o corpo da requisição
    ): Call<PreferenciasResponse>


}
