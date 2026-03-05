package wraphex.anypreference

import android.content.Context
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
open class AnyPreferenceTest {
    val TAG = "AnyPreferenceTest"
    var context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    init {
        AnyPreferences.initialize(context)
    }

    val user1 = User(1, "Alice")
    val user2 = User(2, "Bob")

    // Basic type
    var age by preference(defaultValue = 0)

    // Custom object
    var userPref by preference(defaultValue = user1)

    // Nullable typed value
    var userNullable by preference(defaultValue = null as User?)

    // Custom key name
    var darkMode by preference(key = "dark_mode", defaultValue = false)

    @Test
    fun testSharedPreferences() {
        val user = User(3, "Carol")
        // set: Save it as if assigning a value to a variable
        userPref = user
        // get: Read it just like retrieving a variable value
        Log.d(TAG, "pairPref: $user $userPref")
        assertEquals(user, userPref)
        // remove: Remove the value
        userPref = null
    }

    @Test
    fun testPerformance() {
        runPerformanceTest("basic type") {
            age = 1
            age = 2
        }
        runPerformanceTest("custom type") {
            userPref = user1
            userPref = user2
        }
    }

    fun runPerformanceTest(testName: String = "unnamed", times: Int = 1000, test: (i: Int) -> Unit) {
        val start = System.currentTimeMillis()
        for (i in 0..times) {
            test(i)
        }
        val end = System.currentTimeMillis()
        Log.d(TAG, "${end - start} ms for $testName")
    }
}