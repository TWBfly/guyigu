package com.guyigu.myapplication.ui.adapter

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
    override fun convert(holder: BaseViewHolder, item: Data) {
        holder.itemView.apply {
            Glide.with(context).load(item.img).into(item_img)
            item_name.text = item.name
        }
    }
}