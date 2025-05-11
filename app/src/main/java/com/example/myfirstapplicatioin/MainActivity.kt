package com.example.myfirstapplicatioin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.example.myfirstapplicatioin.blocks.VariableBlock

import com.example.myfirstapplicatioin.ui.theme.MyFirstApplicatioinTheme
import com.example.myfirstapplicatioin.viewBlocks.DrawConnection
import com.example.myfirstapplicatioin.viewBlocks.ViewVariableBlock

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyFirstApplicatioinTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    content = { paddingValues ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)
                        ) {
                            val variableBlock = VariableBlock("value")
                            val view = ViewVariableBlock(variableBlock, 100.dp ,100.dp)
                            val viewer = DrawConnection();
                            view.startView(); // нужно еще во viewer закинуть все переменные
                        }
                    }
                )
            }
        }
    }
}


