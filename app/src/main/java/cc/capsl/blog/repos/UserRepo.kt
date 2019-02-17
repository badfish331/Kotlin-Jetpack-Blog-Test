package cc.capsl.blog.repos

import android.util.Log
import cc.capsl.blog.daos.BlogUserDao
import cc.capsl.blog.models.data.BlogUser
import cc.capsl.blog.utils.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepo private constructor(private val blogUserDao: BlogUserDao) {

    fun getUser(email: String) = blogUserDao.getUser(email)

//    suspend fun addUser(blogUser: BlogUser) {
//        Log.d(TAG, "addUser(): $blogUser")
//        withContext(Dispatchers.IO) {
//            blogUserDao.insert(blogUser)
//        }
//    }

    fun addUser(blogUser: BlogUser) {
        Log.d(TAG, "addUser(): $blogUser")
        blogUserDao.insert(blogUser)
    }

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: UserRepo? = null

        fun getInstance(blogUserDao: BlogUserDao) =
            instance ?: synchronized(this) {
                instance ?: UserRepo(blogUserDao).also { instance = it }
            }
    }
}