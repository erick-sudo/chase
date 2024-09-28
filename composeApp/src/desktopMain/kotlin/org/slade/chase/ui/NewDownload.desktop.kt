package org.slade.chase.ui

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import org.slade.chase.models.DownloadItem

private enum class NewDownloadStage {
    Create,
    Confirm
}

@Composable
actual fun NewDownload(
    onSuccess: (DownloadItem) -> Unit,
    onCancel: () -> Unit
) {

    var newDownloadUrl by remember {
        mutableStateOf("")
    }

    var stage by remember {
        mutableStateOf(NewDownloadStage.Create)
    }

    Crossfade(
        targetState = stage
    ) { newDownloadStage ->
        when(newDownloadStage) {
            NewDownloadStage.Create -> CreateDownloadWindow(
                onSuccess = { url, saveTo ->
                    newDownloadUrl = url
                    stage = NewDownloadStage.Confirm
                },
                onCancel = {
                    onCancel()
                }
            )
            else -> NewDownloadConfirmation(
                url = newDownloadUrl,
                onSuccess = {
                    //onSuccess()
                },
                onCancel = {
                    onCancel()
                },
                recreate = {
                    stage = NewDownloadStage.Create
                }
            )
        }
    }
}