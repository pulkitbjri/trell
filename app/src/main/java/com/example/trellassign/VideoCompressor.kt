package com.example.trellassign

import android.content.Context
import android.util.Log
import com.arthenica.mobileffmpeg.Config
import com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL
import com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS
import com.arthenica.mobileffmpeg.FFmpeg
import com.blackbox.ffmpeg.examples.callback.FFMpegCallback
import java.io.File
import java.io.IOException


class VideoCompressor private constructor(private val context: Context) {

    private var bitRate: String?= null
    private var video: File? = null
    private var callback: FFMpegCallback? = null
    private var outputPath = ""
    private var outputFileName = ""

    fun setFile(originalFiles: File): VideoCompressor {
        this.video = originalFiles
        return this
    }

    fun setCallback(callback: FFMpegCallback): VideoCompressor {
        this.callback = callback
        return this
    }

    fun setOutputPath(output: String): VideoCompressor {
        this.outputPath = output
        return this
    }

    fun setOutputFileName(output: String): VideoCompressor {
        this.outputFileName = output
        return this
    }

    fun setBitRate(bitRate: String): VideoCompressor {
        this.bitRate = bitRate
        return this
    }
    fun compress() {

        if (video == null || !video!!.exists()) {
            callback!!.onFailure(IOException("File not exists"))
            return
        }
        if (!video!!.canRead()) {
            callback!!.onFailure(IOException("Can't read the file. Missing permission?"))
            return
        }
        if (bitRate.isNullOrEmpty()){
            callback!!.onFailure(IOException("BitRate is empty..."))
            return
        }

        val outputLocation = getConvertedFile(outputPath, outputFileName)

        val rc = FFmpeg.execute("-i ${video!!.path} -b:v ${bitRate}k ${outputLocation.path}")

        if (rc == RETURN_CODE_SUCCESS) {
            Log.i(Config.TAG, "Command execution completed successfully.");
            callback?.onSuccess(outputLocation)
        } else if (rc == RETURN_CODE_CANCEL) {
            Log.i(Config.TAG, "Command execution cancelled by user.");
            callback?.onFailure(IOException(""))
        } else {
            Log.i(Config.TAG, String.format("Command execution failed with rc=%d and the output below.", rc));
            callback?.onFailure(IOException(""))
            Config.printLastCommandOutput(Log.INFO);
        }

    }

    companion object {
        fun with(context: Context): VideoCompressor {
            return VideoCompressor(context)
        }
    }
    fun getConvertedFile(folder: String, fileName: String): File {
        val f = File(folder)

        if (!f.exists())
            f.mkdirs()

        return File(f.path + File.separator + fileName)
    }


}
