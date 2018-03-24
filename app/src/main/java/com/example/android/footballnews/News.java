package com.example.android.footballnews;

/**
 * Created by bodiy_000 on 02-Mar-18.
 */

public class News {
    private String articleTitle, sectionName, articleDate, webUrl, authorName;

    public News(String articeName, String sectionName, String articleDate, String webUrl, String authorName) {
        this.articleTitle = articeName;
        this.sectionName = sectionName;
        this.articleDate = articleDate;
        this.webUrl = webUrl;
        this.authorName = authorName;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getArticleDate() {
        return articleDate;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getWebUrl() {
        return webUrl;
    }
}
