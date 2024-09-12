package br.senai.sp.jandira.vivaris.repository

import android.content.Context
import br.senai.sp.jandira.mytrips.model.Usuarios
import br.senai.sp.jandira.vivaris.dao.VivarisDB

class UsuarioRepository(context: Context) {
    private val db = VivarisDB.getBancoDeDados(context).UsuarioDAO()

    fun salvar(usuarios: Usuarios): Long{
        return db.salvar(usuarios)
    }
}