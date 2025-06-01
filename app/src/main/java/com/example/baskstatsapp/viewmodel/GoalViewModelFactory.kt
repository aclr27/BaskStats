package com.example.baskstatsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.baskstatsapp.dao.GoalDao

class GoalViewModelFactory(private val goalDao: GoalDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GoalViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GoalViewModel(goalDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}