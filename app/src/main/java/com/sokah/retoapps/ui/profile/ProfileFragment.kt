package com.sokah.retoapps.ui.profile

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
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
import com.sokah.retoapps.ui.LoginActivity
import com.sokah.retoapps.R
import com.sokah.retoapps.RetoAppsAplication.Companion.prefs
import com.sokah.retoapps.UtilDomi
import com.sokah.retoapps.model.User
import com.sokah.retoapps.databinding.FragmentProfileBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    private var _binding: FragmentProfileBinding? = null
    private var file: File? = null
    private var imgPath: String? = null
    var loggedUser: User? = null
    var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    var FILE_NAME = "photo_" + timestamp + "_"
    lateinit var dialog: BottomSheetDialog

    val launcher = registerForActivityResult(StartActivityForResult(), ::onCameraResult)
    val galleryLauncher = registerForActivityResult(StartActivityForResult(), ::onGalleryResult)



    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        Log.e("TAG", "onGalleryResult: ", )
        //actualiza perfil
        profileViewModel.user.observe(viewLifecycleOwner) {

            loggedUser = it

            binding.editTextTextPersonName.setText(it.name)


            if (!it.picture.isEmpty()) {
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

        return binding.root
    }

    private fun onCameraResult(result: ActivityResult) {

        if (result.resultCode == RESULT_OK) {

            imgPath = file?.path!!
            setProfileImg(imgPath!!)
        } else if (result.resultCode == Activity.RESULT_CANCELED) {

            Toast.makeText(context, "Operaci??n cancelada", Toast.LENGTH_SHORT).show()
        }
        dialog.dismiss()
    }

    private fun updateUser() {

        if (binding.editTextTextPersonName.text.isNotEmpty() && !binding.editTextTextPersonName.text.contentEquals(
                loggedUser?.name
            )
        ) loggedUser?.name = binding.editTextTextPersonName.text.toString()

        if(!imgPath.isNullOrEmpty()&&!imgPath.contentEquals(loggedUser?.picture)){

            loggedUser?.picture=imgPath!!
        }


        profileViewModel.updateUser(loggedUser!!)

        Toast.makeText(context,"Datos actualizados",Toast.LENGTH_SHORT).show()

    }

    private fun onGalleryResult(activityResult: ActivityResult) {

        if(activityResult.resultCode==RESULT_OK){

            val uri = activityResult.data?.data
            Log.e("Uri", uri!!.toString())
            imgPath= UtilDomi.getPath(requireContext(),uri)

            Log.e("Path", imgPath!!)

            setProfileImg(imgPath!!)
        }
        else{

            Toast.makeText(context, "Operaci??n cancelada", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1) {
            var allowed = true
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

    private fun setProfileImg(path: String){

        val imgBitmap = BitmapFactory.decodeFile(path)
        val thumbnail =
            Bitmap.createScaledBitmap(
                imgBitmap,
                imgBitmap.width / 4,
                imgBitmap.height / 4,
                true
            )
        binding.imgProfile.setImageBitmap(thumbnail)
    }

    private fun handleModal() {

        val view: View = layoutInflater.inflate(R.layout.bottomsheet, null)
         dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(view)

        val camera: ConstraintLayout = view.findViewById(R.id.clCamera)
        val gallery: ConstraintLayout = view.findViewById(R.id.clGallery)

        camera.setOnClickListener {

            cameraHandler()
            dialog.dismiss()

        }
        gallery.setOnClickListener {

            galleryHandler()
            dialog.dismiss()

        }
        dialog.show()

    }

    private fun galleryHandler() {

        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type="image/*"
        galleryLauncher.launch(intent)

    }

    private fun cameraHandler() {

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        file = File("${requireContext().getExternalFilesDir(null)}/ $FILE_NAME")
        val uri = FileProvider.getUriForFile(requireContext(), requireContext().packageName, file!!)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        launcher.launch(intent)

    }




    private fun logOut() {

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