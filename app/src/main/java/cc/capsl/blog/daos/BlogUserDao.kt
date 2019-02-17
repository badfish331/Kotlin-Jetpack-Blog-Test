package cc.capsl.blog.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import cc.capsl.blog.models.data.BlogUser

/**
 * this is the data access object for the [BlogUser] class.
 * @author Xyldrun Jacob
 */
@Dao
interface BlogUserDao {

    @Query("SELECT * from users ORDER BY lastName ASC")
    fun getAllBlogUsers(): LiveData<List<BlogUser>>

    /**
     * this is the method for getting a blog user via their user email adress.
     * @param userId -> the id of the user.
     */
    @Query("SELECT * from users WHERE userId = :email")
    fun getUser(email: String): BlogUser

    /**
     * this is the method for adding a blog user.
     * the transaction is aborted if the user already exists.
     * @param blogUser -> the blog user to add.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(blogUser: BlogUser)

    /**
     * this is the method for deleting a blog user.
     * @param email -> the email address of the blog user to be deleted.
     */
    @Query("DELETE from users WHERE userId = :email")
    fun deleteUser(email: String)

    /**
     * this is the method for deleting all blog users.
     */
    @Query("DELETE FROM users")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(users: List<BlogUser>)
}