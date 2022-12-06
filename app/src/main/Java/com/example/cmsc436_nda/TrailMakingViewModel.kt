package com.example.cmsc436_nda

import android.graphics.Bitmap
import android.util.DisplayMetrics
import androidx.lifecycle.*

class TrailMakingViewModel: ViewModel(), DefaultLifecycleObserver {
    lateinit var mainActivity: TrailMakingActivity;
    private var streakStartTime=System.currentTimeMillis()
    private var completionTime=ArrayList<Long>();
    var SCREEN_WIDTH:Int=0;
    var SCREEN_HEIGHT:Int=0;
    lateinit var  bitmap:Bitmap;
    var m_streak=MutableLiveData<Int>();
    val streak:LiveData<Int>
        get()=m_streak

     fun startGame(start:Long){
        streakStartTime=start;
    }
     fun storeCompletionTime(complete:Long){
        completionTime.add(streakStartTime-complete);
        streakStartTime=complete;
    }
     fun avgComepletionTime():Long{
        var average:Long=0L;
        if(completionTime.size<1){
            return -1L;
        }else{
            for(i in 1..completionTime.size){
                average+=completionTime[i-1];
            }
            return (average/completionTime.size)
        }
    }


    fun init() {
        m_streak.value=0;

    }

    fun observeLifeCycle(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this);
    }

    fun getInstanceOf(activity: TrailMakingActivity){
        this.mainActivity= activity;
    }

    fun createCircles() {

    }
}