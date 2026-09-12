package com.example.bushido

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.bushido.ui.navigation.BushidoApp
import com.example.bushido.ui.theme.BushidoTheme
import com.example.bushido.ui.viewmodel.BushidoViewModel
import com.example.bushido.ui.viewmodel.BushidoViewModelFactory

class MainActivity : ComponentActivity() {

    private val viewModel: BushidoViewModel by viewModels {
        BushidoViewModelFactory((application as BushidoApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BushidoTheme {
                BushidoApp(
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
