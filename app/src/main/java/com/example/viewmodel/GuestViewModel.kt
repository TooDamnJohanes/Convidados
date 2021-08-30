package com.example.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.service.constants.GuestConstants
import com.example.service.model.GuestModel
import com.example.service.repository.GuestRepository

class GuestViewModel(apllication: Application) : AndroidViewModel(apllication) {

    // PARA CONSEGUIR CARREGAR UMA LISTA, PRECISAMOS ACESSAR O BANCO DE DADOS CRIADO
    private val mGuestRepository = GuestRepository.getInstance(apllication.applicationContext)

    // PARA QUE AS MUDANÇAS DE VARIÁVEL SEJAM VISÍVEIS, NÓS CRIAMOS UMA LISTA QUE RECEBE OS DADOS
    // DE UMA DATACLASS DE CONVIDADOS
    private val mGuestList = MutableLiveData<List<GuestModel>>()

    // ESTA É A NOSSA LIVEDATA, QUE RECEBE A LISTA QUE O BANCO DE DADOS RETORNA PARA ELA,
    // DE ACORDO COM AS FILTRAGENS
    val guestList: LiveData<List<GuestModel>> = mGuestList

    // MÉTODO RESPONSÁVEL EM TRAZER A LISTA DE CONVIDADOS
    fun load(filter: Int) {
        when (filter) {
            GuestConstants.FILTER.EMPTY -> {
                // DEFINO QUE OS VALORES DESSA LISTA, SÃO REFERENTES A UMA CONSULTA NO BANCO DE DADOS
                // ONDE EU PEGO TODOS OS CONVIDADOS
                mGuestList.value = mGuestRepository.getAll()
            }
            GuestConstants.FILTER.PRESENT -> {
                // DEFINO QUE OS VALORES DESSA LISTA, SÃO REFERENTES A UMA CONSULTA NO BANCO DE DADOS
                // ONDE EU PEGO TODOS OS CONVIDADOS PRESENTES
                mGuestList.value = mGuestRepository.getPresent()
            }
            GuestConstants.FILTER.ABSENT -> {
                // DEFINO QUE OS VALORES DESSA LISTA, SÃO REFERENTES A UMA CONSULTA NO BANCO DE DADOS
                // ONDE EU PEGO TODOS OS CONVIDADOS AUSENTES
                mGuestList.value = mGuestRepository.getAbsent()
            }
            else -> {
                // DEFINO QUE OS VALORES A SEREM MOSTRADOS QUANDO NÃO HÁ UMA SELEÇÃO DE FILTRO ALGUM
                // SERÁ TODOS OS CONVIDADOS
                mGuestList.value = mGuestRepository.getAll()
            }
        }
    }

    fun delete(id: Int) {
        // DELETO O CARA DIRETO DO BANCO DE DADOS
        mGuestRepository.delete(id)
    }
}