package cc.capsl.blog.workers

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import cc.capsl.blog.db.BlogDb
import cc.capsl.blog.models.data.BlogPost
import cc.capsl.blog.models.data.BlogUser
import cc.capsl.blog.utils.BLOGS_DATA
import cc.capsl.blog.utils.USERS_DATA
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader

class BlogDbWorker(
    context: Context,
    workerParameters: WorkerParameters
): Worker(context, workerParameters) {
    private val TAG by lazy { BlogDbWorker::class.java.simpleName }

    override fun doWork(): Result {
        Log.d(TAG, "dowWork();")
        return try {
            applicationContext.assets.open(BLOGS_DATA).use { inputStream ->
                JsonReader(inputStream.reader()).use { jsonReader ->
                    val type = object : TypeToken<List<BlogPost>>() {}.type
                    val blogList: List<BlogPost> = Gson().fromJson(jsonReader, type)
                    val database = BlogDb.getInstance(applicationContext)
                    database.blogPostDao().insertAll(blogList)
                    Result.success()
                }
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Error populating database", ex)
            Result.failure()
        }
    }
}