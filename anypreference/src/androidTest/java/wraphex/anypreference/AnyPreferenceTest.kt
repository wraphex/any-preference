package wraphex.anypreference

import android.content.Context
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry

open class AnyPreferenceTest {
    var context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    fun performanceTest(test: () -> Unit) {
        val start = System.currentTimeMillis()
        for (i in 0..1000) {
            test()
        }
        val end = System.currentTimeMillis()
        Log.d("performanceTest", "time: ${end - start}")
    }
}