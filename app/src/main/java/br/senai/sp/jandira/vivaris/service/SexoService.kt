package br.senai.sp.jandira.vivaris.service

import br.senai.sp.jandira.vivaris.model.Sexo
import br.senai.sp.jandira.vivaris.model.SexoResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface SexoService {
    @GET("sexo")
    fun getSexo(): Call<SexoResponse>

    @GET("usuario/sexo/{id}")
    fun getSexoByID(@Path("id") id: Int): Call<SexoResponse>
}
