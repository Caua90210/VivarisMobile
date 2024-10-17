package br.senai.sp.jandira.vivaris.service

import br.senai.sp.jandira.vivaris.model.DataResponse
import br.senai.sp.jandira.vivaris.model.LoginPsicologo
import br.senai.sp.jandira.vivaris.model.Psicologo
import br.senai.sp.jandira.vivaris.model.PsicologoResponse
import br.senai.sp.jandira.vivaris.model.Result
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface PsicologoService {


    @GET("psicologo")
    fun getAllPsicologos(): Call<Result>

    @GET("psicologo/{id}")
    fun getPsicologById( @Path("id") id: Int): Call<Psicologo>

    @Headers("Content-Type: application/json")
    @POST("profissional/login")
    fun psicologoLogin(@Body loginPsicologo: LoginPsicologo): Call<DataResponse>

    @Headers("Content-Type: application/json")
    @POST("psicologo")
    fun cadastrarPsicologo(@Body psicologo: Psicologo): Call<Psicologo>
}