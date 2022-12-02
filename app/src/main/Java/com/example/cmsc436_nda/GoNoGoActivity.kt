package com.example.cmsc436_nda.com.example.cmsc436_nda

import com.example.cmsc436_nda.GoNoGoViewModel
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.example.cmsc436_nda.ColorGameFragment
import com.example.cmsc436_nda.ColorMenuFragment
import com.example.cmsc436_nda.R
import com.example.cmsc436_nda.databinding.ActivityGoNoGoBinding


class GoNoGoActivity: AppCompatActivity(),LifecycleOwner {
    private lateinit var binding: ActivityGoNoGoBinding;
    private lateinit var viewModel: GoNoGoViewModel
    private lateinit var colorGameInstance: ColorGameFragment
    private lateinit var colorMenuInstance: ColorMenuFragment
    private var handler=Handler(Looper.getMainLooper());

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //
        binding= ActivityGoNoGoBinding.inflate(layoutInflater);
        setContentView(binding.root);
        /*All variables related to the function of the app are updated and stored in the viewModel*/
        viewModel= ViewModelProvider(this)[GoNoGoViewModel::class.java];
        viewModel.observeLifeCycle(this);
        viewModel.getInstanceOf(this);
        viewModel.init();
        //This is the page where stats and the ability to play the game will be
        colorMenuInstance= ColorMenuFragment()
        colorMenuInstance.initVM(viewModel);
        //adding starting page to main_frame
        supportFragmentManager.beginTransaction()
            .add(R.id.main_frame,colorMenuInstance)
            .commitNow()

    }
    //sets 10 second timer where go no go activity is running
     fun startGame(){
        //set game fragment as in main layout
        showGame();
        //This is the operation to end the game which will be delayed by 10 seconds
         val runEndGame = object : Runnable {
            override fun run() {
               colorGameInstance.gameEnd();
               showMenu()
            }
        }
        //posting delayed signal to end game for 10 seconds
        handler.postDelayed(runEndGame,GoNoGoViewModel.PRE_GAME_WAIT+GoNoGoViewModel.RUN_TIME);
    }
    private fun showMenu(){
        colorMenuInstance=ColorMenuFragment();
        colorMenuInstance.initVM(viewModel);
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frame,colorMenuInstance)
            .commitNow();
    }
    private fun showGame(){
        colorGameInstance=ColorGameFragment();
        colorGameInstance.initVM(viewModel);
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frame,colorGameInstance)
            .commitNow();
    }
}