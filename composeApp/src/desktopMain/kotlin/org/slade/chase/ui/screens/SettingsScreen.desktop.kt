package org.slade.chase.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import chase.composeapp.generated.resources.Res
import chase.composeapp.generated.resources.baseline_folder_24
import org.jetbrains.compose.resources.vectorResource
import org.slade.chase.ui.BrowserExtensions
import org.slade.chase.ui.RotatingSurface
import org.slade.chase.ui.dialogs.openFileDialog
import org.slade.chase.ui.theme.ChaseTheme

private data class SettingsState(
    var concurrency: Boolean = false,
)

@Composable
private fun rememberSettingsState(): SettingsState {
    return remember {
        SettingsState()
    }
}

actual class SettingsScreen actual constructor() : Screen {

    override val key: ScreenKey
        get() = "Settings"

    @Composable
    override fun Content() {
        Settings()
    }
}

@Composable
private fun Settings() {

    var concurrency by remember {
        mutableStateOf(false)
    }

    var numberOfConcurrentDownloads by remember {
        mutableStateOf(8)
    }

    var showDownloadsDirectoryChooser by remember {
        mutableStateOf(false)
    }

    var downloadsDirectory by remember {
        mutableStateOf("/home/slade/Downloads")
    }

    var showDownloadCompleteDialog by remember {
        mutableStateOf(false)
    }

    if(showDownloadsDirectoryChooser) {
        openFileDialog(
            title = "Select downloads folder",
            onCloseRequest = {
                showDownloadsDirectoryChooser = false
            },
            onResult = { dirSet ->
                if(dirSet.isNotEmpty()) {
                    downloadsDirectory = dirSet.elementAt(0)
                }
            }
        )
    }

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(360.dp),
        modifier = Modifier
            .padding(8.dp)
    ) {

        item {
            OutlinedCard(
                modifier = Modifier.padding(8.dp),
                shape = RectangleShape
            ) {
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = "Browser Monitoring",
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    BrowserExtensions(
                        modifier = Modifier
                    )
                }
            }
        }

        item("NumberOfConcurrentDownloads") {
            Box(
                modifier = Modifier.padding(8.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RectangleShape,
                    value = TextFieldValue(
                        "$numberOfConcurrentDownloads Concurrent downloads"
                    ),
                    label = {
                        Text("Number of concurrent downloads: ")
                    },
                    onValueChange = {},
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    singleLine = true,
                    supportingText = {
                        Text(
                            text = "Set a limit for the number of concurrent downloads",
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    trailingIcon = {
                        TextButton(
                            modifier = Modifier
                                .padding(horizontal = 8.dp),
                            onClick = {
                                concurrency = true
                            },
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            RotatingSurface(
                                rotate = concurrency,
                                initialRotation = 90f,
                                sweepRotation = -90f
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                                    contentDescription = ""
                                )
                            }
                        }

                        MaterialTheme(
                            shapes = MaterialTheme.shapes.copy(
                                extraSmall = RoundedCornerShape(8.dp)
                            )
                        ) {
                            DropdownMenu(
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.surface)
                                    .border(
                                        width = 1.dp,
                                        brush = Brush.linearGradient(
                                            colors = ChaseTheme.borderGradientColors.map { it.copy(alpha = 0.3f) }
                                        ),
                                        shape = RoundedCornerShape(8.dp)
                                    ),
                                expanded = concurrency,
                                onDismissRequest = {
                                    concurrency = false
                                }
                            ) {
                                (1..10).map { v ->
                                    DropdownMenuItem(
                                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 1.dp),
                                        text = {
                                            Text(text = if(v == 8) "($v Recommended)" else "$v")
                                        },
                                        onClick = {
                                            numberOfConcurrentDownloads = v
                                        }
                                    )
                                }
                            }
                        }
                    }
                )
            }
        }

        item {
            OutlinedCard(
                modifier = Modifier.padding(8.dp),
                shape = RectangleShape
            ) {
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = "Save files to:",
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                    )

                    listOf("Default", "Documents", "Video", "Audio", "Compressed").map {
                        OutlinedTextField(
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent
                            ),
                            value = downloadsDirectory,
                            onValueChange = {},
                            modifier = Modifier
                                .fillMaxWidth(),
                            prefix = {
                                Text(
                                    text = "$it: ",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            },
                            trailingIcon = {
                                TextButton(
                                    enabled = !showDownloadsDirectoryChooser,
                                    modifier = Modifier
                                        .size(36.dp),
                                    onClick = {
                                        showDownloadsDirectoryChooser = true
                                    },
                                    shape = RoundedCornerShape(
                                        8.dp
                                    ),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Icon(
                                        imageVector = vectorResource(Res.drawable.baseline_folder_24),
                                        contentDescription = "Directory Chooser"
                                    )
                                }
                            },
                            singleLine = true
                        )
                    }
                }
            }
        }

        item {
            Box(
                modifier = Modifier
                    .padding(8.dp)
            ) {
                OutlinedTextField(
                    shape = RectangleShape,
                    value = downloadsDirectory,
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth(),
                    supportingText = {
                        Text(
                            text = "Set where to temporarily hold files during download.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    label = {
                        Text(
                            text = "Temporary folder",
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    trailingIcon = {
                        TextButton(
                            enabled = !showDownloadsDirectoryChooser,
                            modifier = Modifier
                                .size(36.dp),
                            onClick = {
                                showDownloadsDirectoryChooser = true
                            },
                            shape = RoundedCornerShape(
                                8.dp
                            ),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Icon(
                                imageVector = vectorResource(Res.drawable.baseline_folder_24),
                                contentDescription = "Directory Chooser"
                            )
                        }
                    },
                    singleLine = true
                )
            }
        }

        item {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(8.dp)
            ) {
                Checkbox(
                    checked = showDownloadCompleteDialog,
                    onCheckedChange = {
                        showDownloadCompleteDialog = it
                    }
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Show Download Complete Dialog",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}