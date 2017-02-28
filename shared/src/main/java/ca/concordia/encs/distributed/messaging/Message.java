package ca.concordia.encs.distributed.messaging;

import java.io.Serializable;

public class Message implements Serializable {
    public String Method;
    public Object Content;
    public Integer Status;

    public Message() {}
    public Message(Message message) {
        this.Method = message.Method;
        this.Status = message.Status;
    }
    public Message(String method, Integer status, Object content) {
        this.Method = method;
        this.Status = status;
        this.Content = content;
    }

    public void setContent(Object content) {
        this.Content = content;
    }
    public Message fromContent(Object content) {
        Message clone = new Message(this);
        clone.setContent(content);
        return clone;
    }
}