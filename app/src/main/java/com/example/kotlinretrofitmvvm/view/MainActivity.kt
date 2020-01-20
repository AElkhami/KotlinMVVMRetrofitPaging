package com.example.kotlinretrofitmvvm.view

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinretrofitmvvm.R
import com.example.kotlinretrofitmvvm.data.SharedPrefs
import com.example.kotlinretrofitmvvm.data.models.Photo
import com.example.kotlinretrofitmvvm.logic.FlickrSearchViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: FlickrSearchViewModel
    private lateinit var mainAdapter: MainActivityRecyclerAdapter
    private var searchHistory: SharedPrefs? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchHistory = SharedPrefs(this@MainActivity)

        viewModel = ViewModelProviders
            .of(this@MainActivity)
            .get(FlickrSearchViewModel::class.java)

        setupRecyclerAdapter()

        if(searchHistory?.getSavedQueries() != null){
            setupSearchHistoryAdapter()
        }

        setUpListeners()
    }

    private fun setUpListeners() {

        searchButton.setOnClickListener{
            getSearchResults(searchTextView.text.toString())
        }

        searchTextView.setOnEditorActionListener { query, actionId, event ->
            if (event == null
                || event.action != KeyEvent.ACTION_DOWN)
                return@setOnEditorActionListener false

            getSearchResults(searchTextView.text.toString())

            return@setOnEditorActionListener true
        }

        searchTextView.setOnItemClickListener { adapterView, view, i, l ->
            getSearchResults(adapterView.getItemAtPosition(i).toString())
        }

        searchTextView.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {

                if(count <= 0){
                    clearTextButton.visibility = View.GONE
                }else {
                    clearTextButton.visibility = View.VISIBLE
                }
            }
        })

        clearTextButton.setOnClickListener{
            searchTextView.setText("")
            flickrPhotosListView.visibility = View.GONE
            emptyListTextView.visibility = View.VISIBLE
            progressBar.visibility = View.GONE

            mainAdapter.submitList(null)
            mainAdapter.currentList?.dataSource?.invalidate()
        }
    }

    //Variable created separatley to prevent memory leaks
    private val observerPhotos = Observer<PagedList<Photo>> {

        val photosList = it

        if (!photosList.isNullOrEmpty()) {
            mainAdapter.submitList(photosList)

            flickrPhotosListView.visibility = View.VISIBLE
            emptyListTextView.visibility = View.GONE
            progressBar.visibility = View.GONE
            reachBottomProgressBar.visibility = View.GONE
        } else {
            flickrPhotosListView.visibility = View.GONE
            emptyListTextView.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            emptyListTextView.text = getString(R.string.list_is_empty_or_null)
        }

        mainAdapter.notifyDataSetChanged()
    }

    private fun setupRecyclerAdapter() {
        mainAdapter = MainActivityRecyclerAdapter()

        flickrPhotosListView.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 2)
            adapter = mainAdapter
        }

        flickrPhotosListView.addOnScrollListener(object: RecyclerView.OnScrollListener(){

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if(!recyclerView.canScrollVertically(1)){
                    reachBottomProgressBar.visibility = View.VISIBLE
                }else{
                    reachBottomProgressBar.visibility = View.GONE
                }

            }
        })

        mainAdapter.notifyDataSetChanged()
    }

    private fun setupSearchHistoryAdapter() {
        val searchHistoryAdapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1,
            searchHistory?.getSavedQueries()!!.toList())

        searchTextView.threshold = 1 //will start working from first character

        searchTextView.setAdapter(searchHistoryAdapter) //setting the adapter data into the AutoCompleteTextView
    }

    private fun getSearchResults(query: String){

        progressBar.visibility = View.VISIBLE
        emptyListTextView.visibility = View.GONE
        flickrPhotosListView.visibility = View.GONE

        mainAdapter.submitList(null)
        mainAdapter.currentList?.dataSource?.invalidate()

        flickrPhotosListView.postDelayed({
            flickrPhotosListView.layoutManager?.scrollToPosition(0)
        }, 400)

        if (query != "") {
            viewModel.searchFlickrPhotos(query)
                .observe(this@MainActivity, observerPhotos)

            saveSearchQuery(query)
        }

        mainAdapter.notifyDataSetChanged()

        hideKeyboard(this)
    }

    private fun saveSearchQuery(query: String){

        val mySet = mutableSetOf(query)

        val appendedHistorySet = searchHistory?.getSavedQueries()
        appendedHistorySet?.addAll(mySet)

        if(appendedHistorySet == null){
            searchHistory?.saveQuery(mySet)
        }else {
            searchHistory?.saveQuery(appendedHistorySet)
        }

        setupSearchHistoryAdapter()
    }

    private fun hideKeyboard(activity: Activity) {
        val imm: InputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}
