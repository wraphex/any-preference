package wraphex.anypreference

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.reflect.KProperty
import kotlin.reflect.KType
import kotlin.reflect.javaType

/**
 * uses SharedPreferences + Gson to store and retrieve any value
 *
 * @param key The key used to store/retrieve the value.
 * @param defaultValue The default value to return if no value is found for the given key.
 * @param type The type of the value being stored, get from defaultValue.
 */
abstract class AnyPreferenceDelegate<T>(
    private val key: String?,
    private val defaultValue: T?,
    private val type: KType,
) {
    private val gson by lazy { Gson() }
    abstract val sharedPreferences: SharedPreferences

    @OptIn(ExperimentalStdlibApi::class)
    @Suppress("UNCHECKED_CAST")
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        val actualKey = key ?: property.name
        val value = sharedPreferences.run {
            when (type.classifier) {
                String::class -> getString(actualKey, defaultValue as String)
                Int::class -> getInt(actualKey, defaultValue as Int)
                Boolean::class -> getBoolean(actualKey, defaultValue as Boolean)
                Float::class -> getFloat(actualKey, defaultValue as Float)
                Long::class -> getLong(actualKey, defaultValue as Long)
                else -> {
                    val jsonString = getString(actualKey, null)
                    val runtimeType = TypeToken.get(type.javaType).type
                    gson.fromJson(jsonString, runtimeType) ?: defaultValue
                }
            }
        }
        return value as T
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        val actualKey = key ?: property.name
        sharedPreferences.edit().run {
            when (value) {
                is String -> putString(actualKey, value)
                is Int -> putInt(actualKey, value)
                is Boolean -> putBoolean(actualKey, value)
                is Float -> putFloat(actualKey, value)
                is Long -> putLong(actualKey, value)
                else -> {
                    val jsonString = gson.toJson(value)
                    putString(actualKey, jsonString)
                }
            }
            apply()
        }
    }
}
