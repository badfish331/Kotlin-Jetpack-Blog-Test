package cc.capsl.blog.viewmodels

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import cc.capsl.blog.models.data.BlogPost
import cc.capsl.blog.models.data.BlogUser
import cc.capsl.blog.repos.BlogRepo
import cc.capsl.blog.repos.UserRepo
import cc.capsl.blog.utils.TAG

class ProfileViewModel(
    currentUser: String,
    targetId: String,
    blogRepo: BlogRepo,
    userRepo: UserRepo
): ViewModel() {

    private val blogList = MediatorLiveData<List<BlogPost>>()
    private val profile: BlogUser

    init {
        Log.d(this::class.java.simpleName, "init{}: $currentUser")
        profile = userRepo.getUser(targetId)
        Log.d(this::class.java.simpleName, "retrieved profile of: $profile")
        val liveBlogList = if (targetId == currentUser) {
            blogRepo.getUserBlogs(targetId, true)
        } else {
            blogRepo.getUserBlogs(targetId, false)
        }
        Log.d(TAG, "liveBlogList.size = ${liveBlogList.value?.size}")
        blogList.addSource(liveBlogList, blogList::setValue)
    }

    fun getBlogs() = blogList

    fun getProfile() = profile
}