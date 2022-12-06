package com.example.cmsc436_nda

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cmsc436_nda.databinding.ActivityTrailMakingBinding
import com.example.cmsc436_nda.databinding.FragmentColorGameBinding
import com.example.cmsc436_nda.databinding.FragmentTrailmakingGameBinding
import java.util.*
import kotlin.math.abs

class TrailMakingGameFragment: Fragment() {
    private lateinit var binding:FragmentTrailmakingGameBinding
    private lateinit var viewModel:TrailMakingViewModel
    private var handler= Handler(Looper.getMainLooper());
    private lateinit var touchListener: View.OnTouchListener;
    private lateinit var bitmap:Bitmap
    private val displayMetrics = DisplayMetrics()


    private lateinit var canvas: Canvas;
    private val randomGenerator=Random();
    private var circleCoords=ArrayList<Coordinate>();
    private lateinit var textOne:TextView;
    private lateinit var textTwo:TextView;
    private lateinit var textThree:TextView;
    private lateinit var textFour:TextView;
    private lateinit var textFive:TextView;
    private lateinit var parentContext:TrailMakingActivity;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding=FragmentTrailmakingGameBinding.inflate(inflater,container,false)
        initTouchObject()
        viewModel.init()
        bitmap=Bitmap.createBitmap(viewModel.SCREEN_WIDTH, viewModel.SCREEN_HEIGHT, Bitmap.Config.ARGB_8888)
        canvas=Canvas(bitmap);
        startGame()
        return binding.root
    }
     fun initVM(vm:TrailMakingViewModel){
     this.viewModel=vm;
    }
    fun initParent(TM:TrailMakingActivity){
        parentContext=TM;
    }
    private fun startGame(){

        //canvas=Canvas(bitmap)
        initTouchObject()
        val startGame = object : Runnable {
            override fun run() {
                binding.startingCard.visibility=View.GONE
                drawMap();
                binding.imageV.background = BitmapDrawable(getResources(), bitmap)
                binding.imageV.setOnTouchListener(touchListener)
                binding.imageV.invalidate()
                viewModel.startGame(System.currentTimeMillis());
            }
        }
        handler.postDelayed(startGame, ONE_SECOND);
    }
     private fun initTouchObject (){
        touchListener=object : View.OnTouchListener {

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                var eventX=event.getX(event.actionIndex);
                var eventY=event.getY(event.actionIndex);
                var circleX=circleCoords[0].x
                var circleY=circleCoords[0].y
                var diffX=0;
                var diffY=0;
                v.performClick()

                when (event.actionMasked) {

                    // Show new MarkerView.
                    MotionEvent.ACTION_DOWN,
                    MotionEvent.ACTION_POINTER_DOWN -> {
                        if(circleCoords.size>1){
                            // if there are circles on the screen
                            circleX=circleCoords[0].x
                            circleY=circleCoords[0].y
                            diffX= abs(circleX-eventX.toInt());
                            diffY= abs(circleY-eventY.toInt());
                            if(diffX< (CIRCLE_SIZE)&&diffY< (CIRCLE_SIZE)){
                                makeCircle(circleX,circleY, CIRCLE_SIZE, SECOND_COLOR)
                                viewModel.m_streak.value=1;

                                binding.imageV.invalidate()
                            }
                        }

                    }

                    // Remove one MarkerView.
                    MotionEvent.ACTION_UP,
                    MotionEvent.ACTION_POINTER_UP -> {
                        if(circleCoords.size>1) {
                            //if there are cicrcles in the screen
                            viewModel.m_streak.value = 0;
                            refreshMap();
                        }

                    }

                    // Move all currently active MarkerViews.
                    MotionEvent.ACTION_MOVE -> {
                        if(viewModel.m_streak.value!!<MAX_CIRCLES) {
                            circleX = circleCoords[viewModel.m_streak.value!!].x
                            circleY = circleCoords[viewModel.m_streak.value!!].y

                            diffX= abs(circleX-eventX.toInt());
                            diffY= abs(circleY-eventY.toInt());
                            if(diffX< (CIRCLE_SIZE)&&diffY< (CIRCLE_SIZE)){
                                makeCircle(circleX,circleY, CIRCLE_SIZE, SECOND_COLOR)
                                viewModel.m_streak.value=viewModel.m_streak.value!!+1;
                                binding.imageV.invalidate()
                            }
                        }else{
                            viewModel.storeCompletionTime(System.currentTimeMillis())
                            viewModel.m_streak.value = 0;
                            refreshMap();
                        }
                    }

                    else -> Log.e(TAG, "unhandled action")
                }
                return true;
            }
        }
    }

    private fun makeCircle(x:Int,y:Int,size:Int,color:String){
        // draw rectangle shape to canvas
        val shapeDrawable: ShapeDrawable = ShapeDrawable(OvalShape())
        shapeDrawable.setBounds(x, y, (x+size), (y+size))
        shapeDrawable.getPaint().setColor(Color.parseColor(color));
        shapeDrawable.draw(canvas)
    }
    private fun drawMap(){
        var cord:Coordinate;
       circleCoords.clear()
        for(i in 1..MAX_CIRCLES) {
            cord=produceCord();
            circleCoords.add(cord);
            makeCircle(cord.x, cord.y, CIRCLE_SIZE, MAIN_COLOR);
        }

        makeNums();
        binding.imageV.invalidate();
    }
    private fun produceCord():Coordinate{

        var valid=false;
        var overlapFound=false;
        var x:Int=0;
        var y:Int=0;
        while(!valid) {
            overlapFound=false;
            x= Random().nextInt(viewModel.SCREEN_WIDTH-250);
            y= Random().nextInt(viewModel.SCREEN_HEIGHT- 250);
            if(circleCoords.size!=0){
                for(i in 1..circleCoords.size){
                    var existingX=circleCoords[i-1].x
                    var existingY=circleCoords[i-1].y;
                    var xDiff= abs(x-existingX);
                    var yDiff= abs(y-existingY);
                    if(xDiff< CIRCLE_DISTANCE&&yDiff< CIRCLE_DISTANCE){
                        Log.e(TAG,circleCoords.size.toString());
                        overlapFound=true;
                    }
                }
                if(!overlapFound){
                    valid=true
                }
            }else{
                valid =true
            }
        }

        return Coordinate(x,y);
    }

    private fun refreshMap(){
        //getting rid of circle and numbers on bitmap already
        bitmap.eraseColor(Color.parseColor("#001E2C"))
        destroyNums()
        //
        drawMap();
    }
    private fun destroyNums(){
        binding.root.removeView(textOne);
        binding.root.removeView(textTwo);
        binding.root.removeView(textThree);
        binding.root.removeView(textFour);
        binding.root.removeView(textFive);

    }
    private fun makeNums(){
        textOne=makeTextView("1",0);
        binding.root.addView(textOne);
        textTwo=makeTextView("2",1);
        binding.root.addView(textTwo)
        textThree=makeTextView("3",2);
        binding.root.addView(textThree)
        textFour=makeTextView("4",3);
        binding.root.addView(textFour)
        textFive=makeTextView("5",4);
        binding.root.addView(textFive)
    }
    private fun makeTextView(text:String,circle:Int): TextView {
        var textView= TextView(parentContext);
        textView.text = text;
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,28f);
        textView.setTextColor(Color.RED);
        textView.x = add(circleCoords[circle].x,-(CIRCLE_SIZE/2));
        textView.y = add(circleCoords[circle].y,-(CIRCLE_SIZE)/2);

        return textView
    }
    private fun add(x:Int,y:Int):Float{
        return (x+y).toFloat()
    }
    companion object{
        private val TAG="Testing from TrailMaking Activity"
        private val CIRCLE_SIZE=50;
        private val MAX_CIRCLES=5;
        private val MAIN_COLOR="#FFFFFF";
        private val SECOND_COLOR="#FFFF00"
        private val CIRCLE_DISTANCE=CIRCLE_SIZE
        private val ONE_SECOND=1000L;
    }
}