package com.example.cmsc436_nda

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import com.example.cmsc436_nda.databinding.ActivityHomeBinding
import android.content.Intent
import android.util.Log
import android.view.View
import com.example.cmsc436_nda.com.example.cmsc436_nda.GoNoGoActivity

class HomeActivity:AppCompatActivity(),LifecycleOwner {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityHomeBinding.inflate(layoutInflater);
        binding.startGoNoGo.setOnClickListener { startGoNoGo() }
        binding.startCorsiBlockTapping.setOnClickListener{ startCorsiBlockTapping()}
        setContentView(binding.root);
    }

    private fun startGoNoGo(){
        val intent=Intent(this, GoNoGoActivity::class.java);
        startActivity(intent);
    }
    private fun startCorsiBlockTapping(){
        val intent = Intent(this, CorsiBlockTappingActivity::class.java);
        startActivity(intent);
    }



    companion object{
        private final const val ONE_SECOND=1000L
        private const val TAG="True Grinder";
    }
}