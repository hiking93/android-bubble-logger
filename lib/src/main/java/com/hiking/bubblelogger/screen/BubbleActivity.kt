package com.hiking.bubblelogger.screen

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.TooltipCompat
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hiking.bubblelogger.BubbleDataHelper
import com.hiking.bubblelogger.R
import com.hiking.bubblelogger.extension.applyEdgeToEdge
import com.hiking.bubblelogger.extension.clearFocusAndHideIme
import kotlinx.android.synthetic.main.activity_bubble.*

/**
 * [AppCompatActivity] for bubble content.
 */
class BubbleActivity : AppCompatActivity() {

    private val viewModel: BubbleViewModel by viewModels()
    private val adapter = BubbleListAdapter()
    private val layoutManager = LinearLayoutManager(this).apply {
        reverseLayout = true
        stackFromEnd = true
    }
    private var isScrollingManually = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bubble)
        setupWindow()
        setupViews()
        setupViewModel()
    }

    private fun setupWindow() {
        applyEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(rootLayout) { v, insets ->
            rootLayout.updatePadding(
                left = insets.systemWindowInsetLeft,
                right = insets.systemWindowInsetRight
            )
            recyclerView.updatePadding(
                bottom = insets.systemWindowInsetBottom
            )
            WindowInsetsCompat.Builder(insets)
                .setSystemWindowInsets(Insets.of(0, insets.systemWindowInsetTop, 0, 0))
                .build()
        }
    }

    private fun setupViews() {
        setupFilterViews()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        recyclerView.apply {
            adapter = this@BubbleActivity.adapter
            layoutManager = this@BubbleActivity.layoutManager
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    updateScrollToTopButton()
                }
            })
        }
        setupRecyclerViewTouchListener()
        setupScrollControls()
    }

    private fun setupRecyclerViewTouchListener() {
        recyclerView.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    isScrollingManually = true
                    filterEditText.clearFocusAndHideIme()
                    v.isPressed = true
                }
                MotionEvent.ACTION_UP -> {
                    isScrollingManually = false
                    v.performClick()
                    v.isPressed = false
                }
            }
            false
        }
    }

    private fun setupScrollControls() {
        scrollToTopButton.apply {
            TooltipCompat.setTooltipText(this, contentDescription)
            setOnClickListener {
                it.visibility = View.GONE
                scrollToTop()
            }
        }
    }

    private fun setupFilterViews() {
        filterEditText.doOnTextChanged { text, _, _, _ ->
            adapter.filterStrings = text.toString().trim().split("\\s".toRegex())
            updateEmptyState()
            resetFilterButton.visibility = if (text.isNullOrEmpty()) View.GONE else View.VISIBLE
        }
        resetFilterButton.apply {
            TooltipCompat.setTooltipText(this, contentDescription)
            setOnClickListener {
                filterEditText.text.clear()
            }
        }
        clearButton.apply {
            TooltipCompat.setTooltipText(this, contentDescription)
            setOnClickListener {
                BubbleDataHelper.clearLogs()
            }
        }
    }

    private fun setupViewModel() {
        viewModel.logs.observe(this, Observer {
            updateList()
        })
    }

    private fun updateList() {
        adapter.items = buildListItems()
        updateEmptyState()
        recyclerView.post { updateScrollToTopButton() }
    }

    private fun updateEmptyState() {
        nothingTextView.apply {
            visibility = if (adapter.displayItems.isNullOrEmpty()) View.VISIBLE else View.GONE
            setText(
                if (adapter.filterStrings.isEmpty()) R.string.log_empty_message
                else R.string.log_empty_filtered_message
            )
        }
    }

    private fun updateScrollToTopButton() {
        val couldScrollUp = scrollToTopButton.visibility == View.VISIBLE
        val canScrollUp = recyclerView.canScrollVertically(-1)
        if (!isScrollingManually && !couldScrollUp && canScrollUp) {
            scrollToTop()
        } else {
            scrollToTopButton.visibility = if (canScrollUp) View.VISIBLE else View.GONE
        }
    }

    private fun scrollToTop() {
        val lastItemPosition = layoutManager.itemCount - 1
        val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
        recyclerView.apply {
            if (lastItemPosition - lastVisibleItemPosition > 10) {
                // Too far away. It may takes forever to scroll smoothly.
                scrollToPosition(lastItemPosition)
            } else {
                smoothScrollToPosition(lastItemPosition)
            }
        }
    }

    private fun buildListItems() = viewModel.logs.value?.map {
        BubbleListAdapter.Item.Log(log = it)
    }
}