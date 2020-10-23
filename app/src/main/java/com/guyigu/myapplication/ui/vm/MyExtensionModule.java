package com.guyigu.myapplication.ui.vm;

import java.util.List;
import java.util.ListIterator;

import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.RongIM;
import io.rong.imkit.plugin.DefaultLocationPlugin;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imlib.model.Conversation;

/**
 * Created by tang on 2020/10/19
 */
public class MyExtensionModule extends DefaultExtensionModule {
    @Override
    public void onInit(String appKey) {
        super.onInit(appKey);
        //自定义消息
        RongIM.registerMessageType(RedMessage.class);
        RongIM.registerMessageTemplate(new RedMessageItemProvider());
    }

    @Override
    public List<IPluginModule> getPluginModules(Conversation.ConversationType conversationType) {
        List<IPluginModule> pluginModules = super.getPluginModules(conversationType);
        ListIterator<IPluginModule> iterator = pluginModules.listIterator();
        // 删除扩展项
//        while (iterator.hasNext()) {
//            IPluginModule integer = iterator.next();
//            // 以删除 FilePlugin 为例
//            if (integer instanceof FilePlugin) {
//                iterator.remove();
//            }
//        }


        // 增加扩展项, 以 ImagePlugin 为例
//        pluginModules.add(new ImagePlugin());
        pluginModules.add(new DefaultLocationPlugin());
        pluginModules.add(new MyPlugin());
//        pluginModules.add(new SightPlugin());
//        pluginModules.add(new AudioPlugin());
//        pluginModules.add(new VideoPlugin());
        return pluginModules;
    }
}
