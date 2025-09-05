package com.hamomel.queens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hamomel.queens.R
import com.hamomel.queens.ui.theme.QueensTheme
import com.hamomel.queens.ui.widgets.QueensButtonLarge
import kotlinx.serialization.Serializable

@Serializable
data class BoardSizeRoute(val currentSize: Int) {
    companion object {
        const val RESULT_KEY = "board_size"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardSizeScreen(
    currentSize: Int,
    maxSize: Int,
    minSize: Int = 4,
    onBoardSizeChosen: (Int) -> Unit,
    onClose: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.close_button_content_description)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        var size by remember { mutableIntStateOf(currentSize) }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(64.dp))

            Text(
                text = "Choose Board Size",
                style = MaterialTheme.typography.headlineMedium
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                IconButton(
                    onClick = { size = (size - 1).coerceAtLeast(minSize) },
                    enabled = size > minSize
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = stringResource(R.string.board_size_make_smaller_button_content_description),
                        modifier = Modifier.graphicsLayer(scaleX = 3f, scaleY = 3f)
                    )
                }

                Spacer(modifier = Modifier.size(32.dp))

                Text(
                    text = size.toString(),
                    style = MaterialTheme.typography.displayMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.sizeIn(minWidth = 80.dp)
                )

                Spacer(modifier = Modifier.size(32.dp))

                IconButton(
                    onClick = { size = (size + 1).coerceAtMost(maxSize) },
                    enabled = size < maxSize
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = stringResource(R.string.board_size_make_larger_button_content_description),
                        modifier = Modifier.graphicsLayer(scaleX = 3f, scaleY = 3f)
                    )
                }
            }

            QueensButtonLarge(
                onClick = { onBoardSizeChosen(size) },
                text = stringResource(R.string.board_size_play_button_title),
            )

            Spacer(modifier = Modifier.height(64.dp))
        }
    }
}

@Preview
@Composable
private fun BoardSizeScreenPreview() {
    QueensTheme {
        BoardSizeScreen(
            currentSize = 8,
            minSize = 4,
            maxSize = 16,
            onBoardSizeChosen = {},
            onClose = {}
        )
    }
}
