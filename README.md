#  AnyPreferences
[![](https://www.jitpack.io/v/wraphex/any-preference.svg)](https://www.jitpack.io/#wraphex/any-preference)
[![](https://img.shields.io/github/issues/wraphex/any-preference.svg)](https://github.com/wraphex/any-preference/issues)
![](https://img.shields.io/github/license/wraphex/any-preference.svg)

---

**简体中文** | [English](README_EN.md)

一个轻量的 Android 键值存储库，利用 Kotlin 属性委托和 Gson 序列化，让存储任意类型的变量像赋值一样简单。支持
SharedPreferences 与 MMKV 两种后端，自动处理类型与序列化。

## 特性

- **类型安全**：通过 Kotlin reified 泛型与 `KType` 在编译时捕获类型信息
- **属性委托**：使用 `by` 关键字实现简洁的读写 API
- **复杂类型支持**：非基本类型通过 Gson 自动 JSON 序列化
- **多后端**：可选 SharedPreferences 或 MMKV 实现

## 集成

1. 在项目根目录的 `settings.gradle.kts` 中添加 JitPack 仓库：
    ```kts
    dependencyResolutionManagement {
        repositories {
            //...
            maven { url = uri("https://jitpack.io") }
        }
    }
    ```

2. 在模块的 `build.gradle.kts` 中添加依赖（选择其中一个，最新版本 [![](https://www.jitpack.io/v/wraphex/any-preference.svg)](https://www.jitpack.io/#wraphex/any-preference) ）：
    ```kts
    dependencies {
        // SharedPreferences
        implementation("com.github.wraphex.any-preference:any-preference-sp:main-SNAPSHOT")
        // or MMKV
        implementation("com.github.wraphex.any-preference:any-preference-mmkv:main-SNAPSHOT")    
   }
    ```

## 快速开始

1. 在 `Application.onCreate()` 中初始化：
    ```kotlin
    class MyApplication : Application() {
        override fun onCreate() {
            super.onCreate()
            AnyPreference.initialize(this)
        }
    }
    ```

2. 使用 `preference` 声明属性：
    ```kotlin
    val user1 = User(1, "Alice")
    val user2 = User(2, "Bob")

    // 基本类型
    var age by preference(defaultValue = 0)

    // 自定义对象
    var userPref by preference(defaultValue = user1)

    // 带类型空值
    var userNullable by preference(defaultValue = null as User?)

    // 自定义键名
    var darkMode by preference(key = "dark_mode", defaultValue = false)

    fun usage() {
        val user = User(3, "Carol")
        // set: 像给变量赋值一样保存
        userPref = user
        // get: 像获取变量值一样读取
        Log.d(TAG, "preference: $user $userPref")
        assertEquals(user, userPref)
        // remove: 赋值为null删除键值
        userPref = null
    }
    ```

## 架构

- **核心层**（`main` 源码集）
  - [`AnyPreferenceDelegate<T>`](anypreference/src/main/kotlin/wraphex/anypreference/AnyPreferenceDelegate.kt)：抽象基类，实现 `getValue/setValue` 读写逻辑，处理基本类型存储与 Gson JSON 序列化
  - [`BaseAnyPreferences`](anypreference/src/main/kotlin/wraphex/anypreference/BaseAnyPreferences.kt)：基类，管理 Context 生命周期与初始化
  - [`AnyPreferenceFactory`](anypreference/src/main/kotlin/wraphex/anypreference/AnyPreferenceFactory.kt)：提供内联函数 `preference` 创建委托实例

- **SharedPreferences 实现**（`sp` 源码集）
  - [`AnyPreferenceDelegateImpl<T>`](anypreference/src/sp/kotlin/wraphex/anypreference/AnyPreferenceDelegateImpl.kt)：继承自 `AnyPreferenceDelegate`，使用 `SharedPreferences` 作为存储后端
  - [`AnyPreferences`](anypreference/src/sp/kotlin/wraphex/anypreference/AnyPreferences.kt)：继承自 `BaseAnyPreferences`，单例对象，管理全局 Context

- **MMKV 实现**（`mmkv` 源码集）
  - [`AnyPreferenceDelegateImpl<T>`](anypreference/src/mmkv/kotlin/wraphex/anypreference/AnyPreferenceDelegateImpl.kt)：继承自 `AnyPreferenceDelegate`，使用 `MMKV` 作为存储后端
  - [`AnyPreferences`](anypreference/src/mmkv/kotlin/wraphex/anypreference/AnyPreferences.kt)：继承自 `BaseAnyPreferences`，初始化 MMKV 并管理全局 Context

- **数据流**：
  ```
  用户属性声明 → preference() → AnyPreferenceDelegateImpl 
              → AnyPreferenceDelegate.getValue/setValue 
              → SharedPreferences/MMKV
  ```

## 注意

- **键名规则**：默认使用属性名作为键名，可通过 `key` 参数自定义
- **存储文件名**：
  - SharedPreferences：默认文件名为 `{packageName}_preferences`，可通过 `name` 参数指定
  - MMKV：默认使用 `defaultMMKV()`，可通过 `name` 参数指定不同的 MMKV ID
