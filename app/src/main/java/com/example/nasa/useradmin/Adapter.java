package com.example.nasa.useradmin;

/**
 * Created by hp on 06-02-2018.
 */

public class Adapter {
    public Adapter() {
    }

    public String getText() {
        return text;
    }

    public Adapter(String text, String url) {
        this.text = text;
        this.url = url;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String text;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String url ;
}
