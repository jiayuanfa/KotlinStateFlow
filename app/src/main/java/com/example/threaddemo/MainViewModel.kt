package com.example.threaddemo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)

    // StateFlow是Kotlin协程库中一个专门用于管理状态的响应式数据流组件。可以把它理解为一个始终持有且仅持有最新状态值的智能公告栏，任何对其感兴趣的人（订阅者）都能立即看到当前的最新信息，并能追踪后续的变更
    // StateFlow是构建在Flow基础之上的一个特殊实现。
    // 简单来说，Flow是数据流的基础工具，而StateFlow是专门为管理状态这个特定任务优化过的专用工具
    val uiState: StateFlow<UiState> = _uiState

    fun fetchData() {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val simulateData = withContext(Dispatchers.IO) {
                    delay(2000)
                    "数据加载成功!"
                }
                _uiState.value = UiState.Success(simulateData)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message?:"未知错误")
            }
        }
    }
}

sealed class UiState {
    data object Idle : UiState()
    data object Loading : UiState()
    data class Success(val data : String) : UiState()
    data class Error(val message: String) : UiState()
}