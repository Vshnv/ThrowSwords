package com.rocketmail.vaishnavanil.throwswordsredone.messages;

public enum Message {
    THROW("**Your item was thrown**"),
    TIMEOUT("**Your item returned to you**"),
    HIT_BLOCK("**Your item hit a block**"),
    HIT_ENTITY("**Your item hit something**"),
    ITEM_BROKE("**Your item broke**");
    String message;
    Message(String m){
        message = m;
    }

    public String getMessage(){
        return message;
    }
    public void setMessage(String s){
        message = s;
    }
}
