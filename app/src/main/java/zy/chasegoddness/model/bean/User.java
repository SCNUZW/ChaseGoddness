package zy.chasegoddness.model.bean;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2016/7/8.
 */
public class User extends BmobUser {
    /**
     * 个性签名
     */
    private String desc;
    /**
     * 用户名
     */
    private String nickName;
    /**
     * 头像
     */
    private BmobFile avatar;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public BmobFile getAvatar() {
        return avatar;
    }

    public void setAvatar(BmobFile avatar) {
        this.avatar = avatar;
    }
}
