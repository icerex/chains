package com.teamlinking.chains.vo;

import java.util.List;

/**
 * Created by admin on 16/1/20.
 */
public class StoryVO {

    private String date;

    private Long id;

    private String title;

    private String pic;

    private int order;

    private String url;

    private List<StoryVO> subs;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic() {
        return pic;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public List<StoryVO> getSubs() {
        return subs;
    }

    public void setSubs(List<StoryVO> subs) {
        this.subs = subs;
    }
}
