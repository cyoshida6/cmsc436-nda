package com.example.cmsc436_nda

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.example.cmsc436_nda.databinding.ActivityTrailMakingBinding
import java.util.*
import kotlin.math.abs


class TrailMakingActivity: AppCompatActivity(), LifecycleOwner {
    private lateinit var binding: ActivityTrailMakingBinding;
    private lateinit var viewModel: TrailMakingViewModel

    private var handler= Handler(Looper.getMainLooper());
    private lateinit var touchListener:OnTouchListener;
    private val displayMetrics = DisplayMetrics()
    private var SCREEN_WIDTH=0;
    private var SCREEN_HEIGHT=0;
    private lateinit var bitmap:Bitmap;
    private lateinit var canvas:Canvas;
    private val randomGenerator=Random();
    private var circleCoords=ArrayList<Coordinate>();
    private lateinit var trailMakingInstance:TrailMakingGameFragment
    private lateinit var trailMakingMenuInstance:TrailMakingMenuFragment
    private lateinit var textOne:TextView;
    private lateinit var textTwo:TextView;
    private lateinit var textThree:TextView;
    private lateinit var textFour:TextView;
    private lateinit var textFive:TextView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        //creating viewModel
        viewModel= ViewModelProvider(this)[TrailMakingViewModel::class.java];
        viewModel.observeLifeCycle(this);
        viewModel.getInstanceOf(this);
        viewModel.SCREEN_WIDTH=displayMetrics.widthPixels
        viewModel.SCREEN_HEIGHT=displayMetrics.heightPixels-75
        //
        trailMakingMenuInstance= TrailMakingMenuFragment()
        trailMakingMenuInstance.initVM(viewModel);
        //trailMakingInstance.initParent(this);
        //adding starting page to main_frame
        supportFragmentManager.beginTransaction()
            .add(R.id.main_trail_frame,trailMakingMenuInstance)
            .commitNow()
        binding= ActivityTrailMakingBinding.inflate(layoutInflater);
        //drawing map and attaching it to image view

        setContentView(binding.root);
    }

    fun runGame(){
        showGame()
        val endGame = object : Runnable {
            override fun run() {
                showMenu();
            }
        }
        handler.postDelayed(endGame, GAME_RUNTIME);
    }

    private fun showGame(){
        trailMakingInstance=TrailMakingGameFragment()
        trailMakingInstance.initVM(viewModel);
        trailMakingInstance.initParent(this);
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_trail_frame,trailMakingInstance)
            .commitNow();
    }
    private fun showMenu(){
        trailMakingMenuInstance=TrailMakingMenuFragment()
        trailMakingMenuInstance.initVM(viewModel);
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_trail_frame,trailMakingMenuInstance)
            .commitNow();
    }

companion object{
    private val ONE_SECOND=1000L
    private val GAME_RUNTIME=ONE_SECOND*20;
}
}
