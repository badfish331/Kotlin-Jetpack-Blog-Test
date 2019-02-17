package cc.capsl.blog.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import cc.capsl.blog.models.LoginModel
import cc.capsl.blog.models.data.BlogUser
import cc.capsl.blog.repos.UserRepo

class LoginViewModel(val userRepo: UserRepo): ViewModel() {

    fun getUser(loginModel: LoginModel): BlogUser? {
        Log.d(this::class.java.simpleName, "getUser(): " + loginModel.email)
        return userRepo.getUser(loginModel.email)
    }
}