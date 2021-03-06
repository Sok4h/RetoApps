package com.sokah.retoapps.ui.publish

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
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.sokah.retoapps.R
import com.sokah.retoapps.UtilDomi
import com.sokah.retoapps.databinding.FragmentPublishBinding
import com.sokah.retoapps.model.Post
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class PublishFragment : Fragment() {

    private lateinit var publishViewModel: PublishViewModel
    private var _binding: FragmentPublishBinding? = null
    private var file: File? = null
    private var imgPath: String? = null
    private val binding get() = _binding!!
    lateinit var dialog: BottomSheetDialog
    var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    var FILE_NAME = "post" + timestamp + "_"

    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), ::onCameraResult)
    val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), ::onGalleryResult)




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        publishViewModel =
            ViewModelProvider(this).get(PublishViewModel::class.java)

        _binding = FragmentPublishBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnPublish.setOnClickListener {

            uploadPost()
        }

        binding.addImg.setOnClickListener {


            requestPermissions(
                arrayOf(
                    Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE
                ), 1
            )
        }
        return root
    }

    private fun uploadPost() {

        if(imgPath.isNullOrEmpty()||binding.etPost.text.isEmpty()) {

            Toast.makeText(context, "Complete todos los campos", Toast.LENGTH_SHORT).show()
        }

        else{

            val post = Post(UUID.randomUUID().toString(),publishViewModel.getUser().id,binding.etPost.text.toString(),binding.spinner.selectedItem.toString(),
                Calendar.getInstance(), imgPath!!)

            publishViewModel.uploadPost(post)

            Toast.makeText(context, "Post realizado correctamente",Toast.LENGTH_SHORT).show()

            clear()
        }

    }


    private fun onCameraResult(result: ActivityResult) {

        if (result.resultCode == Activity.RESULT_OK) {

            imgPath = file?.path!!
           setProfileImg(imgPath!!)
        } else if (result.resultCode == Activity.RESULT_CANCELED) {

            Toast.makeText(context, "Operaci??n cancelada", Toast.LENGTH_SHORT).show()
        }

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

    private fun cameraHandler() {

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        file = File("${requireContext().getExternalFilesDir(null)}/ $FILE_NAME")
        val uri = FileProvider.getUriForFile(requireContext(), requireContext().packageName, file!!)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        launcher.launch(intent)

    }

    private fun galleryHandler() {

        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type="image/*"
        galleryLauncher.launch(intent)

    }

    private fun onGalleryResult(activityResult: ActivityResult) {

        if(activityResult.resultCode== Activity.RESULT_OK){

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


    private fun setProfileImg(path: String){

        val imgBitmap = BitmapFactory.decodeFile(path)
        val thumbnail =
            Bitmap.createScaledBitmap(
                imgBitmap,
                imgBitmap.width,
                imgBitmap.height ,
                true
            )
        binding.imgPost.setImageBitmap(thumbnail)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun clear(){

        binding.etPost.setText("")
        binding.imgPost.setImageBitmap(BitmapFactory.decodeFile(""))
    }
}