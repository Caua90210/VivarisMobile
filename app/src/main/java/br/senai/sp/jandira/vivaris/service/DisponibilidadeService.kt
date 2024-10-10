package br.senai.sp.jandira.vivaris.service

import br.senai.sp.jandira.vivaris.model.Disponibilidade
import br.senai.sp.jandira.vivaris.model.DisponibilidadePsicologo
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface DisponibilidadeService {

    @Headers("Content-Type: application/json")
    @POST("disponibilidade")
    fun cadastrarDisponibilidade(@Body disponibilidade: Disponibilidade): Call<Disponibilidade>

    @GET("disponibilidade/psicologo/{id}")
    fun getDisponibilidadeById( @Path("id") id: Int): Call<Disponibilidade>

    @POST("disponibilidade/psicologo/{id}")
    fun postDisponibilidadePsicologo(
        @Path("id") id: Int,
        disponibilidadePsicologo: DisponibilidadePsicologo
    ): Call<DisponibilidadePsicologo>

}