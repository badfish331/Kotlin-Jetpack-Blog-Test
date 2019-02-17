/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cc.capsl.blog.utils

import android.content.Context
import android.util.Log
import cc.capsl.blog.db.BlogDb
import cc.capsl.blog.repos.BlogRepo
import cc.capsl.blog.repos.UserRepo
import cc.capsl.blog.viewmodels.*

/**
 * Static methods used to inject classes needed for various Activities and Fragments.
 */
object InjectorUtils {

    private fun getUserRepo(context: Context): UserRepo {
        Log.d(TAG, "getUserRepo();")
        return UserRepo.getInstance(
                BlogDb.getInstance(context.applicationContext).blogUserDao())
    }

    private fun getBlogRepo(context: Context): BlogRepo {
        Log.d(TAG, "getBlogRepo();")
        return BlogRepo.getInstance(
                BlogDb.getInstance(context.applicationContext).blogPostDao())
    }

    fun getProfileViewModelFactory(
        context: Context,
        currentUser: String,
        targetId: String
    ): ProfileViewModelFactory {
        Log.d(TAG, "getBlogListViewModelFactory();")
        val blogRepo = getBlogRepo(context)
        val userRepo = getUserRepo(context)
        return ProfileViewModelFactory(currentUser, targetId, blogRepo, userRepo)
    }

    fun getBlogListViewModelFactory(
        context: Context,
        isLoggedIn: Boolean
    ): BlogListViewModelFactory {
        Log.d(TAG, "getBlogListViewModelFactory();")
        val repo = getBlogRepo(context)
        return BlogListViewModelFactory(isLoggedIn, repo)
    }

    fun getLoginViewModelFactory(
        context: Context
    ): LoginViewModelFactory {
        Log.d(TAG, "getLoginViewModelFactory();")
        val repository = getUserRepo(context)
        return LoginViewModelFactory(repository)
    }

    fun getPostViewModelFactory(
        context: Context
    ): PostViewModelFactory {
        Log.d(TAG, "getPostViewModelFactory();")
        val repository = getBlogRepo(context)
        return PostViewModelFactory(repository)
    }

    fun getRegisterViewModelFactory(
        context: Context
    ): RegisterViewModelFactory {
        Log.d(TAG, "getRegisterViewModelFactory();")
        val repository = getUserRepo(context)
        return RegisterViewModelFactory(repository)
    }
}
