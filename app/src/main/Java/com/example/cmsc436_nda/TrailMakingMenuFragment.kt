package com.example.cmsc436_nda

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape

import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue

import android.view.MotionEvent

import androidx.lifecycle.ViewModelProvider
import com.example.cmsc436_nda.databinding.ActivityTrailMakingBinding
import com.example.cmsc436_nda.databinding.FragmentColorGameBinding
import com.example.cmsc436_nda.databinding.FragmentTrailmakingGameBinding
import com.example.cmsc436_nda.databinding.FragmentTrailmakingMenuBinding
import java.util.*
import kotlin.math.abs

class TrailMakingMenuFragment:Fragment() {
    private lateinit var binding: FragmentTrailmakingMenuBinding
    private lateinit var viewModel:TrailMakingViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentTrailmakingMenuBinding.inflate(inflater,container,false)
        binding.startButton.setOnClickListener { viewModel.mainActivity.runGame() }
        return binding.root
    }
    fun initVM(vm:TrailMakingViewModel){
        this.viewModel=vm;
    }
}