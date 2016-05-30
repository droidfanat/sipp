package org.silena.main.model;

import android.net.Uri;

/**
 * Created by дима on 19.05.2016.
 */
public class Contact {

    private String name;
    private String phone;
    private Uri photoUri;

    public Uri getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(Uri photoUri) {
        this.photoUri = photoUri;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Contact(String name, String phone){
        this.name = name;
        this.phone = phone;
    }

    public Contact(){
    }

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", photoUri=" + photoUri +
                '}';
    }
}
