package com.aariyan.imo_template.Model;

public class MessageModel {

    private int id;
    private int type;
    private String receiverMessage,senderMessage;
    private String senderTime,receiverTime;

    public MessageModel(){}

    public MessageModel(int id, int type, String receiverMessage, String senderMessage, String senderTime, String receiverTime) {
        this.id = id;
        this.type = type;
        this.receiverMessage = receiverMessage;
        this.senderMessage = senderMessage;
        this.senderTime = senderTime;
        this.receiverTime = receiverTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getReceiverMessage() {
        return receiverMessage;
    }

    public void setReceiverMessage(String receiverMessage) {
        this.receiverMessage = receiverMessage;
    }

    public String getSenderMessage() {
        return senderMessage;
    }

    public void setSenderMessage(String senderMessage) {
        this.senderMessage = senderMessage;
    }

    public String getSenderTime() {
        return senderTime;
    }

    public void setSenderTime(String senderTime) {
        this.senderTime = senderTime;
    }

    public String getReceiverTime() {
        return receiverTime;
    }

    public void setReceiverTime(String receiverTime) {
        this.receiverTime = receiverTime;
    }
}
