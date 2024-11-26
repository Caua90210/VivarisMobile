package br.senai.sp.jandira.vivaris.service

import android.content.Context
import br.senai.sp.jandira.vivaris.model.Cliente
import br.senai.sp.jandira.vivaris.model.Sexo
import br.senai.sp.jandira.vivaris.security.TokenManager
import okhttp3.OkHttpClient
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitFactory(private val context: Context) {

    private val BASE_URL = "http://10.107.144.30:8080/v1/vivaris/"

    // Interceptor para adicionar o token JWT ao cabeçalho das requisições
    private val authInterceptor = Interceptor { chain ->
        val token = TokenManager(context).getToken()

        val requestBuilder = chain.request().newBuilder()
        token?.let {
            requestBuilder.addHeader("x-access-token", it)
        }

        chain.proceed(requestBuilder.build())
    }

    // Configurando o OkHttpClient
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(authInterceptor)
        .build()

    // Criando a instância do Retrofit
    private val retrofitFactory = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getClienteService(): ClienteService {
        return retrofitFactory.create(ClienteService::class.java)
    }

    fun getPreferenciasService(): PreferenciasService {
        return retrofitFactory.create(PreferenciasService::class.java)
    }

    fun getSexoService(): SexoService {
        return retrofitFactory.create(SexoService::class.java)
    }

    fun getPsicologoService(): PsicologoService {
        return retrofitFactory.create(PsicologoService::class.java)
    }

    fun getDisponibilidadeService(): DisponibilidadeService {
        return retrofitFactory.create(DisponibilidadeService::class.java)
    }

    fun getCartoesService(): CartoesService {
        return retrofitFactory.create(CartoesService::class.java)
    }
}