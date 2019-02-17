package cc.capsl.blog.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import cc.capsl.blog.models.data.BlogUser
import cc.capsl.blog.repos.UserRepo
import cc.capsl.blog.utils.TAG
import kotlinx.coroutines.*

class RegisterViewModel(val userRepo: UserRepo): ViewModel() {

    fun getUser(email: String): BlogUser? {
        Log.d(this::class.java.simpleName, "getUser(): $email")
        return userRepo.getUser(email)
    }

    fun registerUser(blogUser: BlogUser) {
        Log.d(TAG, "registerUser(): $blogUser")
        userRepo.addUser(blogUser)
    }
}