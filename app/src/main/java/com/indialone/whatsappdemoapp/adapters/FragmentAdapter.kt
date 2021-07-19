package com.indialone.whatsappdemoapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.indialone.whatsappdemoapp.fragments.CallsFragment
import com.indialone.whatsappdemoapp.fragments.ChatsFragment
import com.indialone.whatsappdemoapp.fragments.StatusFragment

class FragmentAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                return ChatsFragment()
            }
            1 -> {
                return StatusFragment()
            }
            2 -> {
                return CallsFragment()
            }
            else -> {
                return ChatsFragment()
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var title = ""
        if (position == 0) title = "Chats"
        else if (position == 1) title = "Status"
        else title = "Calls"

        return title
    }
}