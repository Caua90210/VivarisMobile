package br.senai.sp.jandira.vivaris.service

import br.senai.sp.jandira.vivaris.model.CartaoResponse
import br.senai.sp.jandira.vivaris.model.Cartoes
import br.senai.sp.jandira.vivaris.model.Cliente
import br.senai.sp.jandira.vivaris.model.ClienteResponse
import br.senai.sp.jandira.vivaris.model.LoginResponse
import br.senai.sp.jandira.vivaris.model.LoginUsuario
import br.senai.sp.jandira.vivaris.model.Result
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface CartoesService {

    @GET("cartao")
    fun getAllCartoes(): Call<Result>

    @GET("cartao/{id}")
    fun getCartaoById( @Path("id") id: Int): Call<Cartoes>


    @Headers("Content-Type: application/json")
    @POST("cartao")
    fun cadastrarCartao(@Body cartoes: Cartoes): Call<CartaoResponse>
}