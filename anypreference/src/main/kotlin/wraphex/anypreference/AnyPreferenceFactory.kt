package wraphex.anypreference

import kotlin.reflect.typeOf

/**
 * @see [AnyPreferenceDelegate]
 */
inline fun <reified T> preference(
    name: String? = null,
    key: String? = null,
    defaultValue: T
): AnyPreferenceDelegate<T> {
    return AnyPreferenceDelegateImpl(name, key, defaultValue, typeOf<T>())
}

/**
 * @see [AnyPreferenceDelegate]
 */
inline fun <reified T> preference(
    key: String? = null,
    defaultValue: T
): AnyPreferenceDelegate<T> {
    return preference(null, key, defaultValue)
}

/**
 * @see [AnyPreferenceDelegate]
 */
inline fun <reified T> preference(
    defaultValue: T
): AnyPreferenceDelegate<T> {
    return preference(null, defaultValue)
}
