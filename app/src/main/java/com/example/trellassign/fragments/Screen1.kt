package com.example.trellassign.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toFile
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.trellassign.R
import com.example.trellassign.SharedViewModel
import com.example.trellassign.databinding.FragmentScreenBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class Screen1 : Fragment() {

    private val viewmodel: SharedViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentScreenBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_screen, container, false)
        binding.vm=viewmodel
        val view: View = binding.getRoot()
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewmodel.pickerBtnClicked.observe(viewLifecycleOwner, Observer{
            openPicker()
        })
    }
    private fun openPicker(){
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        )

        startActivityForResult(
            Intent.createChooser(intent, "Select Video"),
            REQUEST_TAKE_GALLERY_VIDEO
        )
    }
    private var file: File? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
//                val selectedVideoPath = getPath(data!!.data!!)
//                try {
//                    if (selectedVideoPath == null) {
//                        Toast.makeText(context,"selected video path = null!",Toast.LENGTH_LONG).show()
//                    } else {
//                        Toast.makeText(context,selectedVideoPath,Toast.LENGTH_LONG).show()
//                        viewmodel.setFilePath(selectedVideoPath)
//                        viewmodel.setFilePathURI(data.data!!)
//                    }
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
                data?.data?.let {
                    if (it.scheme.equals("content")) {
                        val pdfBytes =
                            (requireContext().contentResolver?.openInputStream(it))?.readBytes()
                        file = File(
                            requireContext().getExternalFilesDir(null),
                            "${System.currentTimeMillis()}.mp4"
                        )

                        if (file!!.exists())
                            file!!.delete()
                        try {
                            val fos = FileOutputStream(file!!.path)
                            fos.write(pdfBytes)
                            fos.close()
                            viewmodel.setFilePathURI(data!!.data!!)
                            viewmodel.setFile(file!!)
                            viewmodel.setSwitchFrag("2")

                        } catch (e: Exception) {
                            Log.e("PDF File", "Exception in pdf callback", e)
                        }
                    } else {
                        file = it.toFile()
                    }
                }
            }
        }
    }
    fun getPath(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Video.Media.DATA)
        val cursor: Cursor? = requireContext().contentResolver.query(uri, projection, null, null, null)
        return if (cursor != null) {
            val column_index: Int = cursor
                .getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(column_index)
        } else null

    }
    companion object {
        private const val REQUEST_TAKE_GALLERY_VIDEO: Int =1
        @JvmStatic
        fun newInstance() =
            Screen1().apply {
                arguments = Bundle().apply {
//                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
