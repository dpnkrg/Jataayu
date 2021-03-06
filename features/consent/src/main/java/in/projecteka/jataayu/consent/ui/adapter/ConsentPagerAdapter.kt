package `in`.projecteka.jataayu.consent.ui.adapter

import `in`.projecteka.jataayu.consent.ui.fragment.ConsentListFragment
import `in`.projecteka.jataayu.consent.ui.fragment.RequestedListFragment
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


private val TAB_TITLES = arrayOf(
    `in`.projecteka.jataayu.consent.R.string.tab_requests,
    `in`.projecteka.jataayu.consent.R.string.tab_consents
)

class ConsentPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val REQUESTS_INDEX = 0
    private val TABS_COUNT = 2

    override fun getItem(position: Int): Fragment {
        return if (position == REQUESTS_INDEX) {
            RequestedListFragment.newInstance()
        } else {
            ConsentListFragment.newInstance()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return TABS_COUNT
    }
}