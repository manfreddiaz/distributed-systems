package ca.concordia.encs.distributed.messaging;

public enum MessageStatus {
    Ok(200),
    Request(201),
    Reply(202),
    BadRequest(400),
    Unauthorized(401),
    Forbidden(403),
    NotFound(404),
    ServerError(500),
    Timeout(504),;

    private int code;

    MessageStatus(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }
}
