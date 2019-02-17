package cc.capsl.blog

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cc.capsl.blog.databinding.ActivityViewBlogBinding
import cc.capsl.blog.models.data.BlogPost
import com.google.gson.Gson

class ViewBlogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val blogPost = Gson().fromJson(intent.getStringExtra("blogPost"), BlogPost::class.java)
        val currentUser = intent.getStringExtra("currentUser")
        Log.d(this::class.java.simpleName, "blog title: ${blogPost.title}")
        val binding: ActivityViewBlogBinding = DataBindingUtil.setContentView(this, R.layout.activity_view_blog)
        binding.apply {
            blog = blogPost
            author.setOnClickListener {
                val intent = Intent(this@ViewBlogActivity, ProfileActivity::class.java)
                intent.putExtra("targetId", blogPost.userId)
                intent.putExtra("currentUser", currentUser)
                startActivity(intent)
            }
            if (blogPost.imageId != "0") {
                blogImage.setImageURI(Uri.parse(blogPost.imageId))
            }
        }
    }
}