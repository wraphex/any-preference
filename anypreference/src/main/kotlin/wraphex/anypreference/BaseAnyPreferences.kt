package wraphex.anypreference

import android.app.Application
import android.content.Context
import android.util.Log

abstract class BaseAnyPreferences {
    companion object {
        const val TAG = "AnyPreferences"
    }
    lateinit var context: Context

    open fun initialize(context: Context) {
        if (context !is Application) {
            Log.w(TAG, "Using a non-Application context may lead to memory leaks!")
        }
        this.context = context.applicationContext
    }
}