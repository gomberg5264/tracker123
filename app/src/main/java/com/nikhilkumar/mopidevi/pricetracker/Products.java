package com.nikhilkumar.mopidevi.pricetracker;

/**
 * Created by NIKHIL on 27-Mar-15.
 */
public class Products {

    private int id;
    private String title;
    private String url;
    private String init_price;
    private String cur_price;
    private String img_path;

    public Products(int id, String title, String url, String init_price, String cur_price,
                           String img_path) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.init_price = init_price;
        this.cur_price = cur_price;
        this.img_path = img_path;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id=id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getInit_price() {
        return init_price;
    }

    public void setInit_price(String init_price) {
        this.init_price = init_price;
    }

    public String getCur_price() {
        return cur_price;
    }

    public void setCur_price(String cur_price) {
        this.cur_price = cur_price;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }
}
