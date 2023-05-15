package com.kadun.kadchat.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kadun.kadchat.data.repositories.SubredditsRepositories
import com.kadun.kadchat.data.utils.onFailure
import com.kadun.kadchat.data.utils.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val subredditsRepo: SubredditsRepositories) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    init {
        getNewSubreddits()
    }

    private fun getNewSubreddits() = viewModelScope.launch(Dispatchers.IO) {
        subredditsRepo.getNewSubreddits()
            .onSuccess {
                Log.e("tag", "onSuccess $it")
            }
            .onFailure {
                Log.e("tag", "onFailure $it")
            }
    }
}