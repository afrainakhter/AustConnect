package com.example.austconnect.models;

public class ModelBlood {

    String uid,email,name,uDP,pId,ContactName,Description,bGroup,pImage,pTime;

    public ModelBlood() {


    }

    public ModelBlood(String uid, String email, String name, String uDP, String pId, String contactName, String description, String bGroup, String pImage, String pTime) {
        this.uid = uid;
        this.email = email;
        this.name = name;
        this.uDP = uDP;
        this.pId = pId;
        ContactName = contactName;
        Description = description;
        this.bGroup = bGroup;
        this.pImage = pImage;
        this.pTime = pTime;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getuDP() {
        return uDP;
    }

    public void setuDP(String uDP) {
        this.uDP = uDP;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getContactName() {
        return ContactName;
    }

    public void setContactName(String contactName) {
        ContactName = contactName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getbGroup() {
        return bGroup;
    }

    public void setbGroup(String bGroup) {
        this.bGroup = bGroup;
    }

    public String getpImage() {
        return pImage;
    }

    public void setpImage(String pImage) {
        this.pImage = pImage;
    }

    public String getpTime() {
        return pTime;
    }

    public void setpTime(String pTime) {
        this.pTime = pTime;
    }
}
