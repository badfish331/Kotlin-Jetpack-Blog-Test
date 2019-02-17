package cc.capsl.blog.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import cc.capsl.blog.models.data.BlogPost
import cc.capsl.blog.models.data.BlogUser
import cc.capsl.blog.repos.BlogRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PostViewModel(
    val blogRepo: BlogRepo
): ViewModel() {

    private val viewModelJob = Job()

    private val viewModelScope = CoroutineScope(Main + viewModelJob)

    override fun onCleared() {
        super.onCleared()
        Log.d(this::class.java.simpleName, "onCleared()")
        viewModelJob.cancel()
    }

    fun postBlog(blogPost: BlogPost) {
        Log.d(this::class.java.simpleName, "postBlog(): $blogPost")
        viewModelScope.launch {
            blogRepo.postBlog(blogPost)
        }
    }
}