package cc.capsl.blog.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cc.capsl.blog.repos.BlogRepo

class PostViewModelFactory(
    private val blogRepo: BlogRepo
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PostViewModel(blogRepo) as T
    }
}