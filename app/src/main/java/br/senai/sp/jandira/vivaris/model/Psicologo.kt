package br.senai.sp.jandira.vivaris.model

import android.media.Image

data class Psicologo(
    val id: Int = 0,
    val nome: String,
    val data_nascimento: String,
    val cip: String,
    val cpf: String,
    val email: String,
    val senha: String,
    val telefone: String,
    val foto_perfil: String?,
    val descricao: String,
    val link_instagram: String,
    val id_sexo: Int
    )

data class PsicologoResponse(
    val data: Psicologo,
    val status_code: Int
)


