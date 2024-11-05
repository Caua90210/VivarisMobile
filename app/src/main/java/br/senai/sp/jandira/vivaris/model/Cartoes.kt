package br.senai.sp.jandira.vivaris.model

import java.util.Date

data class Cartoes(
    val id: Int,
    val modalidade: String,
    val numero_cartao: String,
    val nome: String,
    val validade: String,
    val cvc: String
)

data class CartaoResponse(
    val card: Cartoes,
    val status_code: Int
)