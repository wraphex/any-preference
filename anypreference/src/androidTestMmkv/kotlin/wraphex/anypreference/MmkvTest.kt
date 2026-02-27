package wraphex.anypreference

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tencent.mmkv.MMKV
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Usage same as SP, without context
 */
@RunWith(AndroidJUnit4::class)
class MmkvTest : AnyPreferenceTest() {
    init {
        MMKV.initialize(context)
    }

    var mmkvPref by preference(null as Pair<String, String>?)

    @Test
    fun testMmkv() {
        val pair = Pair("first", "second")
        mmkvPref = pair
        Log.d("testMmkv", "mmkvPref: $pair $mmkvPref")
        assertEquals(pair, mmkvPref)
    }

    @Test
    fun performanceMmkv() {
        performanceTest {
            mmkvPref = Pair("1", "2")
            mmkvPref = Pair("3", "4")
        }
    }
}