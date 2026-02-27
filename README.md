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

2. 在模块的 build.gradle.kts 中添加依赖（选择其中一个）：
    ```kts
    dependencies {
        // SharedPreferences
        implementation("com.github.wraphex.any-preference:any-preference-sp:main-SNAPSHOT")
        // or MMKV
        implementation("com.github.wraphex.any-preference:any-preference-mmkv:main-SNAPSHOT")    
   }
    ```

## 快速开始

### SharedPreferences 后端

```kotlin
class MainActivity : AppCompatActivity() {
    // 基本类型
    private var userAge by preference(this, 0)
    // 自定义对象
    private var userData by preference(this, User(1, "Default", 111))
    // 带类型空值
    private var userData2 by preference(this, null as User?)
    // 自定义键名
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

### MMKV 后端

1. 在 `Application.onCreate()` 中初始化 MMKV：
    ```kotlin
    class MyApplication : Application() {
        override fun onCreate() {
            super.onCreate()
            MMKV.initialize(this)
        }
    }
    ```

2. 使用 `preference` 声明属性，用法与 SharPreferences 后端基本相同：
    ```kotlin
    class SettingsViewModel {
        private var cache by preference(CacheData(emptyList()))
        private var darkMode by preference(key = "dark_mode", defaultValue = false)
        
        fun update(data: CacheData) { cache = data }
    }
    ```

## 架构

- `AnyPreferenceDelegate<T>`：抽象基类，实现 `getValue/setValue` 读写逻辑
- `AnyPreferenceSpImpl<T>`：SharedPreferences 实现
- `AnyPreferenceMmkvImpl<T>`：MMKV 实现
- 内联函数 `preference` 提供便捷的构造方式

## 注意

- MMKV 为可选后端，未初始化时使用会抛出 `RuntimeException`
- 默认键名使用属性名，可通过 `key` 参数覆盖
- 默认 SharedPreferences 文件名为 `{packageName}_preferences`
