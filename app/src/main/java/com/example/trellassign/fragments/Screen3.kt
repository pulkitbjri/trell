package com.example.trellassign.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels

import com.example.trellassign.R
import com.example.trellassign.SharedViewModel
import com.example.trellassign.databinding.FragmentScreen2Binding
import com.example.trellassign.databinding.FragmentScreen3Binding
import kotlinx.android.synthetic.main.fragment_screen2.*

class Screen3 : Fragment() {
    private val viewmodel: SharedViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentScreen3Binding = DataBindingUtil.inflate(inflater, R.layout.fragment_screen3, container, false)
        binding.vm=viewmodel
        val view: View = binding.getRoot()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setVideoView()
    }
    private fun setVideoView() {
        val conrtoller = MediaController(context)
        conrtoller.setAnchorView(videoView)
        videoView.setMediaController(conrtoller)
        videoView.setVideoPath(viewmodel.convertedFilePath.value?.path)
        videoView.start()
    }
    companion object {
        @JvmStatic
        fun newInstance() =
            Screen3().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
