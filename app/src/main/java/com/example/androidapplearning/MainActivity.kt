package com.example.androidapplearning

import TeamMatch
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
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
import com.google.firebase.Firebase
import com.google.firebase.database.database
import java.util.Calendar

private const val TAG = "MainActivity"
private var numberDisplayValue: Int = 0
class MainActivity : ComponentActivity() {
    val database = Firebase.database("https://androidapplearning-default-rtdb.europe-west1.firebasedatabase.app/")
    private lateinit var buttonAdditionButton: Button
    private lateinit var buttonSubtractionButton: Button
    private lateinit var textViewNumberDisplay: TextView
    private lateinit var editTextTeamName: EditText
    private lateinit var buttonSendButton: Button
    private lateinit var viewResultsButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mainlayout)

        buttonAdditionButton = findViewById(R.id.additionButton)
        buttonSubtractionButton = findViewById(R.id.subtractionButton)
        textViewNumberDisplay = findViewById(R.id.numberDisplay)
        editTextTeamName = findViewById(R.id.teamName)
        buttonSendButton = findViewById(R.id.sendButton)
        viewResultsButton = findViewById(R.id.switchToResults)

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

        buttonSendButton.setOnClickListener(
            object: View.OnClickListener {
                override fun onClick(v: View?) {
                    val teamName: String = editTextTeamName.text.toString()
                    val matchInfo: TeamMatch = TeamMatch(teamName, numberDisplayValue)
                    var databaseNewEntryRef = database.getReference(teamName).child(Calendar.getInstance().time.toString())
                    databaseNewEntryRef.setValue(matchInfo)
                }
            }
        )


        viewResultsButton.setOnClickListener(
            object: View.OnClickListener {
                override fun onClick(v: View?) {
                    val intent = Intent(this@MainActivity, ViewResultsActivity::class.java)
                    startActivity(intent)
                }
            }
        )
    }
}