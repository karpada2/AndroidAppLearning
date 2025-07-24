package com.example.androidapplearning

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.androidapplearning.ui.theme.AndroidAppLearningTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidAppLearningTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) {
                    innerPadding -> TextWithModifier("hi there", Modifier.padding(innerPadding))
                }
            }
        }
    }
}


@Composable
fun Counter(name: String, modifier:Modifier = Modifier) {

}

@Composable
fun TextWithModifier(text: String, modifier: Modifier = Modifier) {
    AndroidAppLearningTheme {
        Text(
            text = text,
            modifier = modifier
        )
    }
}