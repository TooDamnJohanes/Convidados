package com.example.service.repository

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.provider.ContactsContract
import com.example.service.constants.DataBaseConstants
import com.example.service.model.GuestModel
import java.lang.Exception
import java.util.ArrayList

class GuestRepository private constructor(context: Context) {

    // CRIO UMA INSTANCIA DO MEU BANCO DE DADOS, COM UM CONTEXTO, MAS COMO EU PEGO ESSE CONTEXTO
    // SE MEU CONSTRUTOR É PRIVADO?
    private var mGuestDataBaseHelper: GuestDataBaseHelper = GuestDataBaseHelper(context)

    // EU CRIO UM OBJETO, E PASSO O VALOR DO CONTEXTO PARA ESSE OBJETO, QUE PASSARÁ ISSO PARA
    // MEU BANCO DE DADOS
    companion object {
        // PARA EU GERENCIAR DE FATO, AS INSTANCIAS DE UMA CLASSE
        //ESSA VARIAVEL VAI GUARDAR A INSTANCIA DA CLASSE
        private lateinit var repository: GuestRepository
        // METODO ESTATICO, E SOMENTE ELE É RESPONSÁVEL POR ME DAR UMA INSTANCIA DA CLASSE
        fun getInstance(context: Context): GuestRepository {
            if(!::repository.isInitialized) {
                repository = GuestRepository(context)
            }
            return repository
        }
    }

    // CRIAMOS UM DATA CLASS, QUE IRÁ RECEBER OS VALORES VALIDOS E PASSAR PARA ESSA FUNÇÃO QUE IRÁ
    // SALVAR OS CONVIDADOS NO BANCO DE DADOS;
    // IMPLEMENTAÇÃO DOS METODOS DE CRUD - CREATE
    // INSERÇÃO DOS DADOS NO BANCO DE DADOS - CAMADA 3 - DADOS JÁ TRATADOS
    fun save (guest: GuestModel): Boolean {
        return try {
            // CRIO UM ALIAS PARA ESCREVER NO BANCO DE DADOS
            val db = mGuestDataBaseHelper.writableDatabase
            val contentValues = ContentValues()

            contentValues.put(DataBaseConstants.GUEST.COLUMNS.NAME, guest.name)
            contentValues.put(DataBaseConstants.GUEST.COLUMNS.PRESENCE, guest.presence)

            db.insert(DataBaseConstants.GUEST.TABLE_NAME, null, contentValues)
            true
        } catch (e: Exception) {
            false
        }
    }

    @SuppressLint("Range")
    fun get(id: Int): GuestModel? {

        var guest: GuestModel? = null

        return try {
            // CRIO UM ALIAS PARA ESCREVER NO BANCO DE DADOS
            val db = mGuestDataBaseHelper.readableDatabase

            val projection = arrayOf(DataBaseConstants.GUEST.COLUMNS.NAME,DataBaseConstants.GUEST.COLUMNS.PRESENCE)

            val selection = DataBaseConstants.GUEST.COLUMNS.ID + " = ?"
            val args = arrayOf(id.toString())

            val cursor = db.query(DataBaseConstants.GUEST.TABLE_NAME,
            projection,
            selection,
            args,
            null,
            null,
            null)

            if(cursor != null && cursor.count > 0) {
                cursor.moveToFirst()
                val name = cursor.getString(cursor.getColumnIndex(DataBaseConstants.GUEST.COLUMNS.NAME))
                val presence = (cursor.getInt(cursor.getColumnIndex(DataBaseConstants.GUEST.COLUMNS.PRESENCE)) == 1)

                guest = GuestModel(id, name, presence)
            }
            cursor?.close()
            guest
        } catch (e: Exception) {
            guest
        }
    }


