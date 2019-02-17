package cc.capsl.blog

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import cc.capsl.blog.databinding.ActivityLoginBinding
import cc.capsl.blog.models.LoginModel
import cc.capsl.blog.models.data.BlogUser
import cc.capsl.blog.utils.AppUtils
import cc.capsl.blog.utils.InjectorUtils
import cc.capsl.blog.viewmodels.LoginViewModel
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var loginViewModel: LoginViewModel
    lateinit var emailLayout: TextInputLayout
    lateinit var pwdLayout: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = InjectorUtils.getLoginViewModelFactory(this)
        loginViewModel = ViewModelProviders.of(this,factory).get(LoginViewModel::class.java)
        Log.d(this::class.java.simpleName, "loginViewModel: $loginViewModel")
        val loginBinder: ActivityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        loginBinder.loginBtn.setOnClickListener(this)
        loginBinder.skipBtn.setOnClickListener(this)
        loginBinder.registerBtn.setOnClickListener(this)
        emailLayout = loginBinder.root.findViewById(R.id.emailLayout)
        pwdLayout = loginBinder.root.findViewById(R.id.passwordLayout)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, getString(R.string.reg_success), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, getString(R.string.canceled_registration), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.loginBtn -> {
                emailLayout.error = null
                pwdLayout.error = null
                val loginModel = LoginModel()
                loginModel.email = email.text.toString().trim()
                loginModel.password = password.text.toString().trim()
                if (TextUtils.isEmpty(loginModel.email)) {
                    emailLayout.error = getString(R.string.required_email)
                } else if(!AppUtils.isEmailValid(loginModel.email)) {
                    emailLayout.error = getString(R.string.invalid_email)
                } else if (TextUtils.isEmpty(loginModel.password)) {
                    pwdLayout.error = getString(R.string.required_pwd)
                } else {
                    runOnUiThread {
                        val user = loginViewModel.getUser(loginModel)
                        Log.d(this::class.java.simpleName, "user.value: $user")
                        if (user == null) {
                            emailLayout.error = "${loginModel.email} doesn't exist!"
                        }
                        else {
                            if (user.password == loginModel.password) {
                                goToMain(true, user)
                            } else {
                                pwdLayout.error = getString(R.string.incorrect_pwd)
                            }
                        }
                    }
                }
            }
            R.id.skipBtn -> {
                goToMain(false, null)
            }
            R.id.registerBtn -> {
                startActivityForResult(Intent(applicationContext, RegisterActivity::class.java), 0)
            }
        }
    }

    private fun goToMain(isLoggedIn: Boolean, blogUser: BlogUser?) {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.putExtra("isLoggedIn", isLoggedIn)
        if (isLoggedIn) {
            intent.putExtra("user", Gson().toJson(blogUser))
        }
        startActivity(intent)
        finish()
    }
}