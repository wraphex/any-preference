# AnyPreferences
[![](https://www.jitpack.io/v/wraphex/any-preference.svg)](https://www.jitpack.io/#wraphex/any-preference)
[![](https://img.shields.io/github/issues/wraphex/any-preference.svg)](https://github.com/wraphex/any-preference/issues)
![](https://img.shields.io/github/license/wraphex/any-preference.svg)

---

[简体中文](README.md) | **English**

A lightweight Android key-value storage library that leverages Kotlin property delegation and Gson serialization, making storing variables of arbitrary types as simple as assignment. It supports both SharedPreferences and MMKV backends, automatically handling type conversion and serialization.

## Features

- **Type Safe**: Captures type information at compile time using Kotlin reified generics and `KType`.
- **Property Delegation**: Achieves a clean read/write API using the `by` keyword.
- **Complex Type Support**: Non-primitive types are automatically serialized to JSON via Gson.
- **Multi-Backend**: Offers optional SharedPreferences or MMKV implementations.

## Integration

1.  Add the JitPack repository in your project-level `settings.gradle.kts`:
    ```kotlin
    dependencyResolutionManagement {
        repositories {
            // ...
            maven { url = uri("https://jitpack.io") }
        }
    }
    ```

2.  Add the dependency in your module's `build.gradle.kts`(choose one):
    ```kotlin
    dependencies {
        // SharedPreferences
        implementation("com.github.wraphex.any-preference:any-preference-sp:main-SNAPSHOT")
        // or MMKV
        implementation("com.github.wraphex.any-preference:any-preference-mmkv:main-SNAPSHOT")    
    }
    ```

## Quick Start

### SharedPreferences Backend

```kotlin
class MainActivity : AppCompatActivity() {
    // Basic type
    private var userAge by preference(this, 0)
    // Custom object
    private var userData by preference(this, User(1, "Default", 111))
    // Nullable typed value
    private var userData2 by preference(this, null as User?)
    // Custom key name
    private var darkMode by preference(this, "dark_mode", false)

    fun demo() {
        userAge = 30
        userData = User(1, "Alice", 666)
        userData2 = User(2, "Bob", 666)

        Log.d("Pref", "User: ${userData.name}, Age: $userAge")
        Log.d("Pref", "Data: $userData")
    }
}
```

### MMKV Backend

1. Initialize MMKV in your `Application.onCreate()`:
    ```kotlin
    class MyApplication : Application() {
        override fun onCreate() {
            super.onCreate()
            MMKV.initialize(this)
        }
    }
    ```

2. Declare properties using `preference`. The usage is largely identical to the SharedPreferences backend:
    ```kotlin
    class SettingsViewModel {
        private var cache by preference(CacheData(emptyList()))
        private var darkMode by preference(key = "dark_mode", defaultValue = false)
        
        fun update(data: CacheData) { cache = data }
    }
    ```

## Architecture

- `AnyPreferenceDelegate<T>`: Abstract base class implementing the `getValue` / `setValue` logic.
- `AnyPreferenceSpImpl<T>`: SharedPreferences implementation.
- `AnyPreferenceMmkvImpl<T>`: MMKV implementation.
- Inline functions `preference` provide convenient construction methods.

## Notes

- Default keys use the property name, which can be overridden via the `key` parameter.
- Default SharedPreferences filename is `{packageName}_preferences`.