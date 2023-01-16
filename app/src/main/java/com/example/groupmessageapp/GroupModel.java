package com.example.groupmessageapp;

import java.util.List;

public class GroupModel {
    String groupname, groupexplanation, groupimage, groupid;
    List<String> numbers;



    public GroupModel(String name, String explanation, String imageurl, List<String> uyeNumaralari, String id) {
        this.groupname = name;
        this.groupexplanation = explanation;
        this.groupimage = imageurl;
        this.groupid = id;
        this.numbers = uyeNumaralari;
    }


    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getGroupexplanation() {
        return groupexplanation;
    }

    public void setGroupexplanation(String groupexplanation) {
        this.groupexplanation = groupexplanation;
    }

    public String getGroupImage() {
        return groupimage;
    }

    public void setGroupImage(String groupimage) {
        this.groupimage = groupimage;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public List<String> getNumbers() {
        return numbers;
    }
    public void setNumbers(List<String> numbers) {
        this.numbers = numbers;
    }


}
