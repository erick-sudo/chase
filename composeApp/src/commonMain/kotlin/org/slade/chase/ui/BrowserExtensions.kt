package org.slade.chase.ui

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.slade.chase.models.browsers

@Composable
fun BrowserExtensions(
    modifier: Modifier = Modifier
) {

    val _browsers = browsers

    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .then(modifier)
    ) {
        _browsers
            .slice(0 until _browsers.size / 2)
            .zip(_browsers.slice((_browsers.size / 2) until _browsers.size))
            .mapIndexed { index, (browser1, browser2) ->

                if(index > 0) {
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    listOf(browser1, browser2).mapIndexed { index, browser ->

                        if(index > 0) {
                            Spacer(modifier = Modifier.width(8.dp))
                        }

                        ElevatedButton(
                            modifier = Modifier
                                .weight(1f),
                            onClick = {},
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(2.dp)
                            ) {
                                Column (
                                    modifier = Modifier
                                        .padding(vertical = 16.dp, horizontal = 24.dp)
                                        .fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = browser.name,
                                        maxLines = 1,
                                        style = MaterialTheme.typography.bodySmall
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Image(
                                        modifier = Modifier
                                            .size(64.dp),
                                        imageVector = browser.vector,
                                        contentDescription = browser.name
                                    )
                                }

                                Checkbox(
                                    checked = index%2==0,
                                    onCheckedChange = {},
                                    modifier = Modifier
                                        .align(
                                            alignment = Alignment.BottomEnd
                                        )
                                )
                            }
                        }
                    }
                }
            }
    }
}