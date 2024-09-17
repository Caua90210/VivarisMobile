
package br.senai.sp.jandira.vivaris.dao

import UsuarioDAO
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.senai.sp.jandira.vivaris.model.Usuario


@Database(entities = [Usuario::class], version = 2)
abstract class VivarisDB : RoomDatabase() {

    abstract fun UsuarioDAO(): UsuarioDAO

    companion object {
        private lateinit var instancia: VivarisDB

        fun getBancoDeDados(context: Context): VivarisDB {
            instancia = Room
                .databaseBuilder(
                    context,
                    VivarisDB::class.java,
                    "db_vivaris"
                )
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()

            return instancia
        }
    }
}