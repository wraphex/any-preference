package wraphex.anypreference

import android.annotation.SuppressLint
import android.content.Context
import com.tencent.mmkv.MMKV

@SuppressLint("StaticFieldLeak")
object AnyPreferences : BaseAnyPreferences() {
    override fun initialize(context: Context) {
        super.initialize(context)
        MMKV.initialize(context)
    }
}