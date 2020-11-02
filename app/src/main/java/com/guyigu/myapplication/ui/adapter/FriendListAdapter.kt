package com.guyigu.myapplication.ui.adapter

import android.view.View
import androidx.core.view.isGone
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.guyigu.myapplication.R
import com.guyigu.myapplication.bean.Data
import kotlinx.android.synthetic.main.adapter_friendlist.view.*

/**
 * Created by tang on 2020/10/23
 */
class FriendListAdapter : BaseQuickAdapter<Data, BaseViewHolder>(R.layout.adapter_friendlist) {
    private var i: Int = 0
    override fun convert(holder: BaseViewHolder, item: Data) {
        holder.itemView.apply {
            if (i == 0) {
                item_check.visibility = View.GONE
            } else {
                item_check.visibility = View.VISIBLE
                item_check.setOnCheckedChangeListener { _, isChecked ->
                    item.isCheck = isChecked
                }
            }
            Glide.with(context).load(item.img).into(item_img)
            item_name.text = item.name
        }
    }

    fun setCheckBoxShow(i: Int) {
        this.i = i
        notifyDataSetChanged()
    }
}