
import android.os.Build
import me.fsfaysalcse.fanasonic.Platform

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}
