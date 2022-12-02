package com.example.cmsc436_nda

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cmsc436_nda.databinding.FragmentColorGameBinding
import com.example.cmsc436_nda.databinding.FragmentColorMenuBinding

class ColorMenuFragment: Fragment() {
    private lateinit var binding: FragmentColorMenuBinding;
    private lateinit var viewModel: GoNoGoViewModel;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=FragmentColorMenuBinding.inflate(inflater,container,false);
        binding.startButton.setOnClickListener {
            viewModel.mainActivity.startGame();
        }
        binding.Points.text="Points: "+viewModel.points.value.toString();
        binding.ReactionTime.text="Average Reaction Time: "+viewModel.calc_avg_reaction_time().toString();
        return binding.root;
    }
    fun initVM(viewModel:GoNoGoViewModel){
        this.viewModel=viewModel

    }
}