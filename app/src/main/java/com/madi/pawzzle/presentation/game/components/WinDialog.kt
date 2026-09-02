package com.madi.pawzzle.presentation.game.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun WinDialog(
    onDismiss: () -> Unit,
    onNewGame: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("You Won!") },
        text = { Text("Great job! You found all the cats.") },
        confirmButton = {
            TextButton(onClick = {
                onNewGame()
                onDismiss()
            }) {
                Text("New Game")
            }
        }
    )
}
