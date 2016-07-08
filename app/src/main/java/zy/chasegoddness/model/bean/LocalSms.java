package zy.chasegoddness.model.bean;

/**
 * 本地短信
 */
public class LocalSms {
    /**
     * 短信类型：接收的短信
     */
    public final static int RECIEVE_SMS = 1;
    /**
     * 短信类型：发送的短信
     */
    public final static int SEND_SMS = 2;

    /**
     * 短信内容
     */
    private String body;
    /**
     * 发送短信的电话号码
     */
    private String phoneNumber;
    /**
     * 发送短信的日期和时间
     */
    private String date;
    /**
     * 发送短信人的姓名
     */
    private String name;
    /**
     * 短信类型1是接收到的，2是已发出
     */
    private int type;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
