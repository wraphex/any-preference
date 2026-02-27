package wraphex.anypreference

import android.content.SharedPreferences
import java.lang.reflect.Method
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
            val mmkvClass = MmkvReflectionCache.mmkvClass
            if (id == null) {
                MmkvReflectionCache.defaultMMKVMethod.invoke(mmkvClass)
            } else {
                MmkvReflectionCache.mmkvWithIDMethod.invoke(mmkvClass, id)
            } as SharedPreferences
        } catch (e: IllegalStateException) {
            throw RuntimeException("MMKV may not initialized", e)
        }
    }
}

/**
 * @see [AnyPreferenceMmkvImpl]
 */
inline fun <reified T : Any?> preferenceMmkv(
    id: String? = null,
    key: String? = null,
    defaultValue: T
): AnyPreferenceDelegate<T> {
    return AnyPreferenceMmkvImpl(id, key, defaultValue, typeOf<T>())
}

/**
 * @see [AnyPreferenceMmkvImpl]
 */
inline fun <reified T : Any?> preferenceMmkv(
    key: String? = null,
    defaultValue: T
): AnyPreferenceDelegate<T> {
    return preferenceMmkv(null, key, defaultValue)
}

/**
 * @see [AnyPreferenceMmkvImpl]
 */
inline fun <reified T : Any?> preferenceMmkv(
    defaultValue: T
): AnyPreferenceDelegate<T> {
    return preferenceMmkv(null, defaultValue)
}

private object MmkvReflectionCache {
    val mmkvClass: Class<*> by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        try {
            Class.forName("com.tencent.mmkv.MMKV")
        } catch (e: ClassNotFoundException) {
            throw RuntimeException("MMKV not found", e)
        }
    }

    val defaultMMKVMethod: Method by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        mmkvClass.getMethod("defaultMMKV")
    }

    val mmkvWithIDMethod: Method by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        mmkvClass.getMethod("mmkvWithID", String::class.java)
    }
}
