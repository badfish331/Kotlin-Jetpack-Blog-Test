package cc.capsl.blog.models.data

import androidx.room.Embedded
import androidx.room.Relation

class UserBlogs {

    @Embedded
    lateinit var user: BlogUser

    @Relation(parentColumn = "id", entityColumn = "userId")
    var userPosts: List<BlogPost> = arrayListOf()
}