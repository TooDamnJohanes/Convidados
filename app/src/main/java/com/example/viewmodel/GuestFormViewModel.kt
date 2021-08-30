package com.example.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.service.model.GuestModel
import com.example.service.repository.GuestRepository

class GuestFormViewModel(application: Application): AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    // ... QUANDO O FORM CHAMA ESTE ARQUIVO, EU PEGO UM CONTEXTO, E AUTOMATICAMENTE JÁ
    // CHAMO MEU REPOSITORIO, QUE TEM OS METODOS CRUD E SINGLETON, RESPONSÁVEL EM CONVERSAR COM
    // MEU BANCO DE DADOS SQLITE. ESSA VARIÁVEL TAMBÉM SERÁ RESPONSÁVEL EM ACESSAR O BANCO DE DADOS
    private val mContext = application.applicationContext

    // CHAMO MEU REPOSITÓRIO, COMO ELE TEM UM CONSTRUTOR PRIVADO, EU CHAMO UM OBJETO DELE,
    // PASSANDO UM CONTEXTO, QUE ERA O QUE FALTAVA PARA INICIAR O BANCO DE DADOS
    private val mGuestRepository: GuestRepository = GuestRepository.getInstance(mContext)

    private var mSaveGuest = MutableLiveData<Boolean>()
    val saveGuest: LiveData<Boolean> = mSaveGuest

    private var mGuest = MutableLiveData<GuestModel>()
    val guest: LiveData<GuestModel> = mGuest

    // AGORA EU PEGO O REPOSITÓRIO, E SALVO O USUÁRIO
    // CHAMO O MÉTODO NO REPOSITÓRIO, QUE SERÁ RESPONSÁVEL EM SALVAR OS DADOS NO BANCO DE DADOS
    // CHAMADA PARA SALVAR - CAMADA 2 - DADOS JÁ VALIDADOS E TRATADOS
    fun save(id: Int ,nome: String, presence: Boolean) {
        val guest = GuestModel(id = id, name = nome, presence = presence)

        // SE O ID FOR ZERO, SIGNIFICA QUE O USUÁRIO É NOVO, ENTÃO CRIAMOS UM NOVO CONVIDADO
        if(id == 0) {
            // MUDANDO O mGuestRepository MUDAMOS O mSaveGuest, QUE MUDA O OBSERVADOR, QUE DA UM RETORNO
            mSaveGuest.value = mGuestRepository.save(guest)
        }else {
            // CASO O ID SEJA != DE 0, ENTÃO NÓS TEMOS UM USUÁRIO EXISTENTE, E PRECISAMOS APENAS ATUALIZAR
            // OS VALORES QUE ESTÃO NELE
            mSaveGuest.value = mGuestRepository.update(guest)
        }

    }

    fun load(id: Int) {
        mGuest.value = mGuestRepository.get(id)
    }
}