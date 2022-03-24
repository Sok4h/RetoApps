package com.sokah.retoapps.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.sokah.retoapps.LoginActivity
import com.sokah.retoapps.RetoAppsAplication.Companion.prefs
import com.sokah.retoapps.User
import com.sokah.retoapps.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    private var _binding: FragmentProfileBinding? = null
    var loggedUser :User? = null

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.btnLogOut.setOnClickListener {
            logOut()
        }
         loadUser()
        val root: View = binding.root

        profileViewModel.text.observe(viewLifecycleOwner){

        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun loadUser(){

        loggedUser= prefs.getLoggedUser()

        binding.editTextTextPersonName.setText(loggedUser?.name)

    }

    fun logOut(){

        prefs.logOut()
        val intent = Intent(context, LoginActivity::class.java)
        activity?.startActivity(intent)
        activity?.finish()
    }
}