package br.senai.sp.jandira.vivaris.model

data class Sexo(
    val id: Int = 0,
    val sexo: String
)
data class SexoResponse(
    val data: List<Sexo>, // A lista de sexos recebida da API
    val status_code: Int,
    val quantidade: Int
)

