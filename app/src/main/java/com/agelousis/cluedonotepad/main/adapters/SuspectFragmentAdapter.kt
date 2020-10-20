package com.agelousis.cluedonotepad.main.adapters

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.agelousis.cluedonotepad.R
import com.agelousis.cluedonotepad.cardViewer.CardViewerFragment
import com.agelousis.cluedonotepad.cards.CardsFragment
import com.agelousis.cluedonotepad.main.fragments.InfoFragment
import com.agelousis.cluedonotepad.main.fragments.SuspectWhatFragment
import com.agelousis.cluedonotepad.main.fragments.SuspectWhereFragment
import com.agelousis.cluedonotepad.main.fragments.SuspectWhoFragment

class SuspectFragmentAdapter(private val context: Context, private val hasSharingAccess: Boolean, supportFragmentManager: FragmentManager): FragmentStatePagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount() = if (hasSharingAccess) 6 else 4

    override fun getItem(position: Int) =
        when(position) {
            0 -> InfoFragment()
            1 -> SuspectWhoFragment()
            2 -> SuspectWhatFragment()
            3 -> SuspectWhereFragment()
            4 -> CardViewerFragment()
            5 -> CardsFragment()
            else -> SuspectWhoFragment()
        }

    override fun getPageTitle(position: Int) =
        when(position) {
            1 -> context.resources.getString(R.string.key_who_label)
            2 -> context.resources.getString(R.string.key_what_label)
            3 -> context.resources.getString(R.string.key_where_label)
            0, 4, 5 -> null
            else -> context.resources.getString(R.string.key_who_label)
        }
}