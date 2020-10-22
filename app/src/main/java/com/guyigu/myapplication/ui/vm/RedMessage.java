package com.guyigu.myapplication.ui.vm;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.common.ParcelUtils;
import io.rong.imlib.DestructionTag;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;

/**
 * Created by tang on 2020/10/21
 */
@MessageTag(value = "RC:redMsg", flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED)
@DestructionTag
public class RedMessage extends MessageContent implements Parcelable {

    private String id;
    private String name;
    private String imgUrl;
    private String sendUserId;
    private String sendUserName;
    private String extra;

    public RedMessage() {
    }

    public RedMessage(String id, String name, String imgUrl, String sendUserId, String sendUserName, String extra) {
        this.id = id;
        this.name = name;
        this.imgUrl = imgUrl;
        this.sendUserId = sendUserId;
        this.sendUserName = sendUserName;
        this.extra = extra;
    }

    protected RedMessage(Parcel in) {
        id = in.readString();
        name = in.readString();
        imgUrl = in.readString();
        sendUserId = in.readString();
        sendUserName = in.readString();
        extra = in.readString();
        setUserInfo(ParcelUtils.readFromParcel(in, UserInfo.class));
        setDestruct(ParcelUtils.readIntFromParcel(in) == 1);
        setDestructTime(ParcelUtils.readLongFromParcel(in));
    }

    public static final Creator<RedMessage> CREATOR = new Creator<RedMessage>() {
        @Override
        public RedMessage createFromParcel(Parcel in) {
            return new RedMessage(in);
        }

        @Override
        public RedMessage[] newArray(int size) {
            return new RedMessage[size];
        }
    };

    // 快速构建消息对象方法
    public static RedMessage obtain(String id, String title, String imgUrl, String senduserId, String sendUserName, String extra) {
        return new RedMessage(id, title, imgUrl, senduserId, sendUserName, extra);
    }

    /**
     * 创建 CustomMessage(byte[] data) 带有 byte[] 的构造方法用于解析消息内容.
     */
    public RedMessage(byte[] data) {
        if (data == null) {
            return;
        }

        String jsonStr = null;
        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }

        if (jsonStr == null) {
            return;
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            // 消息携带用户信息时, 自定义消息需添加下面代码
            if (jsonObj.has("user")) {
                setUserInfo(parseJsonToUserInfo(jsonObj.getJSONObject("user")));
            }

            // 用于群组聊天, 消息携带 @ 人信息时, 自定义消息需添加下面代码
//            if (jsonObj.has("mentionedInfo")) {
//                setMentionedInfo(parseJsonToMentionInfo(jsonObj.getJSONObject("mentionedInfo")));
//            }

            // ...
            // 自定义消息, 定义的字段
            // ...

        } catch (JSONException e) {
        }

    }

    /**
     * 将本地消息对象序列化为消息数据。
     *
     * @return 消息数据。
     */
    @Override
    public byte[] encode() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", getId()); // 这里的id（联系人）不同于下边发送名片信息者的 sendUserId
            jsonObject.put("name", "=name=");
            jsonObject.put("portraitUri", getImgUrl());
            jsonObject.put("sendUserId", getSendUserId());
            jsonObject.put("sendUserName", "=sendUserName=");
            jsonObject.put("extra", getExtra());
            if (getJSONUserInfo() != null) {
                jsonObject.putOpt("user", getJSONUserInfo());
            }
            jsonObject.put("isBurnAfterRead", isDestruct());
            jsonObject.put("burnDuration", getDestructTime());


        } catch (JSONException e) {
        }

        try {
            return jsonObject.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(imgUrl);
        dest.writeString(sendUserId);
        dest.writeString(sendUserName);
        dest.writeString(extra);
        ParcelUtils.writeToParcel(dest, getUserInfo());
        ParcelUtils.writeToParcel(dest, isDestruct() ? 1 : 0);
        ParcelUtils.writeToParcel(dest, getDestructTime());
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    public String getSendUserName() {
        return sendUserName;
    }

    public void setSendUserName(String sendUserName) {
        this.sendUserName = sendUserName;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
