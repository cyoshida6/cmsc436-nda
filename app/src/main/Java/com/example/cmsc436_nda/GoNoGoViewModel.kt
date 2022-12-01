package com.example.cmsc436_nda

import android.os.Looper
import android.os.Handler
import androidx.lifecycle.*
import java.util.*
class GoNoGoViewModel:ViewModel(), DefaultLifecycleObserver {
    private val random_generator = Random()
    var starting_color = random_generator.nextInt(0xFFFFFF);

    //how many colors to show before showing the starting color again
    private var reccurance = 0;
    private var colorTime = System.currentTimeMillis();
    private val handler = Handler(Looper.getMainLooper())
    private var m_reactionTime = MutableLiveData<Long>();
    private var m_points = MutableLiveData<Int>();
    private var m_t_interval = MutableLiveData<Long>();
    private var m_color = MutableLiveData<Int>();

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
        //switching colors every 1600 ms
        m_t_interval.value = ONE_SECOND - 400;
    }

    fun observeLifeCycle(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this);
    }

    //everytime score changes
    fun score(num: Int) {
        m_points.value = m_points.value!! + num;
        m_reactionTime.value = System.currentTimeMillis() - colorTime;
    }

    //changes color every (t_interval) milliseconds
    private val updater = object : Runnable {
        override fun run() {
            m_color.value = chooseColor();
            colorTime = System.currentTimeMillis();
            handler.postDelayed(this, t_interval.value!!);
        }

        fun stop() {
            handler.removeCallbacks(this);
        }
    }

    private fun chooseColor(): Int {
        if (reccurance == 0) {
            reccurance = random_generator.nextInt(MAX_RECCURANCE);
            return starting_color;
        } else {
            reccurance--;
            return random_generator.nextInt()
        }
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        updater.run();

    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        updater.stop();
    }

    companion object {
        private const val ONE_SECOND = 1000L;

        //The largest amount of colors that can pass
        // before the starting color is shown again
        private const val MAX_RECCURANCE = 10;
    }
}