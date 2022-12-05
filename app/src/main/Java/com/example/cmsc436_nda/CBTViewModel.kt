package com.example.cmsc436_nda

import androidx.lifecycle.*

class CBTViewModel : ViewModel(), DefaultLifecycleObserver {
    lateinit var mainActivity: CorsiBlockTappingActivity
    private var _rounds = MutableLiveData<ArrayList<Pair<Int,Float>>>()
    internal val rounds: LiveData<ArrayList<Pair<Int,Float>>>
        get() = _rounds
    private var _score = MutableLiveData<Int>()
    internal val score: LiveData<Int>
        get() = _score

    fun observeLifeCycle(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this)
    }
    fun getInstanceOf(activity: CorsiBlockTappingActivity) {
        mainActivity = activity
    }
    fun init() {
        _rounds.value = ArrayList()
        _score.value = 0
    }
    fun addScore(){
        _score.value = _score.value?.plus(1)
    }
    fun addRound(pair:Pair<Int,Float>){
        _rounds.value?.add(pair)
    }
}