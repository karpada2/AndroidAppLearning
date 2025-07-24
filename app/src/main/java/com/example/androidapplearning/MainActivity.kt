package com.example.androidapplearning

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
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

private const val TAG = "MainActivity"
private const val URL: String = "https://docs.google.com/spreadsheets/d/e/2PACX-1vQ2lPlbomjfDeYtnO6-MbQOyZuWBxT8Pz_7RHxhI1MozF7gri9gBKh3CqfkMhLolHkn_we6PV7P-O1L/pubhtml?gid=0&single=true"
private var numberDisplayValue: Int = 0
class MainActivity : ComponentActivity() {
    private lateinit var buttonAdditionButton: Button
    private lateinit var buttonSubtractionButton: Button
    private lateinit var textViewNumberDisplay: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mainlayout)

        buttonAdditionButton = findViewById(R.id.additionButton)
        buttonSubtractionButton = findViewById(R.id.subtractionButton)
        textViewNumberDisplay = findViewById(R.id.numberDisplay)

        textViewNumberDisplay.text = "$numberDisplayValue"

        buttonAdditionButton.setOnClickListener(
            object: View.OnClickListener {
                override fun onClick(v: View?) {
                    numberDisplayValue++
                    Log.i(TAG, "addition happened")
                    textViewNumberDisplay.text = "$numberDisplayValue"
                }
            }
        )


        buttonSubtractionButton.setOnClickListener(
            object: View.OnClickListener {
                override fun onClick(v: View?) {
                    numberDisplayValue--
                    Log.i(TAG, "addition happened")
                    textViewNumberDisplay.text = "$numberDisplayValue"
                }
            }
        )
    }
}