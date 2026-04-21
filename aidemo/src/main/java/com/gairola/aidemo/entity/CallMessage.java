package com.gairola.aidemo.entity;

import lombok.Data;

@Data
public class CallMessage {
    private String type; // offer / answer / candidate
    private String sender;
    private String receiver;
    private String data; // SDP or ICE
}