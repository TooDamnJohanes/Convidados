package com.example.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.convidados.R
import com.example.service.model.GuestModel
import com.example.view.listener.GuestListener
import com.example.view.viewholder.GuestViewHolder

/*
RESPONSÁVEL EM CRIAR O LAYOUT DO RECYCLERVIEW
 */

class GuestAdapter : RecyclerView.Adapter<GuestViewHolder>() {
    // AQUI NOS VAMOS CRIAR UMA LISTA, QUE VAI ARMAZENAR A LISTA DE CONVIDADOS
    private var mGuestList: List<GuestModel> = arrayListOf()
    private lateinit var mListener: GuestListener

    // ESSA FUNÇÃO, onCreateViewHolder TEM A MESMA FUNCIONALIDADE QUE O inflate
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuestViewHolder {
        // AQUI NOS CHAMAMOS O METODO INFLATE, PORQUE ELE VAI SER O RESPONSÁVEL EM CHAMAR O LAYOUT
        // QUE ESTAMOS CRIANDO PARA A VISUALIZAÇÃO DA NOSSA LISTA
        val item = LayoutInflater.from(parent.context).inflate(R.layout.row_guest, parent, false)

        // AQUI DECLARAMOS EM QUAL ITEM, GOSTARIAMOS DE TER A NOSSA LISTA A MOSTRA
        return GuestViewHolder(item, mListener)
    }

    // RETORNA QUANTOS ELEMENTOS UMA RECICLERVIEW POSSUI
    override fun getItemCount(): Int {
        // PRECISAMOS RETORNAR A NOSSA LINSTA DE CONVIDADOS
        return mGuestList.count()
    }

    // É O MOMENTO ONDE DEPOIS DO LAYOUT CRIADO, ELE PEGA OS DADOS E REALIZA A ATRIBUIÇÕES NECESSÁRIAS
    // PARA CADA REGISTRO DA NOSSA LISTA, ESSE METODO É CHAMADO
    override fun onBindViewHolder(holder: GuestViewHolder, position: Int) {
        holder.bind(mGuestList[position])
    }

    // FUNÇÕES EXTRAS A QUAL IMPLEMENTEI, PARA PEGAR A LISTA;
    @SuppressLint("NotifyDataSetChanged")
    // COM A CHAMADA DESTA FUNÇÃO, ELA SERÁ RESPONSÁVEL EM NOTIFICAR CADA MUDANÇA QUE A MINHA
    // LIVEDATA LIST SOFRER ALTERAÇÕES
    fun updateGuest(list: List<GuestModel>) {
        mGuestList = list
        // POR QUESTÕES DE ARQUITETURA DE CÓDIGO, ESSA LÓGICA VEIO PARA CA, ONDE EU NOTIFICO
        // ... AS ALTERAÇÕES
        notifyDataSetChanged()
    }

    fun attachListener(listener: GuestListener) {
        mListener = listener
    }

}