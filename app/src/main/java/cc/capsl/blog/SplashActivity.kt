package cc.capsl.blog

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cc.capsl.blog.databinding.ActivitySplashBinding
import cc.capsl.blog.db.BlogDb
import cc.capsl.blog.models.data.BlogUser

/**
 * this is the class for showing a simple splash page, before redirecting to the [LoginActivity]
 * @author Xyldrun Jacob
 */
class SplashActivity : AppCompatActivity() {

    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 3000 // 3 seconds

    override fun onBackPressed() {
        // do nothing when the user presses back.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //DataBindingUtil.setContentView<ActivitySplashBinding>(this, R.layout.activity_splash)
        setContentView(R.layout.activity_splash)
        runOnUiThread {
            val db = BlogDb.getInstance(this)
            db.blogUserDao().insert(BlogUser(
                "test@anon.com",
                "asdf",
                "Anonymous",
                "user",
                "0"
            ))
            mDelayHandler = Handler()
            mDelayHandler!!.postDelayed({
                // launch the next activity once the delay is done:
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }, SPLASH_DELAY)
        }
    }
}