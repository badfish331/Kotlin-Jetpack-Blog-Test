package cc.capsl.blog.models

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR

class RegisterModel : BaseObservable() {

    var email: String = ""
        @Bindable get() = field
        set(email) {
            field = email
            notifyPropertyChanged(BR.email)
        }

    var password: String = ""
        @Bindable get() = field
        set(password) {
            field = password
            notifyPropertyChanged(BR.password)
        }

    var firstName: String = ""
        @Bindable get() = field
        set(firstName) {
            field = firstName
            notifyPropertyChanged(BR.firstName)
        }

    var lastName: String = ""
        @Bindable get() = field
        set(lastName) {
            field = lastName
            notifyPropertyChanged(BR.lastName)
        }

    var imageId: String = "0"
        @Bindable get() = field
        set(imageId) {
            field = imageId
            notifyPropertyChanged(BR.imageId)
        }

}