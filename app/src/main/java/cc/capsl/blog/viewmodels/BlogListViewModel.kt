package cc.capsl.blog.viewmodels

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import cc.capsl.blog.models.data.BlogPost
import cc.capsl.blog.repos.BlogRepo
import cc.capsl.blog.utils.TAG
import java.text.SimpleDateFormat
import java.util.*

class BlogListViewModel (
    isLoggedIn: Boolean,
    blogRepo: BlogRepo
): ViewModel() {

    private val blogList = MediatorLiveData<List<BlogPost>>()
    private val dateFormat by lazy { SimpleDateFormat("MMM d, yyyy", Locale.US) }
    //private val plantDateString by lazy { dateFormat.format(gardenPlanting.plantDate.time) }

    init {
        Log.d(this::class.java.simpleName, "init{}")
        val liveBlogList = blogRepo.getBlogs(isLoggedIn)
        Log.d(TAG, "liveBlogList.size = ${liveBlogList.value?.size}")
        blogList.addSource(liveBlogList, blogList::setValue)
    }

    fun getBlogs() = blogList
}