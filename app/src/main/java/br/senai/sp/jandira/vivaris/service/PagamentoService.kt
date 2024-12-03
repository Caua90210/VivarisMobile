package br.senai.sp.jandira.vivaris.service

import retrofit2.http.GET
import retrofit2.http.Path

interface PagamentoService {
    @GET("payment-status/{sessionId}")
    suspend fun getPaymentStatus(@Path("sessionId") sessionId: String): PaymentStatusResponse
}

data class PaymentStatusResponse(val status: String)

