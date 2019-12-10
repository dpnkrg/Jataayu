package `in`.org.projecteka.jataayu.provider.ui.activity

import `in`.org.projecteka.featuresprovider.R
import android.os.Bundle
import android.view.WindowManager
import androidx.annotation.RestrictTo
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

@RestrictTo(RestrictTo.Scope.TESTS)
class TestsOnlyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        setContentView(R.layout.container_layout)
    }

    public fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .add(R.id.container, fragment, fragment::class.java.simpleName)
            .addToBackStack(null)
            .commit()
    }
}