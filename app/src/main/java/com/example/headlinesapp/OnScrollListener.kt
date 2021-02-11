package com.example.headlinesapp

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Infinite scrolling OnScrollListener for RecyclerView
 * OnScrolled will detect when more items need to be added to the data in the RecyclerView
 * @param layoutManager of the RecyclerView
 * @param updateList will be run when this listener determines you need more items to display
 */
class OnScrollListener(val layoutManager: LinearLayoutManager,
                       val updateList: () -> Unit) : RecyclerView.OnScrollListener() {

    companion object {
        //A buffer of items to kick off the loading before it is absolutely necessary
        //The higher the number, the "sooner" it will run updateList
        private const val VISIBLE_BUFFER = 10
    }

    private var loading = true
    private var previousTotal = 0
    private var firstVisibleItem = 0
    private var visibleItemCount = 0
    private var totalItemCount = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        //Gather current data
        visibleItemCount = recyclerView.childCount
        totalItemCount = layoutManager.itemCount
        firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

        //If in the loading state, check if the list was updated, and leave loading state
        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false
                previousTotal = totalItemCount
            }
        }

        //If first visible item (plus the buffer) is >= the size of the data minus the visible items
        //And it isn't already in the loading state, update the list and enter the loading state.
        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + VISIBLE_BUFFER)) {
            updateList()
            loading = true
        }
    }

    /**
     * Resets OnScrollListener, in cases where the dataset displayed in the RecyclerView is cleared
     */
    fun reset() {
        loading = true
        previousTotal = 0
        firstVisibleItem = 0
        visibleItemCount = 0
        totalItemCount = 0
    }
}