package com.guyigu.myapplication.ui.vm;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.guyigu.myapplication.R;
import com.guyigu.myapplication.ui.activity.RedSetActivity;

import io.rong.imkit.RongExtension;
import io.rong.imkit.RongIM;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imlib.model.Group;

/**
 * Created by tang on 2020/10/19
 */
public class MyPlugin implements IPluginModule {
    @Override
    public Drawable obtainDrawable(Context context) {
        return ContextCompat.getDrawable(context, R.mipmap.red_packet);
    }

    @Override
    public String obtainTitle(Context context) {
        return "红包";
    }

    @Override
    public void onClick(Fragment fragment, RongExtension rongExtension) {
        fragment.getActivity().startActivity(new Intent(fragment.getActivity(), RedSetActivity.class));
    }


    @Override
    public void onActivityResult(int i, int i1, Intent intent) {
        RongIM.setGroupInfoProvider(new RongIM.GroupInfoProvider() {
            @Override
            public Group getGroupInfo(String groupId) {
                Group groupinfo = new Group(groupId, "groupId 对应的名称", Uri.parse("groupId 对应的头像地址"));
                return groupinfo;
            }
        }, true);
    }
}
