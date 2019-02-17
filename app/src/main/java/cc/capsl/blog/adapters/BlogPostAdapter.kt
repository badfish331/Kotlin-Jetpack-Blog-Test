package cc.capsl.blog.adapters

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cc.capsl.blog.ViewBlogActivity
import cc.capsl.blog.databinding.ListItemBlogBinding
import cc.capsl.blog.models.data.BlogPost
import com.google.gson.Gson

class BlogPostAdapter(private val activity: Activity, private val currentUser: String) : ListAdapter<BlogPost, BlogPostAdapter.ViewHolder>(BlogDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val blog = getItem(position)
        holder.apply {
            bind(createOnClickListener(blog), blog)
            itemView.tag = blog
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListItemBlogBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    private fun createOnClickListener(blog: BlogPost): View.OnClickListener {
        return View.OnClickListener {
            Log.d(this::class.java.simpleName, "blogId to pass: ${blog.blogPostId}")
            val intent = Intent(activity, ViewBlogActivity::class.java)
            intent.putExtra("blogPost", Gson().toJson(blog))
            intent.putExtra("currentUser", currentUser)
            activity.startActivity(intent)
        }
    }

    class ViewHolder(
        private val binding: ListItemBlogBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listener: View.OnClickListener, item: BlogPost) {
            binding.apply {
                clickListener = listener
                blog = item
                executePendingBindings()
            }
        }
    }

}

private class BlogDiffCallback : DiffUtil.ItemCallback<BlogPost>() {

    override fun areItemsTheSame(oldItem: BlogPost, newItem: BlogPost): Boolean {
        return oldItem.blogPostId == newItem.blogPostId
    }

    override fun areContentsTheSame(oldItem: BlogPost, newItem: BlogPost): Boolean {
        return oldItem == newItem
    }
}