package com.example.cmsc436_nda

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.cmsc436_nda.databinding.ActivityCorsiBlockTappingBinding

class CorsiBlockTappingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCorsiBlockTappingBinding
    private lateinit var viewModel: CBTViewModel
    private lateinit var CBTGameInstance: CBTGameFragment
    private lateinit var CBTMenuInstance: CBTMenuFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCorsiBlockTappingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[CBTViewModel::class.java]
        viewModel.observeLifeCycle(this)

    }
}