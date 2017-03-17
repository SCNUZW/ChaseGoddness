package zy.chasegoddness.model.bean;

import cn.bmob.v3.BmobObject;

public class Evaluation extends BmobObject{
    private String title;
    private Integer id;
    private Integer favorability;
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer  getId() {
        return id;
    }

    public void setId(Integer  id) {
        this.id = id;
    }

    public Integer  getFavorability() {
        return favorability;
    }

    public void setFavorability(Integer  favorability) {
        this.favorability = favorability;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}