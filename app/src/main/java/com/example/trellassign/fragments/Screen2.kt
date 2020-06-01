package com.example.trellassign.fragments

import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.trellassign.R
import com.example.trellassign.SharedViewModel
import com.example.trellassign.databinding.FragmentScreen2Binding
import kotlinx.android.synthetic.main.fragment_screen2.*


/**
 * A simple [Fragment] subclass.
 */
class Screen2 : Fragment() {
    private val viewmodel: SharedViewModel by activityViewModels()
    lateinit var binding: FragmentScreen2Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         binding = DataBindingUtil.inflate(inflater, R.layout.fragment_screen2, container, false)
        binding.vm=viewmodel
        binding.frag=this
        val view: View = binding.getRoot()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setVideoView()
        observeProgress()
    }

    private fun observeProgress() {
        viewmodel.progressLiveData.observe(viewLifecycleOwner, Observer {
            if (it)
                progressLay.visibility=View.VISIBLE
            else
                progressLay.visibility=View.GONE

        })
    }

    private fun setVideoView() {
        videoView.setVideoURI(viewmodel.getFilePathUri())
        videoView.start()
    }
    fun onCompressClick(view: View){
        val currentBitRate = getBitRate(viewmodel.getFilePathUri())/1000
        if (binding.bitRate.text.isEmpty())
        {
            Toast.makeText(requireContext(),"put bitrate",Toast.LENGTH_LONG).show()
            return
        }
        else if (binding.bitRate.text.toString().toInt() > currentBitRate)
        {
            Toast.makeText(requireContext(),"put bitrate less than $currentBitRate",Toast.LENGTH_LONG).show()
            return
        }
        viewmodel.compressVideo(requireContext().applicationContext.filesDir.absolutePath,
            binding.bitRate.text.toString()
        )
        binding.compress.isEnabled=false
        observeCompressopn()
    }

    private fun observeCompressopn() {
        viewmodel.compressionStatus.observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(),it,Toast.LENGTH_LONG).show()
        })
    }

    fun getBitRate(url: Uri?): Int {
        val mmr = MediaMetadataRetriever()
        return try {
            mmr.setDataSource(requireContext(),url)
            mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE).toInt()
        } catch (e: NumberFormatException) {
            0
        } finally {
            mmr.release()
        }
    }
    companion object {
        @JvmStatic
        fun newInstance() =
            Screen2().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
