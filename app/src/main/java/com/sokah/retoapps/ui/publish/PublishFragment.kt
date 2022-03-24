package com.sokah.retoapps.ui.publish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sokah.retoapps.databinding.FragmentPublishBinding

class PublishFragment : Fragment() {

    private lateinit var publishViewModel: PublishViewModel
    private var _binding: FragmentPublishBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        publishViewModel =
            ViewModelProvider(this).get(PublishViewModel::class.java)

        _binding = FragmentPublishBinding.inflate(inflater, container, false)
        val root: View = binding.root


        publishViewModel.text.observe(viewLifecycleOwner, Observer {

        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}