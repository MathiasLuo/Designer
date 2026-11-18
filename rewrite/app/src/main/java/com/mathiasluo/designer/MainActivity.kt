package com.mathiasluo.designer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.mathiasluo.designer.navigation.DesignerNavHost
import com.mathiasluo.designer.presentation.theme.DesignerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DesignerTheme {
                DesignerNavHost()
            }
        }
    }
}
