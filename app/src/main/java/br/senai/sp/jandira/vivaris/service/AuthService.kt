package br.senai.sp.jandira.vivaris.service

import br.senai.sp.jandira.vivaris.model.LoginResponse
import br.senai.sp.jandira.vivaris.model.LoginUsuario
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("login")
    fun login(@Body loginUsuario: LoginUsuario): Call<LoginResponse>
}