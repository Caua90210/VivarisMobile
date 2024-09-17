package br.senai.sp.jandira.vivaris.service

import br.senai.sp.jandira.vivaris.model.Cliente
import br.senai.sp.jandira.vivaris.model.Result
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ClienteService {


    @GET("Cliente")
    fun getAllClientes(): Call<Result>

    @GET("character/{id}")
    fun getClienteById( @Path("id") id: Int): Call<Cliente>



}