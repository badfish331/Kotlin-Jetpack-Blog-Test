package cc.capsl.blog.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import cc.capsl.blog.MainActivity
import cc.capsl.blog.PostBlogActivity
import cc.capsl.blog.R
import cc.capsl.blog.adapters.BlogPostAdapter
import cc.capsl.blog.databinding.FragmentBlogListBinding
import cc.capsl.blog.models.data.BlogUser
import cc.capsl.blog.utils.InjectorUtils
import cc.capsl.blog.utils.TAG
import cc.capsl.blog.viewmodels.BlogListViewModel
import com.google.android.material.snackbar.Snackbar

class BlogListFragment : Fragment() {

    private lateinit var viewModel: BlogListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentBlogListBinding.inflate(inflater, container, false)
        val context = context ?: return binding.root
        val isLoggedIn = MainActivity.isLoggedIn(activity as MainActivity)
        val user: BlogUser = MainActivity.getUser(activity as MainActivity)
        val factory = InjectorUtils.getBlogListViewModelFactory(context, isLoggedIn)
        viewModel = ViewModelProviders.of(this, factory).get(BlogListViewModel::class.java)

        val adapter = BlogPostAdapter(activity as MainActivity, user.userId)
        binding.blogList.adapter = adapter
        subscribeUi(adapter)

        //setHasOptionsMenu(true)
        binding.fab.setOnClickListener {
            val intent = Intent(context, PostBlogActivity::class.java)
            intent.putExtra("userId", user.userId)
            startActivityForResult(intent, 0)
        }
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(this::class.java.simpleName, "onActivityResult(): $resultCode")
        when (requestCode) {
            0 -> {
                if (resultCode == Activity.RESULT_OK) {
                    Snackbar.make(this.requireView(), getString(R.string.post_success), Snackbar.LENGTH_SHORT).show()
                } else {
                    Snackbar.make(this.requireView(), getString(R.string.post_canceled), Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun subscribeUi(adapter: BlogPostAdapter) {
        Log.d(this::class.java.simpleName, "subscribeUi();")
        viewModel.getBlogs().observe(viewLifecycleOwner, Observer { result ->
            if (result != null && result.isNotEmpty()) {
                Log.d(TAG, "blogs retrieved: ${result.size}")
                adapter.submitList(result)
            } else{
                Log.d(TAG, "no blogs found!")
            }
        })
    }
}