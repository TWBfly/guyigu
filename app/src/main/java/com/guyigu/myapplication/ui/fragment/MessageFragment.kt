package com.guyigu.myapplication.ui.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.blankj.utilcode.util.LogUtils
import com.guyigu.myapplication.R
import com.guyigu.myapplication.base.BaseFragment
import com.guyigu.myapplication.bean.ItemClickDataBean
import com.guyigu.myapplication.ui.activity.NewFriendActivity
import com.guyigu.myapplication.util.item_click_friend_id
import com.guyigu.myapplication.util.item_click_friend_img
import com.guyigu.myapplication.util.item_click_friend_name
import io.rong.imkit.RongIM
import io.rong.imkit.fragment.ConversationListFragment
import io.rong.imkit.model.UIConversation
import io.rong.imlib.IRongCallback
import io.rong.imlib.RongIMClient
import io.rong.imlib.model.Conversation
import io.rong.imlib.model.Message
import io.rong.message.TextMessage
import org.greenrobot.eventbus.EventBus


/**
 * Created by tang on 2020/10/18
 */
class MessageFragment : BaseFragment() {
    override fun getLayoutId(): Int = R.layout.fragment_message

    override fun initView() {
        val conversationListFragment = ConversationListFragment()
        // 此处设置 Uri. 通过 appendQueryParameter 去设置所要支持的会话类型. 例如
        // .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(),"false")
        // 表示支持单聊会话, false 表示不聚合显示, true 则为聚合显示
        val uri: Uri = Uri.parse("rong://" + activity?.applicationInfo?.packageName).buildUpon()
            .appendPath("conversationlist")
            .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
            .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false") //群组
            .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false") //公共服务号
            .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false") //订阅号
            .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true") //系统
            .build()

        conversationListFragment.uri = uri
        val manager: FragmentManager = activity?.supportFragmentManager!!
        val transaction: FragmentTransaction = manager.beginTransaction()
        transaction.replace(R.id.container, conversationListFragment)
        transaction.commit()

        RongIM.setConversationListBehaviorListener(object : RongIM.ConversationListBehaviorListener {
            /**
             * 会话头像点击监听
             *
             * @param context          上下文。
             * @param conversationType 会话类型。
             * @param targetId         被点击的用户id。
             * @return  true 拦截事件, false 执行融云 SDK 内部默认处理逻辑
             */
            override fun onConversationPortraitClick(context: Context?, conversationType: Conversation.ConversationType?, targetId: String?): Boolean {
                return false
            }

            /**
             * 会话头像长按监听
             *
             * @param context          上下文。
             * @param conversationType 会话类型。
             * @param targetId         被点击的用户id。
             * @return true 拦截事件, false 执行融云 SDK 内部默认处理逻辑
             */
            override fun onConversationPortraitLongClick(context: Context?, conversationType: Conversation.ConversationType?, targetId: String?): Boolean {
                return false
            }

            /**
             * 会话列表中的 Item 长按监听
             *
             * @param context      上下文。
             * @param view         触发点击的 View。
             * @param conversation 长按时的会话条目
             * @return true 拦截事件, false 执行融云 SDK 内部默认处理逻辑
             */
            override fun onConversationLongClick(context: Context?, view: View?, conversation: UIConversation?): Boolean {
                return false
            }

            /**
             * 会话列表中的 Item 点击监听
             *
             * @param context      上下文。
             * @param view         触发点击的 View。
             * @param conversation 长按时的会话条目
             * @return true 拦截事件, false 执行融云 SDK 内部默认处理逻辑
             */
            override fun onConversationClick(context: Context?, view: View?, conversation: UIConversation): Boolean {
                EventBus.getDefault().postSticky(ItemClickDataBean(userId = conversation.conversationTargetId, userName = conversation.uiConversationTitle))
                LogUtils.i("==会话列表中的 Item==conversation==" + conversation.uiConversationTitle + "==content==" + conversation.conversationContent + "==" + conversation.conversationTargetId)
                if (conversation.conversationContent.toString().contains("添加你为好友")) {
                    startActivity(Intent(activity, NewFriendActivity::class.java))
                    return true
                } else if (conversation.conversationContent.toString().contains("好友验证通过")) {
                    sendMessage(conversation.conversationTargetId)
                    RongIM.getInstance().startConversation(
                        activity,
                        Conversation.ConversationType.PRIVATE,
                        conversation.conversationTargetId,
                        conversation.uiConversationTitle,
                        bundleOf(item_click_friend_id to conversation.conversationTargetId, item_click_friend_name to conversation.uiConversationTitle, item_click_friend_img to conversation.iconUrl)
                    )
                    return true
                }

                return false
            }

        })

    }

    /**
     * 新好友添加成功 先发条消息
     */
    private fun sendMessage(targetId: String) {
        val content = "我们是好友啦，开始聊天吧！"
        val conversationType = Conversation.ConversationType.PRIVATE

        val messageContent = TextMessage.obtain(content)
        val message = Message.obtain(targetId, conversationType, messageContent);
        RongIM.getInstance().sendMessage(message, null, null, object : IRongCallback.ISendMessageCallback {
            /**
             * 消息发送前回调, 回调时消息已存储数据库
             * @param message 已存库的消息体
             */
            override fun onAttached(message: Message?) {
                LogUtils.w("onAttached==$message")
            }

            /**
             * 消息发送成功。
             * @param message 发送成功后的消息体
             */
            override fun onSuccess(message: Message?) {
                LogUtils.w("onSuccess==$message")
            }

            /**
             * 消息发送失败
             * @param message   发送失败的消息体
             * @param errorCode 具体的错误
             */
            override fun onError(message: Message?, errorCode: RongIMClient.ErrorCode?) {
                LogUtils.w("onError==$message==errorCode==${errorCode?.value}" == "${errorCode?.message}")
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        LogUtils.i("==onActivityResult==" + requestCode, resultCode, data)
    }


}