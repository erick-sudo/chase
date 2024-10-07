package org.slade.chase.ui.screens.downloads

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

class All : Screen {
    override val key: String = "Downloads#All"

    @Composable
    override fun Content() {
        Column {
            Text("All")
        }
    }
}

class Documents : Screen {
    override val key: String = "Downloads#Documents"

    @Composable
    override fun Content() {
        Column {
            Text("Documents")
        }
    }
}

class Images : Screen {
    override val key: String = "Downloads#Images"

    @Composable
    override fun Content() {
        Column {
            Text("Images")
        }
    }
}

class Videos : Screen {
    override val key: String = "Downloads#Videos"

    @Composable
    override fun Content() {
        Column {
            Text("Videos")
        }
    }
}

class Audio : Screen {
    override val key: String = "Downloads#Audio"

    @Composable
    override fun Content() {
        Column {
            Text("Audio")
        }
    }
}

class Programs : Screen {
    override val key: String = "Downloads#Programs"

    @Composable
    override fun Content() {
        Column {
            Text("Programs")
        }
    }
}