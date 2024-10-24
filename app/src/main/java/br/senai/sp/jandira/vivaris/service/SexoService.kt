package br.senai.sp.jandira.vivaris.service

import br.senai.sp.jandira.vivaris.model.Sexo
import br.senai.sp.jandira.vivaris.model.SexoResponse
import retrofit2.Call
import retrofit2.http.GET

interface SexoService {
    @GET("sexo")
    fun getSexo(): Call<SexoResponse>
}
