package wraphex.anypreference

import android.content.SharedPreferences
import com.tencent.mmkv.MMKV
import kotlin.reflect.KType
import kotlin.reflect.typeOf

/**
 * Implementation of [AnyPreferenceDelegate] that uses MMKV to store and retrieve any value.
 *
 * @param id The ID of the MMKV instance. If null, the default MMKV instance will be used.
 * @param key The key to use for storing and retrieving the value.
 * @param defaultValue The default value to return if no value is found for the given key.
 * @param type The type of the value being stored, get from defaultValue.
 */
class AnyPreferenceMmkvImpl<T>(
    id: String?,
    key: String?,
    defaultValue: T?,
    type: KType
) : AnyPreferenceDelegate<T>(key, defaultValue, type) {
    override val sharedPreferences: SharedPreferences by lazy {
        try {
            if (id == null) MMKV.defaultMMKV()
            else MMKV.mmkvWithID(id)
        } catch (e: IllegalStateException) {
            throw RuntimeException("MMKV may not initialized", e)
        }
    }
}

/**
 * @see [AnyPreferenceMmkvImpl]
 */
inline fun <reified T> preference(
    id: String? = null,
    key: String? = null,
    defaultValue: T
): AnyPreferenceDelegate<T> {
    return AnyPreferenceMmkvImpl(id, key, defaultValue, typeOf<T>())
}

/**
 * @see [AnyPreferenceMmkvImpl]
 */
inline fun <reified T> preference(
    key: String? = null,
    defaultValue: T
): AnyPreferenceDelegate<T> {
    return preference(null, key, defaultValue)
}

/**
 * @see [AnyPreferenceMmkvImpl]
 */
inline fun <reified T> preference(
    defaultValue: T
): AnyPreferenceDelegate<T> {
    return preference(null, defaultValue)
}
