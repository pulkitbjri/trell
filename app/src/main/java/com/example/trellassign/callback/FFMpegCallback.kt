package com.blackbox.ffmpeg.examples.callback

import java.io.File

interface FFMpegCallback {


    fun onSuccess(convertedFile: File)

    fun onFailure(error: Exception)


}