package zy.chasegoddness.model.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

/**
 * Created by Administrator on 2016/8/9.
 */
public class EveryDaySMS extends BmobObject {
    /**
     * 每日暖话内容
     */
    private String content;
    /**
     * 好感度
     */
    private Integer favourability;
    /**
     * 编号
     */
    private Integer id;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getFavourability() {
        return favourability;
    }

    public void setFavourability(Integer favourability) {
        this.favourability = favourability;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
