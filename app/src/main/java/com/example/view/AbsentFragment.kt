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
import com.example.convidados.databinding.FragmentAbsentBinding
import com.example.service.constants.GuestConstants
import com.example.view.adapter.GuestAdapter
import com.example.view.listener.GuestListener
import com.example.viewmodel.GuestViewModel

class AbsentFragment : Fragment() {

    private lateinit var mViewModel: GuestViewModel
    private val mAdapter: GuestAdapter = GuestAdapter()
    private lateinit var mListener: GuestListener

    // This property is only valid between onCreateView and
    // onDestroyView.

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewModel =
            ViewModelProvider(this).get(GuestViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_absent, container, false)

        /*
        // RECYCLERVIEW
        1- OBTER A RECYCLER
        2- DEFINIR UM LAYOUT
        3- DEFINIR UM ADAPTER
         */

        // 1- OBTER A RECYCLER
        val recycler = root.findViewById<RecyclerView>(R.id.recycler_absents_guests)

        // 2- DEFINIR UM LAYOUT
        recycler.layoutManager = LinearLayoutManager(context)

        // 3- DEFINIR UM ADAPTER
        recycler.adapter = mAdapter
        mListener = object: GuestListener{
            override fun onClick(id: Int) {

                val intent = Intent(context, GuestFormActivity::class.java)

                val bundle = Bundle()
                bundle.putInt(GuestConstants.GUESTID, id)

                intent.putExtras(bundle)
                startActivity(intent)
            }

            override fun onDelete(id: Int) {
                mViewModel.delete(id)
                mViewModel.load(GuestConstants.FILTER.ABSENT)
            }
        }
        mAdapter.attachListener(mListener)
        observer()
        return root
    }

    override fun onResume() {
        super.onResume()
        mViewModel.load(GuestConstants.FILTER.ABSENT)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observer() {
        mViewModel.guestList.observe(viewLifecycleOwner, Observer {
            mAdapter.updateGuest(it)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}