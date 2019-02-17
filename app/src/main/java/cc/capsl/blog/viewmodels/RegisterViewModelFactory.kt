package cc.capsl.blog.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cc.capsl.blog.repos.UserRepo

class RegisterViewModelFactory(val userRepo: UserRepo): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RegisterViewModel(userRepo) as T
    }
}