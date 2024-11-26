package br.senai.sp.jandira.vivaris.service

import br.senai.sp.jandira.vivaris.model.DataResponse
import br.senai.sp.jandira.vivaris.model.LoginPsicologo
import br.senai.sp.jandira.vivaris.model.Psicologo
import br.senai.sp.jandira.vivaris.model.PsicologoPesquisa
import br.senai.sp.jandira.vivaris.model.PsicologoResponse
import br.senai.sp.jandira.vivaris.model.PsicologoResponsebyID
import br.senai.sp.jandira.vivaris.model.Result
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface PsicologoService {



    @GET("profissionais")
    fun getAllPsicologos(@Header("x-access-token") authorization: String): Call<PsicologoPesquisa>

    @GET("profissional/{id}")
    fun getPsicologById(
        @Path("id") id: Int,
        @Header("x-access-token") authorization: String
    ): Call<PsicologoResponsebyID>

    @Headers("Content-Type: application/json")
    @POST("profissional/login")
    fun psicologoLogin(@Body loginPsicologo: LoginPsicologo): Call<PsicologoResponse>

    @Headers("Content-Type: application/json")
    @POST("psicologo")
    fun cadastrarPsicologo(@Body psicologo: Psicologo): Call<Psicologo>
}