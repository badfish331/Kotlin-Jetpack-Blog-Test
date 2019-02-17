package cc.capsl.blog.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cc.capsl.blog.models.data.BlogPost

@Dao
interface BlogPostDao {

    @Insert
    fun insert(blogPost: BlogPost): Long

    @Query("SELECT * from blogs WHERE isPublic = :isPublic ORDER BY datePosted DESC")
    fun getBlogPosts(isPublic: Boolean = true): LiveData<List<BlogPost>>

    @Query("SELECT * from blogs WHERE isPublic = :isPublic ORDER BY datePosted DESC")
    fun getPostsByVisibility(isPublic: Boolean): LiveData<List<BlogPost>>

    @Query("SELECT * from blogs WHERE isPublic = :isPublic ORDER BY datePosted DESC")
    fun getPublicPosts(isPublic: Boolean): LiveData<List<BlogPost>>

    @Query("SELECT * from blogs WHERE userId = :email ORDER BY datePosted DESC")
    fun getAllPostsByUser(email: String): LiveData<List<BlogPost>>

    @Query("SELECT * from blogs WHERE userId = :email AND isPublic = :isPublic ORDER BY datePosted DESC")
    fun getPublicPostsByUser(email: String, isPublic: Boolean = true): LiveData<List<BlogPost>>

    @Query("DELETE from blogs")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(blogs: List<BlogPost>)
}