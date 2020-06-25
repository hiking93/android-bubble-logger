package com.hiking.bubblelogger.viewholder

import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hiking.bubblelogger.R
import kotlinx.android.synthetic.main.list_item_log.view.*
import java.text.DateFormat
import java.util.*

class LogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {

        private val dateFormat = DateFormat.getDateTimeInstance()

        fun create(parent: ViewGroup) = LogViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_log, parent, false)
        )
    }

    private val titleTextView = itemView.titleTextView
    private val contentTextView = itemView.contentTextView
    private val timeTextView = itemView.timeTextView

    fun bind(
        title: CharSequence?,
        content: CharSequence,
        timestamp: Long,
        filterStrings: List<String>
    ) {
        titleTextView.apply {
            visibility = if (title == null) View.GONE else View.VISIBLE
            text = title.highlightedWith(filterStrings)
        }
        contentTextView.text = content.highlightedWith(filterStrings)
        timeTextView.text = dateFormat.format(Date(timestamp))
    }

    private fun CharSequence?.highlightedWith(
        filterStrings: List<String>
    ) = SpannableStringBuilder(this).apply {
        filterStrings.forEach { filterString ->
            val startIndex = indexOf(filterString).also { if (it == -1) return@forEach }
            val endIndex = startIndex + filterString.length
            setSpan(
                StyleSpan(Typeface.BOLD),
                startIndex,
                endIndex,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            setSpan(
                UnderlineSpan(),
                startIndex,
                endIndex,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }
}
