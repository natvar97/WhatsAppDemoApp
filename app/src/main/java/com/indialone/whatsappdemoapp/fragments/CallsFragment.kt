package com.indialone.whatsappdemoapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.indialone.whatsappdemoapp.R
import com.indialone.whatsappdemoapp.databinding.FragmentCallsBinding

class CallsFragment : Fragment() {

    private lateinit var mBinding : FragmentCallsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentCallsBinding.inflate(inflater , container , false)
        return mBinding.root
    }


}