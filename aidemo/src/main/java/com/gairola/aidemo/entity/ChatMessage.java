package com.gairola.aidemo.entity;

import lombok.Data;

@Data
public class ChatMessage {
    private String sender;
    private String receiver;
    private String content;
}