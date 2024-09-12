package br.senai.sp.jandira.mytrips.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_usuarios")
data class Usuarios(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var nome: String = "",
    var phone: String = "",
    var email: String = "",
    var dataNascimento: String = "",
    var password: String = "",
    var sexo: String = "",
)