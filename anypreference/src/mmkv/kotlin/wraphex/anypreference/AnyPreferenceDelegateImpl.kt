package wraphex.anypreference

import android.content.SharedPreferences
import com.tencent.mmkv.MMKV
import kotlin.reflect.KType
import kotlin.reflect.typeOf

class AnyPreferenceDelegateImpl<T>(
    id: String?,
    key: String?,
    defaultValue: T?,
    type: KType
) : AnyPreferenceDelegate<T>(key, defaultValue, type) {
    override val sharedPreferences: SharedPreferences by lazy {
        if (id == null) MMKV.defaultMMKV()
        else MMKV.mmkvWithID(id)
    }
}
