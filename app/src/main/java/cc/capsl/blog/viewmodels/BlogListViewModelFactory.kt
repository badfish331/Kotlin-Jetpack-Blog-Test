package cc.capsl.blog.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cc.capsl.blog.repos.BlogRepo

class BlogListViewModelFactory(
    private val isLoggedIn: Boolean,
    private val repository: BlogRepo
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BlogListViewModel(isLoggedIn, repository) as T
    }
}