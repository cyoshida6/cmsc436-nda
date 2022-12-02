package com.example.cmsc436_nda

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cmsc436_nda.databinding.FragmentColorGameBinding

class ColorGameFragment:Fragment() {
    private lateinit var binding: FragmentColorGameBinding;
    private val handler= Handler(Looper.getMainLooper());
    private lateinit var viewModel: GoNoGoViewModel;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=FragmentColorGameBinding.inflate(inflater,container,false);
        startGame()
        return binding.root;
    }

     fun initVM(viewModel: GoNoGoViewModel){
     this.viewModel=viewModel;
    }

    private fun startGame(){
        //initializes viewModel properties (point, changing colors, time intervals between color changes )
        viewModel.init();
        binding.ColorView.setBackgroundColor(-viewModel.starting_color);
        val start =object:Runnable{
            override fun run() {
                viewModel.startUpdater();
                //Get rid of starting text view ("Whenever you see this color click on it.")
                binding.textView.visibility= View.GONE;
                //events to occur when color is clicked on
                binding.ColorView.setOnClickListener{onClick() };
                //change background color when color variable is changed
                observeColorChange();
                //display users reaction-time to click on color every time reaction time variable changes
                observeReactTimeChange();
                //display points user has every time points changes
                observePointsChange();
                //change the background color
                binding.ColorView.setBackgroundColor(-viewModel.color.value!!)
            }
        }
        handler.postDelayed(start, GoNoGoViewModel.PRE_GAME_WAIT);

    }

    private fun onClick(){
        //Every click check if the displayed color is the color the user is supposed to click on
        if(viewModel.color.value==viewModel.starting_color){
            //Raise score by 1
            viewModel.score(1);
        }else{
            //Lower score by 1
            viewModel.score(-1);
        }

    }
    private fun observeReactTimeChange(){
        viewModel.reactionTime.observe(this){
            binding.ReactionTime.text="Reaction Time: "+viewModel.reactionTime.value!!.toString()

        }
    }
    private fun observePointsChange(){
        viewModel.points.observe(this){
            var points =viewModel.points.value!!
            //adding the raction time of correct color picks
            if(points>0){
                viewModel.add_reaction_time(viewModel.reactionTime.value!!.toInt())
                viewModel.changeTimeInterval(ACCELERATION);
            }else{
                viewModel.changeTimeInterval(-ACCELERATION);
            }
            //change points text to points
            binding.Points.text="Points: "+points.toString();
        }
    }
    private fun observeColorChange(){
        viewModel.color.observe(this){
            binding.ColorView.setBackgroundColor(-viewModel.color.value!!);
        }
    }


     fun gameEnd(){
        //Stop the cycle of colors from going.
        viewModel.stopUpdater();
    }

    companion object{

        private const val ACCELERATION=120;
        val TAG="GoNoGo Activity log(line 85)";
    }
}