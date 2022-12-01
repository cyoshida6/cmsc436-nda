package com.example.cmsc436_nda.com.example.cmsc436_nda

import com.example.cmsc436_nda.GoNoGoViewModel
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.example.cmsc436_nda.databinding.ActivityGoNoGoBinding


class GoNoGoActivity: AppCompatActivity(),LifecycleOwner {
    private lateinit var binding: ActivityGoNoGoBinding;
    private lateinit var viewModel: GoNoGoViewModel
    private lateinit var updater:Runnable
    private val handler= Handler(Looper.getMainLooper());
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityGoNoGoBinding.inflate(layoutInflater);
        /*All variables related to the function of the app are updated and stored in the viewModel*/
        viewModel= ViewModelProvider(this)[GoNoGoViewModel::class.java];
        //set starting color to be shown
        binding.ColorView.setBackgroundColor(-viewModel.starting_color);
        setContentView(binding.root);
        //Initializes loop that starts changing background colors
        startGame();

    }
    private fun startGame(){
        //initializes viewModel properties (point, changing colors, time intervals between color changes )
        viewModel.init();
        //setting callbacks in activity life-cycle for viewModel to
        viewModel.observeLifeCycle(this);
        val start =object:Runnable{
            override fun run() {
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
        handler.postDelayed(start, ONE_SECOND *3L)

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
            binding.Points.text="Points: "+viewModel.points.value!!.toString()
        }
    }
    private fun observeColorChange(){
        viewModel.color.observe(this){
            binding.ColorView.setBackgroundColor(-viewModel.color.value!!);
        }
    }



    companion object{
        private final const val ONE_SECOND=1000L
        val TAG="GoNoGo Activity log(line 85)";
    }
}