package wraphex.anypreference

import android.content.Context
import android.content.SharedPreferences
import kotlin.reflect.KType

class AnyPreferenceDelegateImpl<T>(
    name: String? = AnyPreferences.context.packageName + "_preferences",
    key: String?,
    defaultValue: T?,
    type: KType
) : AnyPreferenceDelegate<T>(key, defaultValue, type) {
    override val sharedPreferences: SharedPreferences by lazy {
        AnyPreferences.context.getSharedPreferences(name, Context.MODE_PRIVATE)
    }
}
