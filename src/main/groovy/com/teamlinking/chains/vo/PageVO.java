package com.teamlinking.chains.vo;

import java.util.List;

/**
 * Created by admin on 15/12/1.
 */
public class PageVO<T> {
    private List<T> result;

    private long count;

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
