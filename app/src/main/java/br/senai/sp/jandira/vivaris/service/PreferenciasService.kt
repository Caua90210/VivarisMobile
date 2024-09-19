package br.senai.sp.jandira.vivaris.service
import br.senai.sp.jandira.vivaris.model.Cliente
import br.senai.sp.jandira.vivaris.model.Result
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path



    interface PreferenciasService {
    @GET("preferencias")
    fun getAllPreferencias(): Call<Result>
    @GET("preferencias/{id}")
    fun getPreferenciasById( @Path("id") id: Int): Call<Cliente>


}






