package com.example.austconnect.models;

public class ModelPost {

    String pLike,pId,pTitle,pTime,Description,pImage,uid,email,uDP,name;


    public ModelPost() {

    }

    public ModelPost(String pLike,String pId, String pTitle, String pTime, String description, String pImage, String uid, String email, String uDP, String name) {
        this.pId = pId;
        this.pTitle = pTitle;
        this.pTime = pTime;
        Description = description;
        this.pImage = pImage;
        this.uid = uid;
        this.email = email;
        this.uDP = uDP;
        this.name = name;
        this.pLike = pLike;
    }

    public String getpLike() {
        return pLike;
    }

    public void setpLike(String pLike) {
        this.pLike = pLike;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getpTitle() {
        return pTitle;
    }

    public void setpTitle(String pTitle) {
        this.pTitle = pTitle;
    }

    public String getpTime() {
        return pTime;
    }

    public void setpTime(String pTime) {
        this.pTime = pTime;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getpImage() {
        return pImage;
    }

    public void setpImage(String pImage) {
        this.pImage = pImage;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getuDP() {
        return uDP;
    }

    public void setuDP(String uDP) {
        this.uDP = uDP;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

