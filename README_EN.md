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

2.  Add the dependency in your module's `build.gradle.kts`(choose one, latest version [![](https://www.jitpack.io/v/wraphex/any-preference.svg)](https://www.jitpack.io/#wraphex/any-preference) ):
    ```kotlin
    dependencies {
        // SharedPreferences
        implementation("com.github.wraphex.any-preference:any-preference-sp:main-SNAPSHOT")
        // or MMKV
        implementation("com.github.wraphex.any-preference:any-preference-mmkv:main-SNAPSHOT")    
    }
    ```

## Quick Start

1. Initialize in `Application.onCreate()`:
    ```kotlin
    class MyApplication : Application() {
        override fun onCreate() {
            super.onCreate()
            AnyPreference.initialize(this)
        }
    }
    ```

2. Declare properties using `preference`:
    ```kotlin
    val user1 = User(1, "Alice")
    val user2 = User(2, "Bob")

    // Basic type
    var age by preference(defaultValue = 0)

    // Custom object
    var userPref by preference(defaultValue = user1)

    // Nullable with type
    var userNullable by preference(defaultValue = null as User?)

    // Custom key name
    var darkMode by preference(key = "dark_mode", defaultValue = false)

    fun usage() {
        val user = User(3, "Carol")
        // set: save like variable assignment
        userPref = user
        // get: read like getting variable value
        Log.d(TAG, "preference: $user $userPref")
        assertEquals(user, userPref)
        // remove: assign null to delete key-value
        userPref = null
    }
    ```

## Architecture

- **Core Layer** (`main` source set)
  - [`AnyPreferenceDelegate<T>`](anypreference/src/main/kotlin/wraphex/anypreference/AnyPreferenceDelegate.kt): Abstract base class implementing `getValue/setValue` read/write logic, handling primitive type storage and Gson JSON serialization
  - [`BaseAnyPreferences`](anypreference/src/main/kotlin/wraphex/anypreference/BaseAnyPreferences.kt): Base class managing Context lifecycle and initialization
  - [`AnyPreferenceFactory`](anypreference/src/main/kotlin/wraphex/anypreference/AnyPreferenceFactory.kt): Provides inline function `preference` to create delegate instances

- **SharedPreferences Implementation** (`sp` source set)
  - [`AnyPreferenceDelegateImpl<T>`](anypreference/src/sp/kotlin/wraphex/anypreference/AnyPreferenceDelegateImpl.kt): Inherits from `AnyPreferenceDelegate`, uses `SharedPreferences` as storage backend
  - [`AnyPreferences`](anypreference/src/sp/kotlin/wraphex/anypreference/AnyPreferences.kt): Inherits from `BaseAnyPreferences`, singleton object managing global Context

- **MMKV Implementation** (`mmkv` source set)
  - [`AnyPreferenceDelegateImpl<T>`](anypreference/src/mmkv/kotlin/wraphex/anypreference/AnyPreferenceDelegateImpl.kt): Inherits from `AnyPreferenceDelegate`, uses `MMKV` as storage backend
  - [`AnyPreferences`](anypreference/src/mmkv/kotlin/wraphex/anypreference/AnyPreferences.kt): Inherits from `BaseAnyPreferences`, initializes MMKV and manages global Context

- **Data Flow**:
  ```
  User Property Declaration → preference() → AnyPreferenceDelegateImpl 
                          → AnyPreferenceDelegate.getValue/setValue 
                          → SharedPreferences/MMKV
  ```

## Notes

- **Key Name Rules**: Default uses property name as key name, can be customized via `key` parameter
- **Storage Filename**:
  - SharedPreferences: Default filename is `{packageName}_preferences`, can be specified via `name` parameter
  - MMKV: Default uses `defaultMMKV()`, can specify different MMKV ID via `name` parameter