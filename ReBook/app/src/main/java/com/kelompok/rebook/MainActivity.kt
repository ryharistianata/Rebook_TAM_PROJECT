package com.kelompok.rebook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.kelompok.rebook.ui.navigation.ReBookNavGraph
import com.kelompok.rebook.ui.theme.ReBookTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isDarkTheme by remember { mutableStateOf(false) }

            ReBookTheme(darkTheme = isDarkTheme, dynamicColor = false) {
                ReBookNavGraph(
                    isDarkTheme = isDarkTheme,
                    onThemeToggle = { isDarkTheme = !isDarkTheme }
                )
            }
        }
    }
}