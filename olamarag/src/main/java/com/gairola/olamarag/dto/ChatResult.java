package com.gairola.olamarag.dto;


public class ChatResult {

    private final String response;
    private final long responseTimeMs;

    public ChatResult(String response, long responseTimeMs) {
        this.response = response;
        this.responseTimeMs = responseTimeMs;
    }

    public String getResponse() {        return response;
    }

    public long getResponseTimeMs() {
        return responseTimeMs;
    }
}