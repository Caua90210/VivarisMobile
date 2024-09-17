package br.senai.sp.jandira.vivaris.model

import android.media.Image


data class Cliente(
    val id: Int = 0,
    val nome: String,
    val telefone: String,
    val email: String,
    val dataNascimento: String,
    val senha: String,
    val sexo: Sexo? = null,
    val link_instagram: String,
    val foto_perfil: Image?,
    val cpf: String
)
