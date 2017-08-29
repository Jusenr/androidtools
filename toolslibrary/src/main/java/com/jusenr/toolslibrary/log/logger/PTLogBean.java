package com.jusenr.toolslibrary.log.logger;

/**
 * Created by xiaopeng on 2017/7/28.
 */

public class PTLogBean {
    private Long id;
    private int level;
    private String content;
    private java.util.Date date;

    public PTLogBean(Long id, int level, String content, java.util.Date date) {
        this.id = id;
        this.level = level;
        this.content = content;
        this.date = date;
    }

    public PTLogBean() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public java.util.Date getDate() {
        return this.date;
    }

    public void setDate(java.util.Date date) {
        this.date = date;
    }
}
