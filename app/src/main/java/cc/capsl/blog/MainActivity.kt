package cc.capsl.blog

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.core.view.GravityCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import cc.capsl.blog.databinding.ActivityMainBinding
import cc.capsl.blog.databinding.NavHeaderMainBinding
import cc.capsl.blog.models.data.BlogUser
import com.google.gson.Gson

/**
 * this is the main activity class that shows the default dashboard of the app, displaying a list of blog posts.
 * @author Xyldrun Jacob
 */
class MainActivity : AppCompatActivity() {

    private val defaultUser = BlogUser(
        "test@anon.com",
        "asdf",
        "Anonymous",
        "User",
        "0"
    )

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var user: BlogUser
    private lateinit var name: AppCompatTextView
    private lateinit var email: AppCompatTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainBinding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        drawerLayout = mainBinding.drawerLayout

        navController = Navigation.findNavController(this, R.id.blog_nav_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)

        // Set up ActionBar
        setSupportActionBar(mainBinding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Set up navigation menu
        mainBinding.navigationView.setupWithNavController(navController)

        if (intent != null && intent.hasExtra("isLoggedIn")) {
            initView(intent.getBooleanExtra("isLoggedIn", false), mainBinding)
        } else{
            initView(false, mainBinding)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun initView(isLoggedIn: Boolean, mainBinding: ActivityMainBinding) {
        Log.d(this::class.java.simpleName, "initView(): $isLoggedIn")
        user = if (isLoggedIn) {
            Gson().fromJson(intent.getStringExtra("user"), BlogUser::class.java)
        } else{
            defaultUser
        }
        Log.d(this::class.java.simpleName, "current user: $user")
        val headerBinding: NavHeaderMainBinding = DataBindingUtil.inflate(layoutInflater, R.layout.nav_header_main, mainBinding.navigationView, false)
        mainBinding.navigationView.addHeaderView(headerBinding.root)
        headerBinding.email.text = user.userId
        headerBinding.name.text = user.toString()
        if (user.imageId != "0") {
            Log.d(this::class.java.simpleName, "user image available: ${user.imageId}")
            headerBinding.userImg.setImageURI(Uri.parse(user.imageId))
        }
    }

    fun getSessionUser(): BlogUser {
        return user
    }

    companion object {

        fun getUser(activity: MainActivity): BlogUser {
            return activity.getSessionUser()
        }

        fun isLoggedIn(activity: MainActivity): Boolean {
            val public = activity.getSessionUser().userId != "test@anon.com"
            Log.d("MainActivity", "isLoggedIn()? $public")
            return public
        }
    }
}
