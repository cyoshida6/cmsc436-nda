package com.example.cmsc436_nda

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.cmsc436_nda.databinding.FragmentCBTGameBinding
import java.util.concurrent.TimeUnit
import kotlin.math.min
import kotlin.random.Random

class CBTGameFragment : Fragment() {
    private lateinit var binding: FragmentCBTGameBinding
    private lateinit var viewModel: CBTViewModel
    private val handler = Handler(Looper.getMainLooper());
    private lateinit var sequence: ArrayList<Int>
    private lateinit var buttons: Array<Button>
    private var start: Long? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCBTGameBinding.inflate(inflater, container, false)
        buttons = arrayOf(
            binding.button1, binding.button2, binding.button3,
            binding.button4, binding.button5, binding.button6,
            binding.button7, binding.button8, binding.button9)
        startGame()
        return binding.root
    }

    fun initViewModel(viewModel: CBTViewModel){
        this.viewModel = viewModel
    }

    private fun startGame(){
        viewModel.init()
        val start = Runnable {
            binding.round.text = getString(R.string.cbt_round).format(viewModel.score.value)
            round()
//            if (viewModel.score.value > HIGH SCORE){
//                UPDATE HIGH SCORE
//            }
//            viewModel.mainActivity.showMenu()
        }
        handler.postDelayed(start, 1000)
    }

    private fun round(): Boolean {
        val score = viewModel.score.value
        start = System.currentTimeMillis()
        createSequence(min(viewModel.score.value!! + 2, 9))
        buttons.forEachIndexed {i, button ->
            button.setOnClickListener {
                handleButton(i+1, button)
            }
        }
        Log.w("PPOOPOPOPPOPO", sequence.toString())



//        waitUntil(sequence.size > 0){ }
        viewModel.addRound(Pair(viewModel.score.value!!, ((System.currentTimeMillis()- start!!)/1000.0f)))
        buttons.forEach {button ->
            button.setOnClickListener(null)
        }
        binding.round.text = getString(R.string.cbt_round).format(viewModel.score.value)
//        if (viewModel.score.value == score) return false
        return true
    }

    private fun handleButton(buttonNum: Int, button: Button){
        if (buttonNum == sequence.get(0)){
            sequence.removeFirst()
            if (sequence.size == 0){
                viewModel.addScore()
                viewModel.addRound(Pair(viewModel.score.value!!, ((System.currentTimeMillis()- start!!)/1000.0f)))
                buttons.forEach {button ->
                    button.setOnClickListener(null)
                }
                binding.round.text = getString(R.string.cbt_round).format(viewModel.score.value)
            }
        }
        else{
            sequence.clear()
            viewModel.addRound(Pair(viewModel.score.value!!, ((System.currentTimeMillis()- start!!)/1000.0f)))
            buttons.forEach {button ->
                button.setOnClickListener(null)
            }
            binding.round.text = getString(R.string.cbt_round).format(viewModel.score.value)
        }
    }

    private fun createSequence(count: Int){
        val sequence = ArrayList<Int>()
        for (i in 1..count){
            val num = Random.nextInt(1,9)
            sequence.add(num)
            //////
        }
        this.sequence = sequence
    }
}