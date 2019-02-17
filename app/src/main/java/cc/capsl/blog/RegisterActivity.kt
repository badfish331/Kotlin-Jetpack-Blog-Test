package cc.capsl.blog

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import cc.capsl.blog.databinding.ActivityRegisterBinding
import cc.capsl.blog.models.data.BlogUser
import cc.capsl.blog.utils.AppUtils
import cc.capsl.blog.utils.InjectorUtils
import cc.capsl.blog.viewmodels.RegisterViewModel
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var registerViewModel: RegisterViewModel
    lateinit var emailLayout: TextInputLayout
    lateinit var pwdLayout: TextInputLayout
    lateinit var firstNameLayout: TextInputLayout
    lateinit var lastNameLayout: TextInputLayout
    lateinit var confirmPwdLayout: TextInputLayout
    lateinit var uploadTxt: AppCompatTextView
    lateinit var userPhoto: AppCompatImageButton
    var newUser: BlogUser? = null
    var errs = 0
    var uri = "0"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = InjectorUtils.getRegisterViewModelFactory(this)
        registerViewModel = ViewModelProviders.of(this, factory).get(RegisterViewModel::class.java)
        val binding: ActivityRegisterBinding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        emailLayout = binding.emailLayout
        pwdLayout = binding.pwdLayout
        firstNameLayout = binding.pwdLayout
        lastNameLayout = binding.pwdLayout
        confirmPwdLayout = binding.pwdLayout
        uploadTxt = binding.tapPhotoBtn
        uploadTxt.setOnClickListener(this)
        userPhoto = binding.userPhoto
        userPhoto.setOnClickListener(this)
        binding.tapPhotoBtn.setOnClickListener(this)
        submitBtn.setOnClickListener(this)
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.userPhoto -> {
                getPhoto()
            }
            R.id.tapPhotoBtn -> {
                getPhoto()
            }
            R.id.submitBtn -> {
                newUser = BlogUser(
                    email.text.toString(),
                    password.text.toString(),
                    firstName.text.toString(),
                    lastName.text.toString(),
                    uri
                )
                val blogUser = registerViewModel.getUser(newUser!!.userId)
                if (fieldsOk(newUser!!)) {
                    if (blogUser == null) {
                        newUser!!.imageId = uri
                        Log.d(this::class.java.simpleName, "$newUser imageId: ${newUser!!.imageId}")
                        registerViewModel.registerUser(newUser!!)
                        setResult(Activity.RESULT_OK)
                        finish()
                    } else {
                        emailLayout.error = "${newUser!!.userId} is already registered.\n"
                    }
                }
            }
        }
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

    private fun fieldsOk(user: BlogUser): Boolean {
        Log.d(this::class.java.simpleName, "fieldsOk(): ${user.userId}")
        errs = 0
        emailLayout.error = null
        pwdLayout.error = null
        confirmPwdLayout.error = null
        firstNameLayout.error = null
        lastNameLayout.error = null
        if (TextUtils.isEmpty(user.userId)) {
            errs++
            emailLayout.error = getString(R.string.required_email)
        } else if (!AppUtils.isEmailValid(user.userId)) {
            errs++
            emailLayout.error = getString(R.string.invalid_email)
        }
        if (TextUtils.isEmpty(password.text.toString())) {
            errs++
            pwdLayout.error = getString(R.string.required_pwd)
        } else if (TextUtils.isEmpty(confirmPassword.text.toString())) {
            errs++
            confirmPwdLayout.error = getString(R.string.required_conf_pwd)
        } else {
            if (user.password != confirmPassword.text.toString()) {
                errs++
                pwdLayout.error = getString(R.string.pwd_no_match)
                confirmPwdLayout.error = getString(R.string.pwd_no_match)
            }
        }
        if (TextUtils.isEmpty(user.firstName)) {
            errs++
            firstNameLayout.error = getString(R.string.required_first_name)
        }
        if (TextUtils.isEmpty(user.lastName)) {
            errs++
            lastNameLayout.error = getString(R.string.required_last_name)
        }
        return errs == 0
    }
}