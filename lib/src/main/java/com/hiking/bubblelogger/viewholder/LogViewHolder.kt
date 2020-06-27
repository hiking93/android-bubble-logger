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

/**
 * [RecyclerView.ViewHolder] for a log in the bubble.
 */
class LogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {

        private val dateFormat = DateFormat.getDateTimeInstance()

        /**
         * Create an instance of [LogViewHolder].
         */
        fun create(parent: ViewGroup) = LogViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_log, parent, false)
        )
    }

    private val titleTextView = itemView.titleTextView
    private val contentTextView = itemView.contentTextView
    private val timeTextView = itemView.timeTextView

    /**
     * Bind data to this [LogViewHolder].
     * @param title Title of the log.
     * @param content Content of the log.
     * @param timestamp Timestamp of the log.
     * @param filterStrings Filter strings for text highlighting.
     */
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
        for (filterString in filterStrings) {
            val startIndex = indexOf(filterString).let { if (it == -1) null else it } ?: continue
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
