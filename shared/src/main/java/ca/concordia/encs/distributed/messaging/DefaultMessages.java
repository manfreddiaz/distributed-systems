package ca.concordia.encs.distributed.messaging;

public class DefaultMessages {
    public static final Message OK = new Message("OK", MessageStatus.Ok.code(), true);
    public static final Message BAD_REQUEST = new Message("BADREQUEST", MessageStatus.BadRequest.code(), false);
    public static final Message UNAUTHORIZED = new Message("UNAUTHORIZED", MessageStatus.Unauthorized.code(), false);
    public static final Message FORBIDDEN = new Message("FORBIDDEN", MessageStatus.Forbidden.code(), false);
    public static final Message NOTFOUND = new Message("FORBIDDEN", MessageStatus.NotFound.code(), false);
    public static final Message SERVER_ERROR = new Message("SERVERERROR", MessageStatus.ServerError.code(), false);
    public static final Message TIMEOUT = new Message("TIMEOUT", MessageStatus.ServerError.code(), false);
}
