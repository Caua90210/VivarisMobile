package br.senai.sp.jandira.vivaris.model

import android.media.Image
import java.util.Date

data class Psicologo(
    val id: Int = 0,
    val nome: String,
    val data_nascimento: Date,
    val crp: String,
    val cpf: String,
    val email: String,
    val senha: String,
    val telefone: String,
    val foto_perfil: Image,
    val descricao: String,
    val link_instagram: String,
    val sexo: Sexo? =null
    )

