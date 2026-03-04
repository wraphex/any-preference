package wraphex.anypreference

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SpTest : AnyPreferenceTest() {
    // null with type when no actual default value
    var pairPref by preference(
        context,
        null as Pair<String, String>?
    )

    // null with custom type
    var userPref by preference(
        context,
        null as User?
    )

    // actual default value
    var userDefaultPref by preference(
        context,
        User(1, "username", 1)
    )

    @Test
    fun testSharedPreferences() {
        val pair = Pair("first", "second")
        // set: Save it as if assigning a value to a variable
        pairPref = pair
        // get: Read it just like retrieving a variable value
        Log.d("testSharedPreferences", "pairPref: $pair $pairPref")
        assertEquals(pair, pairPref)
        // remove: Remove the value
        pairPref = null
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
}