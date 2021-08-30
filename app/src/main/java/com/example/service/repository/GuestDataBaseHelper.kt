package com.example.service.repository

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.service.constants.DataBaseConstants

class GuestDataBaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    // IDENTIFICA SE EU JÁ TENHO UM BANCO OU NÃO
    // SE EU NÃO TENHO UM BANCO, ELE ME CRIA UM BANCO DE DADOS;
    override fun onCreate(db: SQLiteDatabase) {
        // COMANDO PARA CRIAR A TABELA DE DADOS
        db.execSQL(CREATE_TABLE_GUEST)
    }

    // CASO EU TENHA UMA ATUALIZAÇÃO NO MEU BANCO DE DADOS, E EXISTIREM DADOS
    // NA VERSÃO ANTIGA, ESSA FUNÇÃO SERÁ RODADA, E IRÁ ATUALIZAR OS DADOS ANTIGOS
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    companion object {
        private const val DATABASE_VERSION = 1
        private const val  DATABASE_NAME = "Convidados.db"

        // ISSO AQUI É UMA STRING, COM UM COMANDO PARA QUE O SQLITE INTERPRETE E CRIE UMA TABELA
        private const val CREATE_TABLE_GUEST =
            ("create table " + DataBaseConstants.GUEST.TABLE_NAME + " ("
                    + DataBaseConstants.GUEST.COLUMNS.ID + " integer primary key autoincrement, "
                    + DataBaseConstants.GUEST.COLUMNS.NAME + " text, "
                    + DataBaseConstants.GUEST.COLUMNS.PRESENCE + " integer);")
    }

}