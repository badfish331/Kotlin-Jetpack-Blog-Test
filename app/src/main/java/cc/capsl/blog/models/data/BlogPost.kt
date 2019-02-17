package cc.capsl.blog.models.data

import androidx.room.*
import java.util.*

@Entity(
    tableName = "blogs" ,
    foreignKeys = [ForeignKey(entity = BlogUser::class, parentColumns = ["userId"], childColumns = ["userId"])],
    indices = [Index("userId")]
)
data class BlogPost(
    @ColumnInfo(name = "userId") val userId: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "isPublic") val isPublic: Boolean,
    @ColumnInfo(name = "datePosted") val datePosted: Calendar = Calendar.getInstance(),
    @ColumnInfo(name = "imageId") val imageId: String = "0"
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var blogPostId: Long = 0

    override fun toString(): String {
        return title
    }
}