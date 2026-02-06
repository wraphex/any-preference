package wraphex.anypreference

import android.content.Context
import android.content.SharedPreferences
import kotlin.reflect.KType
import kotlin.reflect.typeOf

/**
 * Implementation of [AnyPreferenceDelegate] that uses SharedPreferences to store and retrieve any value.
 *
 * @param context The context used to access SharedPreferences.
 * @param name The name of the SharedPreferences file. If null, the default SharedPreferences file will be used.
 * @param key The key used to store/retrieve the value in SharedPreferences.
 * @param defaultValue The default value to return if no value is found for the given key.
 * @param type The type of the value being stored, get from defaultValue.
 */
class AnyPreferenceSpImpl<T>(
    context: Context,
    name: String? = context.packageName + "_preferences",
    key: String?,
    defaultValue: T?,
    type: KType
) : AnyPreferenceDelegate<T>(key, defaultValue, type) {
    override val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(name, Context.MODE_PRIVATE)
    }
}


/**
 * @see [AnyPreferenceSpImpl]
 */
inline fun <reified T : Any?> preference(
    context: Context,
    name: String? = null,
    key: String? = null,
    defaultValue: T
): AnyPreferenceDelegate<T> {
    return AnyPreferenceSpImpl(context, name, key, defaultValue, typeOf<T>())
}

/**
 * @see [AnyPreferenceSpImpl]
 */
inline fun <reified T : Any?> preference(
    context: Context,
    key: String? = null,
    defaultValue: T
): AnyPreferenceDelegate<T> {
    return preference(context, null, key, defaultValue)
}

/**
 * @see [AnyPreferenceSpImpl]
 */
inline fun <reified T : Any?> preference(
    context: Context,
    defaultValue: T
): AnyPreferenceDelegate<T> {
    return preference(context, null, defaultValue)
}
