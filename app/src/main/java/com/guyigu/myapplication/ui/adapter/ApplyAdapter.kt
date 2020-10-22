package com.guyigu.myapplication.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.guyigu.myapplication.R
import com.guyigu.myapplication.bean.ApplyFriendData
import kotlinx.android.synthetic.main.adapter_apply.view.*

/**
 * Created by tang on 2020/10/20
 */
class ApplyAdapter:BaseQuickAdapter<ApplyFriendData,BaseViewHolder>(R.layout.adapter_apply) {
    override fun convert(holder: BaseViewHolder, item: ApplyFriendData) {
        addChildClickViewIds(R.id.item_agree)
        holder.itemView.apply {
            item_name.text = item.remark
        }
    }
}