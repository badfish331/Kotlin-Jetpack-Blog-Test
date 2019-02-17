package cc.capsl.blog.repos

import android.util.Log
import androidx.lifecycle.LiveData
import cc.capsl.blog.daos.BlogPostDao
import cc.capsl.blog.models.data.BlogPost
import cc.capsl.blog.utils.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BlogRepo private constructor(private val blogPostDao: BlogPostDao) {

    private fun getAllBlogs() = blogPostDao.getBlogPosts()

    private fun getAllBlogsByUser(email: String) = blogPostDao.getAllPostsByUser(email)

    private fun getPublicPostsByUser(email: String) = blogPostDao.getPublicPostsByUser(email)

    fun getBlogs(isLoggedIn: Boolean) = if (isLoggedIn) {
        getAllBlogs()
    } else{
        blogPostDao.getPublicPosts(true)
    }

    fun getUserBlogs(email: String, sameUser: Boolean): LiveData<List<BlogPost>> {
        return if (sameUser) {
            getAllBlogsByUser(email)
        } else {
            getPublicPostsByUser(email)
        }
    }

    suspend fun postBlog(blogPost: BlogPost) {
        withContext(Dispatchers.IO) {
            val id = blogPostDao.insert(blogPost)
            Log.d(TAG, "new blog post id: $id")
        }
    }

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: BlogRepo? = null

        fun getInstance(blogPostDao: BlogPostDao) =
            instance ?: synchronized(this) {
                instance ?: BlogRepo(blogPostDao).also { instance = it }
            }
    }
}