    // FUNÇÃO PARA RETORNAR TODOS OS CONVIDADOS
    // IMPLEMENTAÇÃO DOS METODOS DE CRUD - READ
    @SuppressLint("Range")
    fun getAll (): List<GuestModel> {
        val list: MutableList<GuestModel> = ArrayList()

        var guest: GuestModel? = null


        return try {
            val db = mGuestDataBaseHelper.readableDatabase

            // NESSA PARTE DO CÓDIGO, EU CRIO UM ARRAY COM OS NOMES DAS COLUNAS
            val projection = arrayOf(DataBaseConstants.GUEST.COLUMNS.ID, DataBaseConstants.GUEST.COLUMNS.NAME, DataBaseConstants.GUEST.COLUMNS.PRESENCE)

//            // NESSA PARTE DO CÓDIGO, EU CRIO UMA STRING, QUE CONTEM O COMANDO DA SELECTION
//            val selection = DataBaseConstants.GUEST.COLUMNS.ID + " = ?"
//
//            // NESSA PARTE DO CÓDIGO, EU COLOCO C0MO STRING, O ID
//            val args = arrayOf(id.toString())

            // COM AS VARIÁVEIS ACIMA, EU CRIO UMA QUERY, ATRIBUINDO ELA A UMA OUTRA VARIAVEL
            val cursor = db.query(DataBaseConstants.GUEST.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null)

            // AQUI EU COLETO AS VARIAVEIS QUE O BANCO ENCONTROU ATRAVÉS DA MINHA QUERY
            if(cursor != null && cursor.count > 0) {
                // COLOCO ELE NA PRIMEIRA POSIÇÃO DA BUSCA
                cursor.moveToFirst()

                // AQUI EU FAÇO UMA INTERAÇÃO ENTRE TODOS OS RESULTADOS ENCONTRADOS NA BUSCA
                while(cursor.moveToNext()) {
                    // COLETO A VARIAVEL ID
                    val id = cursor.getInt(cursor.getColumnIndex(DataBaseConstants.GUEST.COLUMNS.ID))

                    // COLETO O VALOR NA VARIAVEL NAME
                    val name = cursor.getString(cursor.getColumnIndex(DataBaseConstants.GUEST.COLUMNS.NAME))

                    // COLETO O VALOR NA VARIAVEL PRESENCE
                    val presence = (cursor.getInt(cursor.getColumnIndex(DataBaseConstants.GUEST.COLUMNS.PRESENCE)) == 1)

                    guest = GuestModel(id, name, presence)
                    list.add(guest)
                }
            }
            cursor?.close()
            list
        } catch (e: Exception) {
            list
        }

//        return try {
//            // CRIO UM ALIAS PARA ESCREVER NO BANCO DE DADOS
//            val db = mGuestDataBaseHelper.readableDatabase
//
//            val projection = arrayOf(DataBaseConstants.GUEST.COLUMNS.ID, DataBaseConstants.GUEST.COLUMNS.NAME,DataBaseConstants.GUEST.COLUMNS.PRESENCE)
//
//            val cursor = db.query(DataBaseConstants.GUEST.TABLE_NAME,
//                projection,
//                null,
//                null,
//                null,
//                null,
//                null)
//
//            if(cursor != null && cursor.count > 0) {
//                while(cursor.moveToNext()) {
//                    val id = cursor.getInt(cursor.getColumnIndex(DataBaseConstants.GUEST.COLUMNS.ID))
//                    val name = cursor.getString(cursor.getColumnIndex(DataBaseConstants.GUEST.COLUMNS.NAME))
//                    val presence = (cursor.getInt(cursor.getColumnIndex(DataBaseConstants.GUEST.COLUMNS.PRESENCE)) == 1)
//                    val guest = GuestModel(id, name, presence)
//                    list.add(guest)
//                }
//            }
//            cursor?.close()
//            list
//        } catch (e: Exception) {
//            list
//        }

    }

