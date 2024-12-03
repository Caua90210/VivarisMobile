package br.senai.sp.jandira.vivaris.service

import br.senai.sp.jandira.vivaris.model.AgendamentoRequest
import br.senai.sp.jandira.vivaris.model.consultaResponse
import br.senai.sp.jandira.vivaris.model.session
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface ConsultaService {
        @Headers("Content-Type: application/json")
        @POST("consulta")
        fun agendarConsulta(@Body request: AgendamentoRequest, @Header("x-access-token") authorization: String): Call<consultaResponse>

        @POST("create-checkout-session")
        fun criarSession(@Header("x-access-token") authorization: String, @Body request: session): Call<session>
}