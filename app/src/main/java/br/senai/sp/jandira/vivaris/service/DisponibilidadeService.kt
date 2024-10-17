package br.senai.sp.jandira.vivaris.service

import br.senai.sp.jandira.vivaris.model.Disponibilidade
import br.senai.sp.jandira.vivaris.model.DisponibilidadePsicologo
import br.senai.sp.jandira.vivaris.model.DisponibilidadeResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface DisponibilidadeService {

    @Headers("Content-Type: application/json")
    @POST("disponibilidade")
    fun cadastrarDisponibilidade(@Body disponibilidade: Disponibilidade): Call<DisponibilidadeResponse>

    @GET("disponibilidade/psicologo/{id}")
    fun getDisponibilidadeById( @Path("id") id: Int): Call<Disponibilidade>

    @POST("disponibilidade/psicologo/{id}")
    fun postDisponibilidadePsicologo(
        @Path("id") idPsicologo: Int,
        @Body disponibilidadePsicologo: DisponibilidadePsicologo
    ): Call<DisponibilidadePsicologo>

    @DELETE("disponibilidade/psicologo/{id}")
    fun deleteDisponibilidade(
        @Path("id") idPsicologo: Int,
        @Body 
    )


}