    // FUNÇÃO PARA RETORNAR TODOS OS CONVIDADOS PRESENTES
    // IMPLEMENTAÇÃO DOS METODOS DE CRUD - READ
    @SuppressLint("Range")
    fun getPresent(): List<GuestModel> {
        val list: MutableList<GuestModel> = ArrayList()
        var guest: GuestModel? = null

        return try {
            val db = mGuestDataBaseHelper.readableDatabase

            // NESSA PARTE DO CÓDIGO, EU CRIO UM ARRAY COM OS NOMES DAS COLUNAS
            val projection = arrayOf(DataBaseConstants.GUEST.COLUMNS.ID, DataBaseConstants.GUEST.COLUMNS.NAME, DataBaseConstants.GUEST.COLUMNS.PRESENCE)

//            // NESSA PARTE DO CÓDIGO, EU CRIO UMA STRING, QUE CONTEM O COMANDO DA SELECTION
//            val selection = DataBaseConstants.GUEST.COLUMNS.ID + " = ?"
//
//            // NESSA PARTE DO CÓDIGO, EU COLOCO C0MO STRING, O ID
//            val args = arrayOf(id.toString())

            // COM AS VARIÁVEIS ACIMA, EU CRIO UMA QUERY, ATRIBUINDO ELA A UMA OUTRA VARIAVEL
            val cursor = db.query(DataBaseConstants.GUEST.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null)

            // AQUI EU COLETO AS VARIAVEIS QUE O BANCO ENCONTROU ATRAVÉS DA MINHA QUERY
            if(cursor != null && cursor.count > 0) {
                // COLOCO ELE NA PRIMEIRA POSIÇÃO DA BUSCA
                cursor.moveToFirst()

                // AQUI EU FAÇO UMA INTERAÇÃO ENTRE TODOS OS RESULTADOS ENCONTRADOS NA BUSCA
                while(cursor.moveToNext()) {
                    // COLETO A VARIAVEL ID
                    val id = cursor.getInt(cursor.getColumnIndex(DataBaseConstants.GUEST.COLUMNS.ID))

                    // COLETO O VALOR NA VARIAVEL NAME
                    val name = cursor.getString(cursor.getColumnIndex(DataBaseConstants.GUEST.COLUMNS.NAME))

                    // COLETO O VALOR NA VARIAVEL PRESENCE
                    val presence = (cursor.getInt(cursor.getColumnIndex(DataBaseConstants.GUEST.COLUMNS.PRESENCE)) == 1)

                    // SE PRESENCE FOR VERDADEIRO, EU ARMAZENO PRESENCE E NAME NA LISTA
                    if (presence) {
                        guest = GuestModel(id, name, presence)
                        list.add(guest)
                    }
                }
            }
            cursor?.close()
            list
        } catch (e: Exception) {
            list
        }


//        return try {
//            // CRIO UM ALIAS PARA ESCREVER NO BANCO DE DADOS
//            val db = mGuestDataBaseHelper.readableDatabase
//
//            val cursor = db.rawQuery("SELECT id, name, presence FROM Guest WHERE presence = 1", null)
//
//            if(cursor != null && cursor.count > 0) {
//                while(cursor.moveToNext()) {
//                    val id = cursor.getInt(cursor.getColumnIndex(DataBaseConstants.GUEST.COLUMNS.ID))
//                    val name = cursor.getString(cursor.getColumnIndex(DataBaseConstants.GUEST.COLUMNS.NAME))
//                    val presence = (cursor.getInt(cursor.getColumnIndex(DataBaseConstants.GUEST.COLUMNS.PRESENCE)) == 1)
//                    val guest = GuestModel(id, name, presence)
//                    list.add(guest)
//                }
//            }
//            cursor?.close()
//            list
//        } catch (e: Exception) {
//            list
//        }
    }

