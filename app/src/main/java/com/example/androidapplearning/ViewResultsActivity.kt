package com.example.androidapplearning

import TeamShowcase
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.children
import com.example.androidapplearning.ui.theme.AndroidAppLearningTheme
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import org.w3c.dom.Text
import org.xmlpull.v1.XmlPullParser
import java.text.AttributedCharacterIterator

private const val TAG = "ViewResultsActivityTAG"
public var textViewIdToUse: Int = 1
class ViewResultsActivity : ComponentActivity() {
    val database = Firebase.database("https://androidapplearning-default-rtdb.europe-west1.firebasedatabase.app/")

    private lateinit var viewInputFormButton: Button


    private lateinit var tableLayoutResults: TableLayout
    private lateinit var tableRowTeamNames: TableRow
    private lateinit var tableRowAverageScore: TableRow
    private lateinit var tableRowMaxScore: TableRow
    private lateinit var tableRowMinScore: TableRow

    private lateinit var textViewTeamNameTitle: TextView
    private lateinit var textViewAverageScoreTitle: TextView
    private lateinit var textViewMaxScoreTitle: TextView
    private lateinit var textViewMinScoreTitle: TextView


    private lateinit var radioGroupSortOrder: RadioGroup
    private lateinit var radioButtonAverage: RadioButton
    private lateinit var radioButtonMax: RadioButton
    private lateinit var radioButtonMin: RadioButton


    private lateinit var teamShowcases: Array<TeamShowcase>
    private lateinit var knownTeamNames: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.viewresultslayout)

        viewInputFormButton = findViewById(R.id.switchToInputForm)


        tableLayoutResults = findViewById(R.id.resultsTableLayout)
        tableRowTeamNames = findViewById(R.id.teamNames)
        tableRowAverageScore = findViewById(R.id.averageTeamScore)
        tableRowMaxScore = findViewById(R.id.maxTeamScore)
        tableRowMinScore = findViewById(R.id.minTeamScore)

        textViewTeamNameTitle = findViewById(R.id.teamNamesTitle)
        textViewAverageScoreTitle = findViewById(R.id.averageTeamScoreTitle)
        textViewMaxScoreTitle = findViewById(R.id.maxTeamScoreTitle)
        textViewMinScoreTitle = findViewById(R.id.minTeamScoreTitle)


        radioGroupSortOrder = findViewById(R.id.radioGroup)

        radioButtonAverage = findViewById(R.id.sortAverageScore)
        radioButtonMax = findViewById(R.id.sortMaxScore)
        radioButtonMin = findViewById(R.id.sortMinScore)

        teamShowcases = arrayOf()
        knownTeamNames = arrayOf()

        radioGroupSortOrder.setOnCheckedChangeListener(
            object: RadioGroup.OnCheckedChangeListener {
                override fun onCheckedChanged(
                    group: RadioGroup,
                    checkedId: Int
                ) {
                    var selectedRadioButton: RadioButton = findViewById(checkedId)

                    var outputMessage: String = "["
                    for (team in teamShowcases) {
                        outputMessage = outputMessage.plus(team.toString())
                    }
                    outputMessage = outputMessage.plus("]")
                    Log.i(TAG, outputMessage)

                    if (selectedRadioButton == radioButtonAverage) {
                        orderTeams(teamShowcases, TeamShowcase.SortOrder.AVERAGE_SCORE)
                        Log.i(TAG, "Average Button picked")
                    }
                    else if (selectedRadioButton == radioButtonMax) {
                        orderTeams(teamShowcases, TeamShowcase.SortOrder.MAX_SCORE)
                        Log.i(TAG, "Max Button picked")
                    }
                    else if (selectedRadioButton == radioButtonMin) {
                        orderTeams(teamShowcases, TeamShowcase.SortOrder.MIN_SCORE)
                        Log.i(TAG, "Min Button picked")
                    }
                }

            }
        )

        database.reference.addValueEventListener(
            object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChildren()) {
                        Log.i(TAG, "snapshot.children: " + snapshot.toString())
                        for (child in snapshot.children) {
                            var scores: Array<Int> = Array(child.childrenCount.toInt(), {i: Int -> child.children.elementAt(i).child("score").getValue().toString().toInt()})
                            Log.i(TAG, "child: " + child.toString() + " | scores: " + scores.toString())

                            Log.i(TAG, "child key: " + child.key.toString())
                            var outputMessage: String = "["
                            for (knownTeam in knownTeamNames) {
                                outputMessage = outputMessage.plus(knownTeam)
                            }
                            Log.i(TAG, "known teams: " + outputMessage + "]")
                            Log.i(TAG, "is new team NOT in known teams? " + (child.key.toString() !in knownTeamNames))
                            if (child.key.toString() !in knownTeamNames) {
                                knownTeamNames = knownTeamNames.plusElement(child.key.toString())
                                var addedTeam: TeamShowcase = TeamShowcase(child.key.toString())
                                teamShowcases = teamShowcases.plusElement(addedTeam)

                                addedTeam.setTextViewTeamName(generateTextView(""))
                                addedTeam.setTextViewAverageScore(generateTextView(""))
                                addedTeam.setTextViewMaxScore(generateTextView(""))
                                addedTeam.setTextViewMinScore(generateTextView(""))

                                addedTeam.updateScores(scores.toIntArray())
                            }
                            else {
                                TeamShowcase.getTeamWithName(teamShowcases, child.key.toString()).updateScores(scores.toIntArray())
                            }
                        }
                        orderTeams(teamShowcases, TeamShowcase.SortOrder.AVERAGE_SCORE)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    handleFirebaseError(error)
                }
            }
        )

        viewInputFormButton.setOnClickListener(
            object: View.OnClickListener {
                override fun onClick(v: View?) {
                    val intent = Intent(this@ViewResultsActivity, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        )
    }

    fun generateTextView(text: String, padding: Int = 3): TextView {
        var textView: TextView = TextView(this)
        textView.id = textViewIdToUse
        textViewIdToUse++

        textView.text = text

        val layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.MATCH_PARENT
        )
        textView.layoutParams = layoutParams

        textView.gravity = Gravity.CENTER

        textView.setPadding(padding, 0, padding, 0)

        return textView
    }

    fun handleFirebaseError(error: DatabaseError): Unit {
        AlertDialog.Builder(this)
            .setTitle("Database Error")
            .setMessage(error.toString())
            .show()
    }

    fun orderTeams(teams: Array<TeamShowcase>, order: TeamShowcase.SortOrder): Unit {
        var sorted: Array<TeamShowcase> = TeamShowcase.sortTeams(teams, order)

        Log.i(TAG, "sorted list to add: " + sorted.toList().toString())

        tableRowTeamNames.removeAllViews()
        tableRowAverageScore.removeAllViews()
        tableRowMaxScore.removeAllViews()
        tableRowMinScore.removeAllViews()


        tableRowTeamNames.addView(textViewTeamNameTitle)
        tableRowAverageScore.addView(textViewAverageScoreTitle)
        tableRowMaxScore.addView(textViewMaxScoreTitle)
        tableRowMinScore.addView(textViewMinScoreTitle)

        var i: Int = 1
        for (team in sorted) {
            Log.i(TAG, "team id: " + team.textViewTeamName.id + ", i = " + i)
            tableRowTeamNames.addView(team.textViewTeamName)
            tableRowAverageScore.addView(team.textViewAverageScore)
            tableRowMaxScore.addView(team.textViewMaxScore)
            tableRowMinScore.addView(team.textViewMinScore)
            i++
        }
    }
}