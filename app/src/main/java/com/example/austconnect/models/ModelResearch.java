package com.example.austconnect.models;

public class ModelResearch {

    String pId,uid,email,uDP,name,rCategory,rDescription,rSkill,pImage,rExp,pTime;

    public ModelResearch() {
    }

    public ModelResearch(String pId, String uid, String email, String uDP, String name, String rCategory, String rDescription, String rSkill, String pImage, String rExp, String pTime) {
        this.pId = pId;
        this.uid = uid;
        this.email = email;
        this.uDP = uDP;
        this.name = name;
        this.rCategory = rCategory;
        this.rDescription = rDescription;
        this.rSkill = rSkill;
        this.pImage = pImage;
        this.rExp = rExp;
        this.pTime = pTime;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
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

    public String getrCategory() {
        return rCategory;
    }

    public void setrCategory(String rCategory) {
        this.rCategory = rCategory;
    }

    public String getrDescription() {
        return rDescription;
    }

    public void setrDescription(String rDescription) {
        this.rDescription = rDescription;
    }

    public String getrSkill() {
        return rSkill;
    }

    public void setrSkill(String rSkill) {
        this.rSkill = rSkill;
    }

    public String getpImage() {
        return pImage;
    }

    public void setpImage(String pImage) {
        this.pImage = pImage;
    }

    public String getrExp() {
        return rExp;
    }

    public void setrExp(String rExp) {
        this.rExp = rExp;
    }

    public String getpTime() {
        return pTime;
    }

    public void setpTime(String pTime) {
        this.pTime = pTime;
    }
}
