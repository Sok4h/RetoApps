package com.sokah.retoapps.ui.profile

import android.Manifest
import android.Manifest.permission_group.STORAGE
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.sokah.retoapps.LoginActivity
import com.sokah.retoapps.R
import com.sokah.retoapps.RetoAppsAplication.Companion.prefs
import com.sokah.retoapps.User
import com.sokah.retoapps.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var bottomsheet :BottomSheetBehavior<ConstraintLayout>
    private var _binding: FragmentProfileBinding? = null
    var loggedUser :User? = null
    val launcher = registerForActivityResult(StartActivityForResult(),::onCameraResult)

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


        binding.imgProfile.setOnClickListener {

            requestPermissions(arrayOf(Manifest.permission.CAMERA
                ,Manifest.permission.READ_EXTERNAL_STORAGE),1)
        }
         loadUser()
        val root: View = binding.root

        profileViewModel.text.observe(viewLifecycleOwner){

        }
        return root
    }

    private fun onCameraResult(result: ActivityResult) {

        val bitmap = result.data?.extras?.get("data") as Bitmap

        binding.imgProfile.setImageBitmap(bitmap)

    }

    private fun changeProfilePicture() {


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode==1){
             var allowed = true;
            for(result in grantResults){

                if(result==PackageManager.PERMISSION_DENIED) allowed= false
            }

            if(allowed){


                handleModal()
            }

            else{

                Toast.makeText(context,"No aceptaste los permisos",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleModal() {

        val view :View = layoutInflater.inflate(R.layout.bottomsheet,null)
        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(view)

        val camera : ConstraintLayout = view.findViewById(R.id.clCamera)
        val gallery : ConstraintLayout = view.findViewById(R.id.clGallery)
        camera.setOnClickListener{

            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            launcher.launch(intent)

        }
        gallery.setOnClickListener{
            Toast.makeText(context,"gallery",Toast.LENGTH_SHORT).show()

        }
        dialog.show()

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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}