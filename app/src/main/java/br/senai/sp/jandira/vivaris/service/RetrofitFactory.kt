package br.senai.sp.jandira.vivaris.service

import br.senai.sp.jandira.vivaris.model.Cliente
import br.senai.sp.jandira.vivaris.model.Sexo
import okhttp3.OkHttpClient
import okhttp3.Interceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitFactory {

    private val BASE_URL = "http://10.107.144.8:8080/v1/vivaris/"

    // Configurando o OkHttpClient com timeout personalizado e interceptador para cabeçalho "Connection: close"
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)  // Tempo limite para conexão
        .writeTimeout(30, TimeUnit.SECONDS)    // Tempo limite para escrita de dados
        .readTimeout(30, TimeUnit.SECONDS)     // Tempo limite para leitura de dados
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .header("Connection", "close")
                .header("Content-Type", "application/json")  /// Fecha a conexão após a requisição
                .build()
            chain.proceed(request)
        }
        .build()

    // Criando a instância do Retrofit com o OkHttpClient personalizado
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
