package br.senai.sp.jandira.vivaris.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.senai.sp.jandira.mytrips.model.Usuarios

@Dao
interface UsuarioDAO {


    @Insert
    fun salvar(usuarios: Usuarios): Long

    @Update
    fun atualizar(usuarios: Usuarios): Int

    @Delete
    fun excluir(usuarios: Usuarios)

    @Query("SELECT * FROM tbl_usuarios ORDER BY nome ASC")
    fun listarTodosOsUsuarios(): List<Usuarios>


}