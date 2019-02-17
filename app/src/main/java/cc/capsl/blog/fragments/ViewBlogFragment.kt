package cc.capsl.blog.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import cc.capsl.blog.R

class ViewBlogFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<cc.capsl.blog.databinding.FragmentViewBlogBinding>(inflater, R.layout.fragment_view_blog, container, false)
        return binding.root
    }
}