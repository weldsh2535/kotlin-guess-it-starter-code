package com.example.android.guesstheword.screens.score

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ScoreViewModelFactory(private val finalScore:Int,private val wordSize:String): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScoreViewModel::class.java)){
            return ScoreViewModel(finalScore,wordSize) as T
        }
        throw IllegalArgumentException("Unkown ViewModel class")
    }
}