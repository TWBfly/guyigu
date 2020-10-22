package com.guyigu.myapplication.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tencent.mmkv.MMKV

/**
 * Created by tang on 2020/10/18
 */
abstract class BaseFragment : Fragment() {
    var kv: MMKV? = null
    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(getLayoutId(), container, false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        kv = MMKV.defaultMMKV()
        initView()
    }

    abstract fun getLayoutId(): Int

    abstract fun initView()
}