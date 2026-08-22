package com.thesua7.kmpplayground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.thesua7.kmpplayground.di.AppContainer

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val container = AppContainer()
        val viewmodel = container.createLoginViewModel()

        setContent {
            LoginScreen(viewmodel)
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}