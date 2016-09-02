package zy.chasegoddness.model.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/9/2.
 */
public class FeedBack extends BmobObject {
    /**
     * 反馈内容
     */
    private String content;
    /**
     * 反馈者的QQ
     */
    private String QQ;
    /**
     * 反馈者的联系电话
     */
    private String phone;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getQQ() {
        return QQ;
    }

    public void setQQ(String QQ) {
        this.QQ = QQ;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
