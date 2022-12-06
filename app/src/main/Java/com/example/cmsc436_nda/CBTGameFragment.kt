package com.example.cmsc436_nda

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.cmsc436_nda.databinding.FragmentCBTGameBinding
import kotlin.math.min
import kotlin.random.Random

class CBTGameFragment : Fragment() {
    private lateinit var binding: FragmentCBTGameBinding
    private lateinit var viewModel: CBTViewModel
    private val handler = Handler(Looper.getMainLooper());
    private lateinit var sequence: ArrayList<Int>
    private lateinit var buttons: Array<Button>
    private var start: Long? = null
    private var round: Int? = null
    private var fail = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCBTGameBinding.inflate(inflater, container, false)
        buttons = arrayOf(
            binding.button1, binding.button2, binding.button3,
            binding.button4, binding.button5, binding.button6,
            binding.button7, binding.button8, binding.button9)
        for (i in buttons)
            i.setBackgroundColor(Color.BLUE)
        startGame()
        return binding.root
    }

    fun initViewModel(viewModel: CBTViewModel){
        this.viewModel = viewModel
    }

    private fun startGame(){
        round = 0
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

    private fun round() {
        binding.round.text = getString(R.string.cbt_round).format(round)
        start = System.currentTimeMillis()
        createSequence(min(viewModel.score.value!! + 2, 9))
        buttons.forEachIndexed {i, button ->
            button.setOnClickListener {
                handleButton(i+1, button)
            }
        }
    }

    private fun handleButton(buttonNum: Int, button: Button){
        if (buttonNum == sequence.get(0)){
            sequence.removeFirst()
            if (sequence.size == 0){
                viewModel.addScore()
                viewModel.addRound(Pair(round!!, ((System.currentTimeMillis()- start!!)/1000.0f)))
                buttons.forEach {button ->
                    button.setOnClickListener(null)
                }
                round = round?.plus(1)
                round()
            }
        }
        else{
            sequence.clear()
            viewModel.addRound(Pair(round!!, ((System.currentTimeMillis()- start!!)/1000.0f)))
            buttons.forEach {button ->
                button.setOnClickListener(null)
            }
            if (!fail) {
                fail = true
                round()
            }
            else
                viewModel.mainActivity.showMenu()
        }
    }

    private fun createSequence(count: Int){
        val sequence = ArrayList<Int>()
        for (i in 1..count){
            val num = Random.nextInt(1, 9)
            sequence.add(num)
            val a = Runnable{
                buttons[num-1].setBackgroundColor(Color.RED)
                val b = Runnable {
                    buttons[num-1].setBackgroundColor(Color.BLUE)
                }
                handler.postDelayed(b, 500)
            }
            handler.postDelayed(a, (i*1000).toLong())
        }
        this.sequence = sequence
    }
}