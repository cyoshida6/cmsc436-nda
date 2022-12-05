package com.example.cmsc436_nda

import android.os.Looper
import android.os.Handler
import androidx.lifecycle.*
import com.example.cmsc436_nda.com.example.cmsc436_nda.GoNoGoActivity
import java.util.*
class GoNoGoViewModel:ViewModel(), DefaultLifecycleObserver {
    lateinit var mainActivity: GoNoGoActivity;
    private val random_generator = Random()
    var starting_color = random_generator.nextInt(0xFFFFFF);
    //private var startTime=System.currentTimeMillis();
    private var reactions= arrayOf(-1,-1,-1,-1,-1,-1,-1,-1,-1,-1);
    private var num_of_reactions=0;
    private var avgReactionTime=0;
    //how many colors to show before showing the starting color again
    private var reccurance = 0;
    private var colorTime = System.currentTimeMillis();
    private val handler = Handler(Looper.getMainLooper())

    //Mutable Live Data in View Model
    private var m_reactionTime = MutableLiveData<Long>();
    private var m_points = MutableLiveData<Int>();
    private var m_t_interval = MutableLiveData<Long>();
    private var m_color = MutableLiveData<Int>();
    //Associated Live Data in ViewModel
    internal val reactionTime: LiveData<Long>
        get() = m_reactionTime
    internal val color: LiveData<Int>
        get() = m_color;
    internal val points: LiveData<Int>
        get() = m_points;
    internal val t_interval: LiveData<Long>
        get() = m_t_interval

    //initialize the properties of viewModel and listen for
//changes in Lifecycle
    fun init() {
        m_points.value = 0;
        m_color.value = random_generator.nextInt(0xFFFFFF);
        //switching colors every 600 ms
        m_t_interval.value = ONE_SECOND - 400;
    }
    //observe the liefcycle owner's life cycle
    fun observeLifeCycle(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this);
    }
    fun getInstanceOf(gng: GoNoGoActivity){
        this.mainActivity=gng;
    }

    //everytime score changes
    fun score(num: Int) {
        m_reactionTime.value = System.currentTimeMillis() - colorTime;
        m_points.value = m_points.value!! + num;
    }
    fun changeTimeInterval(change:Int){
        m_t_interval.value = m_t_interval.value?.minus(change);
    }
    //changes color every (t_interval) milliseconds
    private val updater = object : Runnable {
        override fun run() {
            m_color.value = chooseColor(random_generator.nextInt(3));
            colorTime = System.currentTimeMillis();
            //accounting for time passed since every color has been shown
            handler.postDelayed(this, t_interval.value!!);
        }

        fun stop() {
            handler.removeCallbacks(this);
        }
    }
    //chooses whether the next color to show will be the starting color
    private fun chooseColor(baseColor:Int): Int{

        if (reccurance == 0) {
            reccurance = random_generator.nextInt(MAX_RECCURANCE);
            return starting_color;
        } else {
           var base:Int= when(baseColor){
                0->0xFF0000;
                1->0x00FF00;
                2->0x0000FF;
               else->0xFFFFFF;
            }
            reccurance--;
            return base+(600*random_generator.nextInt(5));
        }
    }

    //stop cycling through all the colors
    fun stopUpdater(){
        updater.stop();
    }
    fun startUpdater(){
        updater.run();
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        updater.stop();
    }

    fun add_reaction_time(reactTime: Int){
        reactions[((num_of_reactions++)%10)]=reactTime;
    }
     fun calc_avg_reaction_time():Int{
            if(reactions[0]==-1){
                return -1;
            }else{
                num_of_reactions=0
                avgReactionTime=0;
                while(reactions[num_of_reactions]!=-1){
                    avgReactionTime+=reactions[num_of_reactions++];
                }
                avgReactionTime/=num_of_reactions
                return avgReactionTime;
            }
    }


    companion object {
         const val ONE_SECOND = 1000L;

        //The largest amount of colors that can pass
        // before the starting color is shown again
        private const val MAX_RECCURANCE = 10;
        const val RUN_TIME= ONE_SECOND *25L;
        const val PRE_GAME_WAIT= ONE_SECOND*4L;
        const val AVERAGE_PERSON_REACTION=600L;
    }
}