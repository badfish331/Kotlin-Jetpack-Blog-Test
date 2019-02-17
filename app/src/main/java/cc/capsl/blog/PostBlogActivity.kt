package cc.capsl.blog

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import cc.capsl.blog.databinding.ActivityPostBlogBinding
import cc.capsl.blog.models.data.BlogPost
import cc.capsl.blog.utils.InjectorUtils
import cc.capsl.blog.viewmodels.PostViewModel
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textfield.TextInputLayout
import java.util.*

class PostBlogActivity : AppCompatActivity() {

    lateinit var titleLayout: TextInputLayout
    lateinit var isPrivate: MaterialCheckBox
    private lateinit var userPhoto: AppCompatImageView
    var uri = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityPostBlogBinding = DataBindingUtil.setContentView(this, R.layout.activity_post_blog)
        val factory = InjectorUtils.getPostViewModelFactory(this)
        val viewModel = ViewModelProviders.of(this, factory).get(PostViewModel::class.java)
        val userId = intent.getStringExtra("userId")
        Log.d(this::class.java.simpleName, "userId: $userId")
        titleLayout = binding.titleLayout
        isPrivate = binding.isPrivate
        userPhoto = binding.userPhoto
        if (userId == "test@anon.com") {
            isPrivate.visibility = View.GONE
        }
        Log.d(this::class.java.simpleName, "is checkbox visible? ${isPrivate.isVisible}")
        binding.cancelBtn.setOnClickListener { onBackPressed() }
        binding.submitBtn.setOnClickListener {
            val title = binding.title.text.toString()
            val content = binding.content.text.toString()
            titleLayout.error = null
            when {
                TextUtils.isEmpty(title) -> titleLayout.error = getString(R.string.required_title)
                TextUtils.isEmpty(content) -> titleLayout.error = getString(R.string.required_content)
                else -> post(title, content, userId, viewModel)
            }
        }
        binding.photoLayout.setOnClickListener {
            getPhoto()
        }
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            0 -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null && data.data != null) {
                        val retrieved = data.data
                        Log.d(this::class.java.simpleName, "retrieved image uri: $retrieved")
                        userPhoto.setImageURI(retrieved)
                        uri = retrieved.toString()
                    }
                } else {
                    Toast.makeText(this, getString(R.string.choose_pic_canceled), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getPhoto() {
        Log.d(this::class.java.simpleName, "getPhoto();")
        // ref: https://stackoverflow.com/questions/40438537/permission-denied-opening-provider-com-google-android-apps-photos-contentprovi
        //startActivityForResult(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 0)
        val action: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent.ACTION_OPEN_DOCUMENT
        } else {
            Intent.ACTION_PICK
        }
        startActivityForResult(Intent(action, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 0)
    }

    private fun post(title: String, content: String, userId: String, viewModel: PostViewModel) {
        Log.d(this::class.java.simpleName, "post(): $title")
        val publicPost = if (userId == "test@anon.com") {
            true
        } else {
            isPrivate.isVisible && !isPrivate.isChecked
        }
        Log.d(this::class.java.simpleName, "public post? $publicPost")
        val blogPost = BlogPost(userId, title, content, publicPost, Calendar.getInstance(), uri)
        Log.d(this::class.java.simpleName, "blog image uri: ${blogPost.imageId}")
        viewModel.postBlog(blogPost)
        setResult(Activity.RESULT_OK)
        finish()
    }
}