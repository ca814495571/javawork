package com.cqfc.queue.model;

/**
 * @author: giantspider@126.com
 */

public class Message {

    private final int id;
    private final String body;

    public Message(int id, String body) {
        this.id = id;
        this.body = body;
    }

    public int getId() {
        return id;
    }

    public String getBody() {
        return body;
    }

}
