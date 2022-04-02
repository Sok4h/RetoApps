package com.sokah.retoapps.ui.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.sokah.retoapps.LoginActivity
import com.sokah.retoapps.R
import com.sokah.retoapps.RetoAppsAplication.Companion.prefs
import com.sokah.retoapps.User
import com.sokah.retoapps.UtilDomi
import com.sokah.retoapps.databinding.FragmentProfileBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    private var _binding: FragmentProfileBinding? = null
    private var file: File? = null
    lateinit var imgPath: String
    var loggedUser: User? = null
    var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    var FILE_NAME = "photo_" + timestamp + "_"

    val launcher = registerForActivityResult(StartActivityForResult(), ::onCameraResult)

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)


        //actualiza perfil
        profileViewModel.user.observe(viewLifecycleOwner) {

            loggedUser = it

            binding.editTextTextPersonName.setText(it.name)


            if(!it.picture.isEmpty()){
                Log.e("TAG", it.picture)
                var bitmap = BitmapFactory.decodeFile(it.picture)

                val thumbnail =
                    Bitmap.createScaledBitmap(
                        bitmap,
                        bitmap.width / 4,
                        bitmap.height / 4,
                        true
                    )
                binding.imgProfile.setImageBitmap(thumbnail)
            }




        }

        binding.btnLogOut.setOnClickListener {
            logOut()
        }

        binding.editProfile.setOnClickListener {

            updateUser()
        }


        binding.imgProfile.setOnClickListener {

            requestPermissions(
                arrayOf(
                    Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE
                ), 1
            )
        }

        val root: View = binding.root

        return root
    }

    private fun onCameraResult(result: ActivityResult) {

        if (result.resultCode == Activity.RESULT_OK) {

            imgPath = file?.path!!
            val imgBitmap = BitmapFactory.decodeFile(file?.path)
            Log.e("bitmap", file?.path!!)
            val thumbnail =
                Bitmap.createScaledBitmap(
                    imgBitmap,
                    imgBitmap.width / 4,
                    imgBitmap.height / 4,
                    true
                )
            binding.imgProfile.setImageBitmap(thumbnail)
        } else if (result.resultCode == Activity.RESULT_CANCELED) {

            Toast.makeText(context, "Operación cancelada", Toast.LENGTH_SHORT).show()
        }

    }

    private fun updateUser() {

        if (binding.editTextTextPersonName.text.isNotEmpty() && !binding.editTextTextPersonName.text.contentEquals(
                loggedUser?.name
            )
        ) loggedUser?.name = binding.editTextTextPersonName.text.toString()

        if(imgPath.isNotEmpty() && !imgPath.contentEquals(loggedUser?.picture)) loggedUser?.picture=imgPath


        profileViewModel.updateUser(loggedUser!!)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1) {
            var allowed = true;
            for (result in grantResults) {

                if (result == PackageManager.PERMISSION_DENIED) allowed = false
            }

            if (allowed) {


                handleModal()
            } else {

                Toast.makeText(context, "No aceptaste los permisos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleModal() {

        val view: View = layoutInflater.inflate(R.layout.bottomsheet, null)
        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(view)

        val camera: ConstraintLayout = view.findViewById(R.id.clCamera)
        val gallery: ConstraintLayout = view.findViewById(R.id.clGallery)

        camera.setOnClickListener {

            cameraHandler()

        }
        gallery.setOnClickListener {
            Toast.makeText(context, "gallery", Toast.LENGTH_SHORT).show()

        }
        dialog.show()

    }

    fun cameraHandler() {

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        file = File("${requireContext().getExternalFilesDir(null)}/ $FILE_NAME")
        val uri = FileProvider.getUriForFile(requireContext(), requireContext().packageName, file!!)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        launcher.launch(intent)

    }




    fun logOut() {

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