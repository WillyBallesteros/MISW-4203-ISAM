import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.test.espresso.IdlingResource

class AlbumIdlingResource(private val dataLoaded: LiveData<Boolean>) :
    IdlingResource, Observer<Boolean> {

    @Volatile
    private var callback: IdlingResource.ResourceCallback? = null

    init {
        dataLoaded.observeForever(this)
    }

    override fun getName(): String = AlbumIdlingResource::class.java.name

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

    override fun onChanged(isDataLoaded: Boolean) { // Asegúrate de que no hay un signo de interrogación aquí
        if (isDataLoaded) { // Usamos el valor directamente ya que sabemos que no es nulo
            callback?.onTransitionToIdle()
        }
    }

    fun cleanup() {
        Handler(Looper.getMainLooper()).post {
            dataLoaded.removeObserver(this)
        }
    }
}
