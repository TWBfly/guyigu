package com.guyigu.myapplication.ui.vm;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.app.DialogCompat;

import com.blankj.utilcode.util.LogUtils;
import com.guyigu.myapplication.R;

import io.rong.imkit.RongIM;
import io.rong.imkit.emoticon.AndroidEmoji;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.AsyncImageView;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;

/**
 * Created by tang on 2020/10/21
 */
@ProviderTag(messageContent = RedMessage.class, showProgress = false, showReadState = true)
public class RedMessageItemProvider extends IContainerItemProvider.MessageProvider<RedMessage> {

    private static class ViewHolder {
        LinearLayout mLayout;
    }

    @Override
    public View newView(Context context, ViewGroup group) {
        View view = LayoutInflater.from(context).inflate(R.layout.message_red, null);
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.mLayout = view.findViewById(R.id.rc_layout);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View v, int position, RedMessage content, UIMessage message) {
        final RedMessageItemProvider.ViewHolder viewHolder = (RedMessageItemProvider.ViewHolder) v.getTag();

        if (!TextUtils.isEmpty(content.getName())) {
            SpannableStringBuilder spannable = new SpannableStringBuilder(content.getName());
            AndroidEmoji.ensure(spannable);
        }

//        IContactCardInfoProvider iContactCardInfoProvider = ContactCardContext.getInstance().getContactCardInfoProvider();
//        if (iContactCardInfoProvider != null) {
//            iContactCardInfoProvider.getContactAppointedInfoProvider(content.getId(), content.getName(), content.getImgUrl(), list -> {
//                if (list != null && list.size() > 0) {
//                    UserInfo userInfo = list.get(0);
//                    if (userInfo != null && userInfo.getPortraitUri() != null) {
//                                /* 如果名片发送的推荐人头像信息，与本地数据库的对应头像信息不一致，
//                                   则优先显示本地数据库的对应头像信息 */
//                        if (TextUtils.isEmpty(content.getImgUrl()) ||
//                                !content.getImgUrl().equals(userInfo.getPortraitUri().toString())) {
//                            viewHolder.mImage.setAvatar(userInfo.getPortraitUri());
//                            ((ContactMessage) (message.getContent()))
//                                    .setImgUrl(userInfo.getPortraitUri().toString());
//                        }
//                        // 如果本端设置了该用户信息的别名(备注、昵称)，优先显示这个别名
//                        if (!TextUtils.isEmpty(content.getName()) && !content.getName().equals(userInfo.getName())) {
//                            viewHolder.mName.setText(userInfo.getName());
//                        }
//                    }
//                }
//            });
//        }

        if (message.getMessageDirection() == Message.MessageDirection.RECEIVE)
            viewHolder.mLayout.setBackgroundResource(io.rong.contactcard.R.drawable.rc_ic_bubble_left_file);
        else
            viewHolder.mLayout.setBackgroundResource(io.rong.contactcard.R.drawable.rc_ic_bubble_right_file);
    }

    @Override
    public Spannable getContentSummary(RedMessage redMessage) {
        return null;
    }

    @Override
    public Spannable getContentSummary(Context context, final RedMessage contactMessage) {
        if (contactMessage != null && !TextUtils.isEmpty(contactMessage.getSendUserId()) && !TextUtils.isEmpty(contactMessage.getSendUserName())) {
            if (contactMessage.getSendUserId().equals(RongIM.getInstance().getCurrentUserId())) {
                String str_RecommendClause = context.getResources().getString(io.rong.contactcard.R.string.rc_recommend_clause_to_others);
                return new SpannableString(String.format(str_RecommendClause, contactMessage.getName()));
            } else {
                String str_RecommendClause = context.getResources().getString(io.rong.contactcard.R.string.rc_recommend_clause_to_me);
                return new SpannableString(String.format(str_RecommendClause, contactMessage.getSendUserName(), contactMessage.getName()));
            }
        }
        return new SpannableString("[" + context.getResources().getString(io.rong.contactcard.R.string.rc_plugins_contact) + "]");
    }


    @Override
    public void onItemClick(View view, int i, RedMessage content, UIMessage uiMessage) {
        LogUtils.e("redMessage=onItemClick=");
    }
}
