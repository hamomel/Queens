package com.hamomel.queens.winscreeen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hamomel.queens.R
import com.hamomel.queens.ui.theme.QueensTheme
import kotlinx.serialization.Serializable

@Serializable
data class WinRoute(val boardSize: Int)

@Composable
fun WinScreen(
    boardSize: Int,
    onClose: () -> Unit
) {
    Scaffold { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ) {

            IconButton(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp),
                onClick = onClose
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.close_button_content_description),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = stringResource(R.string.win_congratulations),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(16.dp)
                )

                Spacer(modifier = Modifier.height(80.dp))

                Text(
                    text = "🏆",
                    fontSize = 120.sp
                )

                Spacer(modifier = Modifier.height(80.dp))

                Text(
                    text = stringResource(R.string.win_success_message, boardSize),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(16.dp)
                        .sizeIn(maxWidth = 360.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun WinScreenPreview() {
    QueensTheme {
        WinScreen(8, onClose = {})
    }
}