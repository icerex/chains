package com.teamlinking.chains.vo;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by admin on 16/1/22.
 */
public class StorySimpleVO {
    private String title;
    private Long id;
    private List<StorySimpleVO> subs = Lists.newArrayList();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<StorySimpleVO> getSubs() {
        return subs;
    }

    public void setSubs(List<StorySimpleVO> subs) {
        this.subs = subs;
    }
}
