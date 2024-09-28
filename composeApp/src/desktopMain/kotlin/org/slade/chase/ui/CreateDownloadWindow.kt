package org.slade.chase.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.rememberWindowState
import chase.composeapp.generated.resources.Res
import chase.composeapp.generated.resources.baseline_folder_24
import org.jetbrains.compose.resources.vectorResource

@Composable
fun CreateDownloadWindow(
    onSuccess: (String, String) -> Unit,
    onCancel: () -> Unit
) {

    var newDownloadUrl by remember {
        mutableStateOf(TextFieldValue(""))
    }

    var saveTo by remember {
        mutableStateOf(TextFieldValue(""))
    }

    val windowState = rememberWindowState(
        width = 500.dp,
        height = 340.dp
    )

    ChaseWindow(
        onCloseRequest = {
            onCancel()
        },
        resizable = false,
        state = windowState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Add New Download",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = newDownloadUrl,
                    onValueChange = { newFieldValue ->
                        newDownloadUrl = newFieldValue
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = "New Download Url"
                        )
                    },
                    label = {
                        Text("Insert url here")
                    },
                    supportingText = {
                        Text("Please try and use a valid url")
                    },
                    shape = RectangleShape
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = saveTo,
                    onValueChange = { newFieldValue ->
                        saveTo = newFieldValue
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    trailingIcon = {
                        Icon(
                            imageVector = vectorResource(Res.drawable.baseline_folder_24),
                            contentDescription = "Directory Chooser"
                        )
                    },
                    label = {
                        Text("Save to:")
                    },
                    supportingText = {
                        Text("Specify where you would like this file to be saved to.")
                    },
                    shape = RectangleShape
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                ElevatedButton(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    onClick = {
                        onSuccess(
                            newDownloadUrl.text,
                            saveTo.text
                        )
                    },
                    shape = RectangleShape
                ) {
                    Text("Add")
                }

                ElevatedButton(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    onClick = {
                        onCancel()
                    },
                    shape = RectangleShape
                ) {
                    Text("Cancel")
                }
            }
        }
    }
}