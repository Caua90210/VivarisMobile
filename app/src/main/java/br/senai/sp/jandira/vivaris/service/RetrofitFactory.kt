package br.senai.sp.jandira.vivaris.service

import br.senai.sp.jandira.vivaris.model.Cliente
import br.senai.sp.jandira.vivaris.model.Sexo
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitFactory {

    private val BASE_URL = "http://10.107.144.7:8080/v1/vivaris/"



    private val retrofitFactory = Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getClienteService(): ClienteService {
        return retrofitFactory.create(ClienteService::class.java)
    }

    fun getPreferenciasService(): PreferenciasService{
        return retrofitFactory.create(PreferenciasService::class.java)
    }

    fun getSexoService(): SexoService{
        return retrofitFactory.create(SexoService::class.java)
    }

    fun getPsicologoService(): PsicologoService {
        return retrofitFactory.create((PsicologoService::class.java))
    }

    fun getDisponibilidadeService(): DisponibilidadeService{
        return retrofitFactory.create((DisponibilidadeService::class.java))
    }


}