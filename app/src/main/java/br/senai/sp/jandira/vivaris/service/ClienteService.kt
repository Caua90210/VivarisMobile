package br.senai.sp.jandira.vivaris.service

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

interface ClienteService {


    @GET("cliente")
    fun getAllClientes(): Call<Result>

    @GET("cliente/{id}")
    fun getClienteById( @Path("id") id: Int): Call<Cliente>

    @Headers("Content-Type: application/json")
    @POST("login/usuario")
    fun loginUsuario(@Body loginUsuario: LoginUsuario): Call<LoginResponse>

    @Headers("Content-Type: application/json")
    @POST("cliente")
    fun cadastrarCliente(@Body cliente: Cliente): Call<ClienteResponse>

}