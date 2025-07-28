package com.oneplus.redcableclub

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.Modifier
import com.oneplus.redcableclub.navigation.NavigationRoot
import com.oneplus.redcableclub.ui.theme.RedCableClubTheme


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            RedCableClubTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val windowSize = calculateWindowSizeClass(activity = this)
                   NavigationRoot(windowSizeClass = windowSize)

                }
                }
            }
        }
    }


