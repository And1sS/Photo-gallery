package com.and1ss.android.photogallery.view

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.and1ss.android.photogallery.PhotoGalleryViewModel
import com.and1ss.android.photogallery.R
import com.and1ss.android.photogallery.model.BaseDataSource


private const val TAG = "PhotoGalleryFragment"

class PhotoGalleryFragment : Fragment() {
    private lateinit var photoRecyclerView: RecyclerView
    private lateinit var galleryItemAdapter: GalleryItemAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var errorView: View

    private val photoGalleryViewModel by lazy {
        ViewModelProvider(this).get(PhotoGalleryViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.fragment_photo_gallery,
            container, false
        )

        errorView = view.findViewById(R.id.error_layout)

        swipeRefreshLayout = (view.findViewById(R.id.swipe_refresh) as SwipeRefreshLayout).apply {
            setOnRefreshListener {
                photoGalleryViewModel.reloadCurrentDataSource()
            }
        }

        galleryItemAdapter = GalleryItemAdapter(context!!, parentFragmentManager).apply {
            registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    if (positionStart == 0) {
                        photoRecyclerView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                            override fun onGlobalLayout() {
                                photoRecyclerView.scrollToPosition(0)
                                photoRecyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                            }
                        })
                    }
                }
            })
        }

        photoRecyclerView = (view.findViewById(R.id.photo_recycler_view) as RecyclerView)
            .apply {
                adapter = galleryItemAdapter
                layoutManager = GridLayoutManager(context, 3)
                overScrollMode = View.OVER_SCROLL_IF_CONTENT_SCROLLS
                setHasFixedSize(true)
            }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        photoGalleryViewModel.galleryItemPagedList.observe(
            viewLifecycleOwner, Observer { galleryItems ->
                galleryItemAdapter.submitList(galleryItems)
            })

        photoGalleryViewModel.loadStatus.observe(
            viewLifecycleOwner, Observer { loadStatus ->
                when(loadStatus) {
                    is BaseDataSource.LoadStatus.Loaded -> {
                        swipeRefreshLayout.isRefreshing = false
                        errorView.visibility = View.GONE
                    }

                    is BaseDataSource.LoadStatus.Loading -> {
                        errorView.visibility = View.GONE
                    }

                    is BaseDataSource.LoadStatus.Error -> {
                        swipeRefreshLayout.isRefreshing = false
                        errorView.visibility = View.VISIBLE
                        Log.d(TAG, "FETCHING: ${loadStatus.msg}")
                    }
                }
            })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_photo_gallery, menu)

        (menu.findItem(R.id.menu_item_search).actionView as SearchView).apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    clearFocus()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText != null) {
                        photoGalleryViewModel.switchToSearchDataSource(newText)
                        return true
                    }
                    return false
                }
            })

            setOnCloseListener {
                photoGalleryViewModel.switchToInterestingDataSource()
                clearFocus()
                onActionViewCollapsed()
                true
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when(item.itemId) {
            R.id.menu_item_search -> true
            else -> super.onOptionsItemSelected(item)
        }

    companion object {
        fun newInstance(): PhotoGalleryFragment =
            PhotoGalleryFragment()
    }
}
