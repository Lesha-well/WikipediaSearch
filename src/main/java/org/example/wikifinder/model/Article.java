package org.example.wikifinder.model;

public class Article {

    private final long pageId;
    private final String title;
    private final String snippet;

    public Article(long pageId, String title, String snippet) {
        this.pageId = pageId;
        this.title = title;
        this.snippet = snippet == null ? "" : snippet;
    }

    public long getPageId() {
        return pageId;
    }

    public String getTitle() {
        return title;
    }

    public String getSnippet() {
        return snippet;
    }
}