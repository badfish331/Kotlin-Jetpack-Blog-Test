package cc.capsl.blog.models.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * this is the model class for blog users.
 * @author xyldrun jacob
 */
@Entity(tableName = "users")
data class BlogUser(
    @PrimaryKey @ColumnInfo(name = "userId") val userId: String,
    @ColumnInfo(name = "password") val password: String,
    @ColumnInfo(name = "firstName") val firstName: String,
    @ColumnInfo(name = "lastName") val lastName: String,
    @ColumnInfo(name = "imageId") var imageId: String
) {
    override fun toString(): String {
        return "$firstName $lastName"
    }
}