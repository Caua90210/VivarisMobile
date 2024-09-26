package br.senai.sp.jandira.vivaris.model

data class Preferencias(
    val id: Int = 0,
    val nome: String,
    val cor: String
)

data class PreferenciasResponse(
    val data: List<Preferencias>,
    val status_code: Int
)
