package com.gairola.GairolaSearchEngine.entity;


public class SearchResult {

    private final WebPage page;
    private final int score;

    public SearchResult(WebPage page, int score) {
        this.page = page;
        this.score = score;
    }

    public WebPage getPage() {
        return page;
    }

    public int getScore() {
        return score;
    }
}