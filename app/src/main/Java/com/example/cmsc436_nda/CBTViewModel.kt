package com.example.cmsc436_nda

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel

class CBTViewModel : ViewModel(), DefaultLifecycleObserver {
    fun observeLifeCycle(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this)
    }
}