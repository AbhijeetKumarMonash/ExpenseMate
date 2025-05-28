package com.example.expensemate

import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import io.noties.markwon.Markwon

@Composable
fun MarkdownTextCompat(markdown: String, modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            TextView(context).apply {
                setTextColor(android.graphics.Color.WHITE)
                textSize = 16f
                val markwon = Markwon.create(context)
                markwon.setMarkdown(this, markdown)
            }
        }
    )
}
