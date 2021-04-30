package com.agelousis.cluedonotepad.main.adapters

import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.agelousis.cluedonotepad.R
import com.agelousis.cluedonotepad.cardViewer.CardViewerFragment
import com.agelousis.cluedonotepad.cards.CardsFragment
import com.agelousis.cluedonotepad.main.fragments.InfoFragment
import com.agelousis.cluedonotepad.main.fragments.SuspectWhatFragment
import com.agelousis.cluedonotepad.main.fragments.SuspectWhereFragment
import com.agelousis.cluedonotepad.main.fragments.SuspectWhoFragment

class SuspectFragmentAdapter(private val fragmentActivity: FragmentActivity, private val hasSharingAccess: Boolean): FragmentStateAdapter(fragmentActivity) {

    fun getPageTitle(position: Int) =
        when(position) {
            1 -> fragmentActivity.resources.getString(R.string.key_who_label)
            2 -> fragmentActivity.resources.getString(R.string.key_what_label)
            3 -> fragmentActivity.resources.getString(R.string.key_where_label)
            0, 4, 5 -> null
            else -> fragmentActivity.resources.getString(R.string.key_who_label)
        }

    fun getPageIcon(position: Int) =
        when(position) {
            0 -> R.drawable.ic_info
            4 -> R.drawable.ic_send
            5 -> R.drawable.ic_image
            else -> null
        }

    override fun getItemCount() = if (hasSharingAccess) 6 else 4

    override fun createFragment(position: Int) = when(position) {
        0 -> InfoFragment()
        1 -> SuspectWhoFragment()
        2 -> SuspectWhatFragment()
        3 -> SuspectWhereFragment()
        4 -> CardViewerFragment()
        5 -> CardsFragment()
        else -> SuspectWhoFragment()
    }
}