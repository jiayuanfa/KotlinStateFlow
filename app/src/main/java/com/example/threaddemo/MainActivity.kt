package com.example.threaddemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.threaddemo.ui.theme.ThreadDemoTheme
import org.w3c.dom.Text

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ThreadDemoTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    GreetingScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun GreetingScreen(viewModel: MainViewModel,modifier: Modifier = Modifier) {

    // 此代码的意思是通过订阅上游的StateFlow，并将其当前的值转换成Compose运行时能够直接识别的State对象。这是触发UI更新的关键
    // by 是 kotlin的委托性语法，它意味着uiState这个变量的值将委托给collectAsState()返回的State对象来管理。
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (val currentState = uiState) {
          is UiState.Idle -> {
              Text(text = "准备就绪，点击按钮加载数据")
          }
            is UiState.Loading -> {
                CircularProgressIndicator()
                Text(text = "拼命加载中...", modifier = Modifier.padding(top = 16.dp))
            }
            is UiState.Success -> {
                Text(text = currentState.data, style = MaterialTheme.typography.headlineSmall)
            }
            is UiState.Error -> {
                Text(text = "出错啦：${currentState.message}", color = MaterialTheme.colorScheme.error)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                viewModel.fetchData()
            },
            enabled = uiState !is UiState.Loading
        ) {
            Text(text = "模拟网络请求")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ThreadDemoTheme {
        GreetingScreen(viewModel = MainViewModel())
    }
}