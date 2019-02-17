package cc.capsl.blog.db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import cc.capsl.blog.daos.BlogPostDao
import cc.capsl.blog.daos.BlogUserDao
import cc.capsl.blog.models.data.BlogPost
import cc.capsl.blog.models.data.BlogUser
import cc.capsl.blog.utils.Converters
import cc.capsl.blog.utils.DATABASE_NAME
import cc.capsl.blog.utils.TAG
import cc.capsl.blog.workers.BlogDbWorker
import cc.capsl.blog.workers.UserDbWorker

/**
 * TODO: don't forget to increment version number every time you update the schema!!!
 */
@Database(entities = [BlogUser::class, BlogPost::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class BlogDb : RoomDatabase() {

    abstract fun blogUserDao(): BlogUserDao

    abstract fun blogPostDao(): BlogPostDao

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: BlogDb? = null

        fun getInstance(context: Context): BlogDb {
            Log.d(TAG, "getInstance();")
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        // Create and pre-populate the database. See this article for more details:
        // https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
        private fun buildDatabase(context: Context): BlogDb {
            Log.d(TAG, "buildDatabase();")
            return Room.databaseBuilder(context, BlogDb::class.java, DATABASE_NAME)
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        Log.d(TAG, "onCreate(): $db")
                        val request1 = OneTimeWorkRequestBuilder<UserDbWorker>().build()
                        //val request2 = OneTimeWorkRequestBuilder<BlogDbWorker>().build()
                        WorkManager.getInstance().enqueue(request1)
                        //WorkManager.getInstance().enqueue(request2)
                    }
                })
                //.fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
        }
    }
}