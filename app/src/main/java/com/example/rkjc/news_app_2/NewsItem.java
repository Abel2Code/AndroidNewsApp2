package com.example.rkjc.news_app_2;

import android.arch.persistence.room.ColumnInfo;

import android.arch.persistence.room.Entity;

import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import android.support.annotation.NonNull;

@Entity(tableName = "news_item")
public class NewsItem {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    @NonNull
    private String author;

    @NonNull
    private String title;

    @NonNull
    private String description;

    @NonNull
    private String url;

    @NonNull
    private String urlToImage;

    @NonNull
    private String publishedAt;

    public NewsItem(int id, String author, String title, String description, String url, String urlToImage, String publishedAt){
        setId(id);
        this.author = author;
        setTitle(title);
        setDescription(description);
        setUrl(url);
        this.urlToImage = urlToImage;
        setPublishedAt(publishedAt);
    }

    @Ignore
    public NewsItem(String author, String title, String description, String url, String urlToImage, String publishedAt){
        this.author = author;
        setTitle(title);
        setDescription(description);
        setUrl(url);
        this.urlToImage = urlToImage;
        setPublishedAt(publishedAt);
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }
}
