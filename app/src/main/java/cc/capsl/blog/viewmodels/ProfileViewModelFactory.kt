package cc.capsl.blog.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cc.capsl.blog.repos.BlogRepo
import cc.capsl.blog.repos.UserRepo

class ProfileViewModelFactory(
    private val currentUser: String,
    private val targetId: String,
    private val blogRepo: BlogRepo,
    private val userRepo: UserRepo
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProfileViewModel(currentUser, targetId, blogRepo, userRepo) as T
    }
}