    // FUNÇÃO PARA RETORNAR TODOS OS CONVIDADOS AUSENTES
    // IMPLEMENTAÇÃO DOS METODOS DE CRUD - READ
    @SuppressLint("Range")
    fun getAbsent(): List<GuestModel> {
        val list: MutableList<GuestModel> = ArrayList()

        var guest: GuestModel? = null

        return try {
            val db = mGuestDataBaseHelper.readableDatabase

            // NESSA PARTE DO CÓDIGO, EU CRIO UM ARRAY COM OS NOMES DAS COLUNAS
            val projection = arrayOf(DataBaseConstants.GUEST.COLUMNS.ID, DataBaseConstants.GUEST.COLUMNS.NAME, DataBaseConstants.GUEST.COLUMNS.PRESENCE)

//            // NESSA PARTE DO CÓDIGO, EU CRIO UMA STRING, QUE CONTEM O COMANDO DA SELECTION
//            val selection = DataBaseConstants.GUEST.COLUMNS.ID + " = ?"
//
//            // NESSA PARTE DO CÓDIGO, EU COLOCO C0MO STRING, O ID
//            val args = arrayOf(id.toString())

            // COM AS VARIÁVEIS ACIMA, EU CRIO UMA QUERY, ATRIBUINDO ELA A UMA OUTRA VARIAVEL
            val cursor = db.query(DataBaseConstants.GUEST.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null)

                // AQUI EU COLETO AS VARIAVEIS QUE O BANCO ENCONTROU ATRAVÉS DA MINHA QUERY
                if(cursor != null && cursor.count > 0) {
                    // COLOCO ELE NA PRIMEIRA POSIÇÃO DA BUSCA
                    cursor.moveToFirst()
                    // AQUI EU FAÇO UMA INTERAÇÃO ENTRE TODOS OS RESULTADOS ENCONTRADOS NA BUSCA
                    while(cursor.moveToNext()) {
                        // COLETO A VARIAVEL ID
                        val id = cursor.getInt(cursor.getColumnIndex(DataBaseConstants.GUEST.COLUMNS.ID))

                        // COLETO O VALOR NA VARIAVEL NAME
                        val name = cursor.getString(cursor.getColumnIndex(DataBaseConstants.GUEST.COLUMNS.NAME))

                        // COLETO O VALOR NA VARIAVEL PRESENCE
                        val presence = (cursor.getInt(cursor.getColumnIndex(DataBaseConstants.GUEST.COLUMNS.PRESENCE)) == 1)

                        // SE PRESENCE FOR FALSO, EU ARMAZENO PRESENCE E NAME NA LISTA
                        if (!presence) {
                            guest = GuestModel(id, name, presence)
                            list.add(guest)
                        }
                    }
                }
            cursor?.close()
            list
        } catch (e: Exception) {
            list
        }



//        return try {
//            // CRIO UM ALIAS PARA ESCREVER NO BANCO DE DADOS
//            val db = mGuestDataBaseHelper.readableDatabase
//
//            val cursor = db.rawQuery("SELECT id, name, presence FROM Guest WHERE presence = 0", null)
//
//            if(cursor != null && cursor.count > 0) {
//                while(cursor.moveToNext()) {
//                    val id = cursor.getInt(cursor.getColumnIndex(DataBaseConstants.GUEST.COLUMNS.ID))
//                    val name = cursor.getString(cursor.getColumnIndex(DataBaseConstants.GUEST.COLUMNS.NAME))
//                    val presence = (cursor.getInt(cursor.getColumnIndex(DataBaseConstants.GUEST.COLUMNS.PRESENCE)) == 1)
//                    val guest = GuestModel(id, name, presence)
//                    list.add(guest)
//                }
//            }
//            cursor?.close()
//            list
//        } catch (e: Exception) {
//            list
//        }
    }

    // IMPLEMENTAÇÃO DOS METODOS DE CRUD - CREATE, READ, UPDATE, DELETE
    // IMPLEMENTAÇÃO DOS METODOS DE CRUD - UPDATE
    fun update (guest: GuestModel): Boolean {
        return try {
            // CRIO UM ALIAS PARA ESCREVER NO BANCO DE DADOS
            val db = mGuestDataBaseHelper.writableDatabase
            val contentValues = ContentValues()

            contentValues.put(DataBaseConstants.GUEST.COLUMNS.NAME, guest.name)
            contentValues.put(DataBaseConstants.GUEST.COLUMNS.PRESENCE, guest.presence)

            val selection = DataBaseConstants.GUEST.COLUMNS.ID + " = ?"
            val args = arrayOf(guest.id.toString())

            db.update(DataBaseConstants.GUEST.TABLE_NAME, contentValues, selection, args)
            true
        } catch (e: Exception) {
            false
        }
    }
    // IMPLEMENTAÇÃO DOS METODOS DE CRUD - DELETE
    fun delete (id: Int): Boolean {

        return try {
            // CRIO UM ALIAS PARA ESCREVER NO BANCO DE DADOS
            val db = mGuestDataBaseHelper.writableDatabase

            val selection = DataBaseConstants.GUEST.COLUMNS.ID + " = ?"
            val args = arrayOf(id.toString())

            db.delete(DataBaseConstants.GUEST.TABLE_NAME, selection, args)
            true
        } catch (e: Exception) {
            false
        }

    }
}