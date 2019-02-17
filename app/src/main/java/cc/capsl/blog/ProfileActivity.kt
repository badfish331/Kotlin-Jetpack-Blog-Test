package cc.capsl.blog

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import cc.capsl.blog.adapters.BlogPostAdapter
import cc.capsl.blog.databinding.ActivityProfileBinding
import cc.capsl.blog.utils.InjectorUtils
import cc.capsl.blog.viewmodels.ProfileViewModel

class ProfileActivity : AppCompatActivity() {

    lateinit var viewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val currentUser = intent.getStringExtra("currentUser")
        val targetId = intent.getStringExtra("targetId")
        Log.d(this::class.java.simpleName, "currentUser: $currentUser")
        Log.d(this::class.java.simpleName, "targetId: $targetId")
        val factory = InjectorUtils.getProfileViewModelFactory(this, currentUser, targetId)
        viewModel = ViewModelProviders.of(this, factory).get(ProfileViewModel::class.java)
        val author = viewModel.getProfile()
        Log.d(this::class.java.simpleName, "retrieved author: $author")
        val adapter = BlogPostAdapter(this, currentUser)
        val binding: ActivityProfileBinding = DataBindingUtil.setContentView(this, R.layout.activity_profile)
        val uri = author.imageId
        Log.d(this::class.java.simpleName, "author imageId: $uri")
        if (uri != "0") {
            val parsed = Uri.parse(uri)
            Log.d(this::class.java.simpleName, "parsed uri: $parsed")
            binding.userImg.setImageURI(parsed)
        }
        binding.blogList.adapter = adapter
        subscribeUi(adapter)
        binding.apply {
            profile = author
        }
    }

    private fun subscribeUi(adapter: BlogPostAdapter) {
        Log.d(this::class.java.simpleName, "subscribeUi();")
        viewModel.getBlogs().observe(this, Observer { result ->
            if (result != null && result.isNotEmpty()) {
                Log.d(this::class.java.simpleName, "blogs retrieved: ${result.size}")
                adapter.submitList(result)
            } else{
                Log.d(this::class.java.simpleName, "no blogs found!")
            }
        })
    }
}