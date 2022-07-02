package com.sendmail;

public class Template {
    private String from;
    private String subject;
    private String mineType;
    private String body;

    public Template(String from, String subject, String mineType, String body) {
        this.from = from;
        this.subject = subject;
        this.mineType = mineType;
        this.body = body;
    }

    public Template() {
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMineType() {
        return mineType;
    }

    public void setMineType(String mineType) {
        this.mineType = mineType;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}

