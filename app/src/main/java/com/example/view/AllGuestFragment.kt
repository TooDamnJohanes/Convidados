package com.example.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.convidados.R
import com.example.convidados.databinding.FragmentHomeBinding
import com.example.service.constants.GuestConstants
import com.example.view.adapter.GuestAdapter
import com.example.view.listener.GuestListener
import com.example.viewmodel.GuestViewModel

class AllGuestFragment : Fragment() {

    private lateinit var mViewModel: GuestViewModel
    private var _binding: FragmentHomeBinding? = null
    // COMO EU PRECISO CHAMAR ESSA FUNÇÃO EM OUTRAS FUNÇÕES, DECLARO ELA
    // COMO UMA VARIAVEL GLOBAL, ASSIM CONSIGO REALIZAR A CHAMADA DELA A QUALQUER INSTANTE
    private val mAdapter: GuestAdapter = GuestAdapter()
    private lateinit var mListener: GuestListener

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewModel =
            ViewModelProvider(this).get(GuestViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /*
        // RECYCLERVIEW
        1- OBTER A RECYCLER
        2- DEFINIR UM LAYOUT
        3- DEFINIR UM ADAPTER
         */

        // 1- OBTER A RECYCLER
        /*
        QUANDO A FRAGMENT VEM PARA A TELA, O INFLATER CRIA O LAYOUT. POR ISSO UTILIZAMOS A VARIAVEL
        ROOT, PORQUE É ELA QUEM ARMAZENA O LAYOUT
         */
        val recycler = root.findViewById<RecyclerView>(R.id.recycler_all_guests)

        // 2- DEFINIR UM LAYOUT
        /*
        ISSO NÃO É UM LAYOUT VISUAL, MAS SIM, COMO ELA SE COMPORTA, EM CÓDIGO;
         */
        // ISSO É COMO ELA SE COMPORTA NA TELA, COMO O COMPORTAMENTO DO RECICLERVIEW TERÁ
        recycler.layoutManager = LinearLayoutManager(context)

        // 3- DEFINIR UM ADAPTER
        /*
        AQUI É A PARTE MAIS COMPLICADA;
        ELE VAI PEGAR O ELEMENTO DO LAYOUT, PEGAR OS DADOS DO REPOSITORIO, E JUNTAR OS DOIS
        ELE PEGA O LAYOUT, PEGA OS DADOS, E COLA OS DOIS;

        DEFINIMOS UM ADAPTER PARA ELE, QUE VEM DA CLASSE GuestAdapter, QUE TEM UM HOLDER, que vem DE
        GuestViewHolder
         */
        // O ADAPTER ESTÁ COMO mAdapter PORQUE JÁ FOI DECLARADO ACIMA O QUE ELE É
        // PARA QUE TODOS TENHAM ACESSO A ELA
        recycler.adapter = mAdapter
        mListener = object: GuestListener{
            override fun onClick(id: Int) {
                // AQUI EU FAÇO A DECLARAÇÃO DE QUAL JANELA EU GOSTARIA DE ABRIR, DANDO
                // UM CLICK NA TELA
                val intent = Intent(context, GuestFormActivity::class.java)

                // CRIO UM BUNDLE, COM O VALOR DE ID DELE
                val bundle = Bundle()
                bundle.putInt(GuestConstants.GUESTID, id)

                intent.putExtras(bundle)
                startActivity(intent)
            }

            override fun onDelete(id: Int) {
                mViewModel.delete(id)
                mViewModel.load(GuestConstants.FILTER.EMPTY)
            }
        }
        mAdapter.attachListener(mListener)
        // COMO EU CRIO UMA LISTA DE LIVE DATA, PRECISO FICAR ESCUTANDO AS MODIFICAÇÕES QUE
        // ESSE CARA SOFRE
        observer()

        return root
    }

    // ESSE É O ESTADO EM QUE A TELA FICA, QUANDO NÃO ESTAMOS EXECUTANDO NENHUM TIPO DE COMANDO
    // OU CLICK. LOGO, POSSO PEDIR PARA ELE CARREGAR AS LISTAS;
    override fun onResume() {
        super.onResume()
        // METODO RESPONSÁVEL EM TRAZER A LISTA DE CONVIDADOS
        mViewModel.load(GuestConstants.FILTER.EMPTY)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observer() {
        // NESSA FUNÇÃO EU FICO OBSERVANDO AS MODIFICAÇÕES QUE A LISTA DE CONVIDADOS SOFRE, O TEMPO
        // ... INTEIRO. O viewLifercycleOwner FAZ O MESMO PAPEL DO CONTEXTO, SÓ QUE É
        // ... DO RECICLYERVIEW
        mViewModel.guestList.observe(viewLifecycleOwner, Observer {
            // CHAMO ESSA FUNÇÃO, PARA QUE ELA SEJA RESPONSÁVEL EM ME NOTIFICAR AS MUDANÇAS
            // DE VARIAVEL
            mAdapter.updateGuest(it)
        })
    }


}