package com.example.lenovo.guardianapp;

public class NewsClass {
    private String Title;
    private String SectionName;
    private String NUri;
    private String Author;
    private String Date;

    public NewsClass(String title, String sectionName, String uri, String author, String date) {
        Title = title;
        SectionName = sectionName;
        NUri = uri;
        Author = author;
        Date = date;
    }

    public String getTitle() {
        return Title;
    }

    public String getSectionName() {
        return SectionName;
    }

    public String getNUri() {
        return NUri;
    }

    public String getAuthor() {
        return Author;
    }

    public String getDate() {
        return Date;
    }
}