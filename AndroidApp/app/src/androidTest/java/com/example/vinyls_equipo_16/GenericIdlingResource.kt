import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.test.espresso.IdlingResource

class GenericIdlingResource(private val dataLoaded: LiveData<Boolean>) :
    IdlingResource, Observer<Boolean> {

    @Volatile
    private var callback: IdlingResource.ResourceCallback? = null

    init {
        dataLoaded.observeForever(this)
    }

    override fun getName(): String = GenericIdlingResource::class.java.name

    override fun isIdleNow(): Boolean {
        val idleNow = dataLoaded.value == true
        if (idleNow) {
            callback?.onTransitionToIdle()
        }
        return idleNow
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.callback = callback
    }

    override fun onChanged(value: Boolean) {
        if (value) {
            callback?.onTransitionToIdle()
        }
    }

    fun cleanup() {
        Handler(Looper.getMainLooper()).post {
            dataLoaded.removeObserver(this)
        }
    }
}
