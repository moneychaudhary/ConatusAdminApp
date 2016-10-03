package com.example.money.conatusadminapp.MagazineGaller;

/**
 * Created by #money on 10/2/2016.
 */

public class ListOfView {
    private String title;
    private String image;

    public ListOfView(String title, String image) {
        this.title = title;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ListOfView()
    {

    }
}
