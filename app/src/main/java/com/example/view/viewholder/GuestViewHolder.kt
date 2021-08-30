package com.example.view.viewholder

import android.app.AlertDialog
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.convidados.R
import com.example.service.model.GuestModel
import com.example.view.listener.GuestListener

/*
GUARDA AS REFERENCIAS DE UM LAYOUT
ELA VAI FAZER A MANIPULAÇÃO DOS ELEMENTOS DE INTERFACE
 */

class GuestViewHolder(itemView: View, private val listener: GuestListener) : RecyclerView.ViewHolder(itemView) {

    // ELA QUEM VAI FAZER A MANIPULAÇÃO, A ATRIBUIÇÃO DOS ELEMENTOS DE INTERFACE
    fun bind(guest: GuestModel) {
        // COMO EU TENHO O ID DESSE CARA, EU FAÇO UMA PESQUISA DE QUAL É O NOME DELE
        val textName = itemView.findViewById<TextView>(R.id.text_name)

        // ACHANDO O NOME DELE, EU COLOCO EM UMA LISTA
        textName.text = guest.name

        textName.setOnClickListener {
            listener.onClick(guest.id)
        }

        textName.setOnLongClickListener {

            AlertDialog.Builder(itemView.context)
                .setTitle(R.string.remocao_convidado)
                .setMessage(R.string.deseja_remover)
                .setPositiveButton(R.string.remover) {dialog, which ->
                    listener.onDelete(guest.id)
                }
                .setNeutralButton(R.string.cancelar, null)
                .show()

            true
        }
    }

}