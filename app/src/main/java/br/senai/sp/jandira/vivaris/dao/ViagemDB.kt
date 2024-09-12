package br.senai.sp.jandira.vivaris.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.senai.sp.jandira.mytrips.model.Usuarios



@Database(entities = [Usuarios::class], version = 1)
abstract class VivarisDB : RoomDatabase() {

    abstract fun UsuarioDAO(): UsuarioDAO

    companion object{
        private lateinit var instancia: VivarisDB

        fun getBancoDeDados(context: Context): VivarisDB{
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