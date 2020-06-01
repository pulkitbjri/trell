package com.example.trellassign

import android.app.Application
import android.net.Uri
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.blackbox.ffmpeg.examples.callback.FFMpegCallback
import java.io.File

class SharedViewModel(application: Application) : AndroidViewModel(application) {
    private var actualFile = MutableLiveData<File>()
    var convertedFilePath = MutableLiveData<File>()
    private var filePathURI = MutableLiveData<Uri>()
    var switchFragment = MutableLiveData<String>() //1,2,3 for screens respectively
    var pickerBtnClicked = MutableLiveData<Boolean>()
    var progressLiveData = MutableLiveData<Boolean>()



    fun setSwitchFrag(screenId : String){
        switchFragment.value=screenId
    }

    fun onPickerBtnClicked(){
        pickerBtnClicked.value= true
    }

    fun setFilePathURI(data: Uri) {
        filePathURI.value=data
    }
    fun getFilePathUri(): Uri? {
        return filePathURI.value
    }

    fun compressVideo(outputFolderPath: String, bitRate: String) {
       ExecuteCompression().execute(outputFolderPath,bitRate)
    }

    fun getOutputPath(outputFolderPath: String): String {
        val path = outputFolderPath + File.separator + "CompressedVideos" + File.separator

        val folder = File(path)
        if (!folder.exists())
            folder.mkdirs()

        return path
    }

    fun setFile(file: File) {
        this.actualFile.value = file
    }

    inner class ExecuteCompression : AsyncTask<String, String, String>() {
        var file : File? = null
        override fun onPreExecute() {
            super.onPreExecute()
            progressLiveData.value=true
        }
        override fun doInBackground(vararg params: String?): String? {
            val outputPath = getOutputPath(params[0]!!)
            VideoCompressor.with(getApplication())
                .setFile(actualFile.value!!)
                .setOutputPath(outputPath)
                .setBitRate(params[1]!!)
                .setOutputFileName("compressed_" + System.currentTimeMillis() + ".mp4")
                .setCallback(object : FFMpegCallback{

                    override fun onSuccess(convertedFile: File) {
                        Log.i("","")
                        file=convertedFile
                    }

                    override fun onFailure(error: Exception) {
                        Log.i("","")
                    }

                })
                .compress()
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (file==null)
                return
            convertedFilePath.value=file
            progressLiveData.value=false
            setSwitchFrag("2")
        }

    }
}