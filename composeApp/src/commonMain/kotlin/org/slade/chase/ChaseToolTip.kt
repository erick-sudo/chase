package org.slade.chase

import androidx.compose.material3.CaretScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChaseRichToolTip(
    modifier: Modifier = Modifier,
    tooltip: @Composable CaretScope.() -> Unit,
    anchor: @Composable () -> Unit
) {

    val tooltipState = rememberTooltipState()

    val tooltipPositionProvider = TooltipDefaults.rememberRichTooltipPositionProvider()

    TooltipBox(
        modifier = Modifier
            .then(modifier),
        tooltip = {
            tooltip()
        },
        state = tooltipState,
        positionProvider = tooltipPositionProvider
    ) {
        anchor()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChasePlainToolTip(
    modifier: Modifier = Modifier,
    tooltip: @Composable CaretScope.() -> Unit,
    anchor: @Composable () -> Unit
) {

    val tooltipState = rememberTooltipState()

    val tooltipPositionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider()

    TooltipBox(
        modifier = Modifier
            .then(modifier),
        tooltip = {
            tooltip()
        },
        state = tooltipState,
        positionProvider = tooltipPositionProvider
    ) {
        anchor()
    }
}