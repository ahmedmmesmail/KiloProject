package com.amme.es2al_mgarab

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DoneAll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.amme.es2al_mgarab.ui.theme.Es2alMgarabTheme
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Es2alMgarabTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Quiz(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Quiz(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val questions = remember { loadQuestions(context) }
    var currentIndex by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<Answer?>(null) }
    val openAlertDialog = remember { mutableStateOf(false) }
    var score by remember { mutableStateOf(0) }

    val currentQuestion = questions[currentIndex]


    Column (
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = currentQuestion.category,
            style = MaterialTheme.typography.headlineMedium,
            modifier = modifier
        )
        Column (
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .weight(1f, false)
                .padding(12.dp)
        ) {
            Text(
                text = currentQuestion.question,
                style = MaterialTheme.typography.titleLarge,
                modifier = modifier
            )
            currentQuestion.answers.forEach { answer ->
                RadioButtonWithText(
                    selected = selectedAnswer == answer,
                    onSelect = { selectedAnswer = answer },
                    text = answer.text

                )
            }
        }
        Column (
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f, true)
                .padding(12.dp)
        ) {
            Button(
                onClick = {
                    selectedAnswer?.let {
                        if (it.isCorrect) {
                            score++
                            Toast.makeText(context, "correct :)", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "wrong :(", Toast.LENGTH_SHORT).show()
                        }
                    }
                    if (currentIndex < questions.lastIndex) {
                        currentIndex++
                        selectedAnswer = null
                    } else {
                        openAlertDialog.value = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)

                )
            {
                Text(
                    text = (if (currentIndex == questions.lastIndex) "Finish" else "Next")
                )
            }
            if (openAlertDialog.value) {
                FinishDialog(
                    onDismissRequest = { openAlertDialog.value = false },
                    onConfirmation = {
                        currentIndex = 0
                        selectedAnswer = null
                        score = 0
                        openAlertDialog.value = false
                    },
                    dialogTitle = "All Questions Done 🎉",
                    dialogText = "You’ve reached the end of the quiz!\n" +
                            "your score is: $score/${questions.size}\n" +
                            "want to start again?",
                    icon = Icons.Rounded.DoneAll
                )
            }
        }

    }
}

@Composable
fun RadioButtonWithText(
    selected: Boolean,
    onSelect: () -> Unit,
    text: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = { onSelect() },
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary,
                unselectedColor = Color.Gray,
            )
        )
        Spacer(
            modifier = Modifier.width(8.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun FinishDialog(onDismissRequest: () -> Unit,
                 onConfirmation: () -> Unit,
                 dialogTitle: String,
                 dialogText: String,
                 icon: ImageVector,){
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Yes!")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("No, thanks")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun QuizPreview() {
    Es2alMgarabTheme {
        Quiz()
    }
}

fun loadQuestions(context: Context): List<Question> {
    val json = context.assets.open("questions.json")
        .bufferedReader()
        .use { it.readText() }

    val type = object : TypeToken<List<Question>>() {}.type
    return Gson().fromJson(json, type)
}