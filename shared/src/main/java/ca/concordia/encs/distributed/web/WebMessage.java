package ca.concordia.encs.distributed.web;


public class WebMessage {
    public Integer Status;
    public String Method;
    public String Content;

    public WebMessage() {

    }

    public WebMessage(Integer status, String method, String content) {
        this.Status = status;
        this.Method = method;
        this.Content = content;
    }

}
