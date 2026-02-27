package wraphex.anypreference

import android.content.Context
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.tencent.mmkv.MMKV
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class AnyPreferenceTest {
    var context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    // null with type when no actual value
    var pairPref by preference(
        context,
        null as Pair<String, String>?
    )

    // null with custom type
    var userPref by preference(
        context,
        null as User?
    )

    // actual value
    var userDefaultPref by preference(
        context,
        User(1, "username", 1)
    )

    // Usage same as preference
    var mmkvPref by preferenceMmkv(null as Pair<String, String>?)

    @Test
    fun testSharedPreferences() {
        val pair = Pair("first", "second")
        // Save it as if assigning a value to a variable
        pairPref = pair
        // Read it just like retrieving a variable value
        Log.d("testSharedPreferences", "pairPref: $pair $pairPref")
        assertEquals(pair, pairPref)
    }

    @Test
    fun testMmkv() {
        MMKV.initialize(context)
        val pair = Pair("first", "second")
        // Usage same as preference
        mmkvPref = pair
        Log.d("testMmkv", "mmkvPref: $pair $mmkvPref")
        assertEquals(pair, mmkvPref)
    }

    @Test
    fun testCustomType() {
        val user = User(1, "username", 1)
        userPref = user
        Log.d("testCustomType", "userPref: $user $userPref")
    }

    @Test
    fun performanceSp() {
        performanceTest {
            pairPref = Pair("1", "2")
            pairPref = Pair("3", "4")
        }
    }

    @Test
    fun performanceMmkv() {
        MMKV.initialize(context)
        performanceTest {
            mmkvPref = Pair("1", "2")
            mmkvPref = Pair("3", "4")
        }
    }

    fun performanceTest(test: () -> Unit) {
        val start = System.currentTimeMillis()
        for (i in 0..100000) {
            test()
        }
        val end = System.currentTimeMillis()
        Log.d("performanceTest", "time: ${end - start}")
    }
}