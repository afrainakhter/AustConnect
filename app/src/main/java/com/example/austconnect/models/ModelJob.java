package com.example.austconnect.models;

public class ModelJob {
    String pId,uid,email,uDP,name,jCategory,jDescription,jSkill,jType,pImage,jPreference,jSalary,jExp,pTime;


    public ModelJob() {

    }

    public ModelJob(String pId, String uid, String email, String uDP, String name, String jCategory, String jDescription, String jSkill, String jType, String pImage, String jPreference, String jSalary, String jExp, String pTime) {
        this.pId = pId;
        this.uid = uid;
        this.email = email;
        this.uDP = uDP;
        this.name = name;
        this.jCategory = jCategory;
        this.jDescription = jDescription;
        this.jSkill = jSkill;
        this.jType = jType;
        this.pImage = pImage;
        this.jPreference = jPreference;
        this.jSalary = jSalary;
        this.jExp = jExp;
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

    public String getjCategory() {
        return jCategory;
    }

    public void setjCategory(String jCategory) {
        this.jCategory = jCategory;
    }

    public String getjDescription() {
        return jDescription;
    }

    public void setjDescription(String jDescription) {
        this.jDescription = jDescription;
    }

    public String getjSkill() {
        return jSkill;
    }

    public void setjSkill(String jSkill) {
        this.jSkill = jSkill;
    }

    public String getjType() {
        return jType;
    }

    public void setjType(String jType) {
        this.jType = jType;
    }

    public String getpImage() {
        return pImage;
    }

    public void setpImage(String pImage) {
        this.pImage = pImage;
    }

    public String getjPreference() {
        return jPreference;
    }

    public void setjPreference(String jPreference) {
        this.jPreference = jPreference;
    }

    public String getjSalary() {
        return jSalary;
    }

    public void setjSalary(String jSalary) {
        this.jSalary = jSalary;
    }

    public String getjExp() {
        return jExp;
    }

    public void setjExp(String jExp) {
        this.jExp = jExp;
    }

    public String getpTime() {
        return pTime;
    }

    public void setpTime(String pTime) {
        this.pTime = pTime;
    }
}

