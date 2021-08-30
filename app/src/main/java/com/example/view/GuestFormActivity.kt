package com.example.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.viewmodel.GuestFormViewModel
import com.example.convidados.R
import com.example.service.constants.GuestConstants
import kotlinx.android.synthetic.main.activity_guest_form.*
import kotlinx.android.synthetic.main.row_guest.*

class GuestFormActivity : AppCompatActivity(), View.OnClickListener {

    // AQUI, EU FAÇO UMA CHAMADA DO GuestModelViewModel
    // FAÇO UM LATEINIT, PORQUE AINDA NÃO EXISTE UM CONTEXTO EXISTENTE
    private lateinit var mViewModel : GuestFormViewModel
    private var mGuestId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guest_form)

        // INICIO MINHA VARIAVEL, PORQUE AQUI JÁ EXISTE UM CONTEXTO EXISTENTE
        // AO INICIAR ESSA FUNÇÃO ... (OLHAR GuestFormViewModel)
        mViewModel = ViewModelProvider(this).get(GuestFormViewModel::class.java)
        setListeners()
        observe()
        loadData()
        radiobuttonConfirma.isChecked = true
    }

    override fun onClick(view: View) {
        val id = view.id
        if(id == R.id.button_save) {
            val name = editGetNome.text.toString()
            val presence = radiobuttonConfirma.isChecked

            // CHAMO O METODO DE SALVAR O USUÁRIO - CAMADA 1 - DADOS CRUS
            if(name != "" && (radiobuttonConfirma.isChecked || radiobuttonAusente.isChecked)) {
                mViewModel.save(mGuestId, name, presence)
            }
            else {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadData() {
        val bundle = intent.extras

        if(bundle != null) {
            mGuestId = bundle.getInt(GuestConstants.GUESTID)
            mViewModel.load(mGuestId)
        }
    }

    private fun observe() {
        mViewModel.saveGuest.observe(this, Observer {
            if(it) {
                Toast.makeText(applicationContext, "Sucesso bro", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(applicationContext, "Falha", Toast.LENGTH_SHORT).show()
            }
            finish()
        })

        mViewModel.guest.observe(this, Observer {
            editGetNome.setText(it.name)
            if(it.presence) {
                radiobuttonConfirma.isChecked = true
            } else {
                radiobuttonAusente.isChecked = true
            }
        })
    }

    private fun setListeners() {
        button_save.setOnClickListener(this)
    }

}