package org.slade.chase.models

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.vectorResource
import org.slade.chase.ui.ChaseIcons

data class Browser(
    var name: String,
    var icon: DrawableResource
) {
    val vector
        @Composable
        get() = vectorResource(icon)
}

val browsers
    @Composable
    get() = listOf(
        Browser("Google Chrome", ChaseIcons.Browsers.Chrome),
        Browser("Mozilla Firefox", ChaseIcons.Browsers.Firefox),
        Browser("Brave", ChaseIcons.Browsers.Brave),
        Browser("Microsoft Edge", ChaseIcons.Browsers.Edge),
        Browser("Ios Safari", ChaseIcons.Browsers.Safari),
        Browser("Opera mini", ChaseIcons.Browsers.Opera)
    )