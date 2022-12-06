package com.example.cmsc436_nda

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cmsc436_nda.databinding.FragmentCBTMenuBinding

class CBTMenuFragment : Fragment() {
    private lateinit var binding: FragmentCBTMenuBinding
    private lateinit var viewModel: CBTViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCBTMenuBinding.inflate(inflater,container,false)
        binding.highScore.text = getString(R.string.cbt_hi_score).format(999)
        // GET HIGH SCORE ^^^
        if (viewModel.rounds.value!!.size > 0){
            binding.score.text = getString(R.string.cbt_score).format(viewModel.score.value)
            var temp = ""
            for (i in viewModel.rounds.value!!){
                temp += getString(R.string.cbt_report_line).format(i.first,i.second)
            }
            binding.report.text = getString(R.string.cbt_report)
        }
        binding.startButton.setOnClickListener{
            viewModel.mainActivity.showGame()
        }
        return binding.root
    }

    fun initViewModel(viewModel: CBTViewModel){
        this.viewModel = viewModel
    }
}