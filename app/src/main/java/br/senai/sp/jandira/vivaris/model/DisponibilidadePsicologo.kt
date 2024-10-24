package br.senai.sp.jandira.vivaris.model


data class DisponibilidadePsicologo(
    val disponibilidade: Int,
    val id_psicologo: Int,
    val status: String = "Livre"
)




