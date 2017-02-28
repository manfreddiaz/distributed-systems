package ca.concordia.encs.distributed.service.messaging;

import ca.concordia.encs.distributed.messaging.Message;
import ca.concordia.encs.distributed.messaging.MessageStatus;

public class DefaultSystemMessages {
    public static final Message OK = new Message("COUNT", MessageStatus.Request.code(), 0);
    public static final Message COUNT = new Message("COUNT", MessageStatus.Request.code(), 0);
    public static final Message ALIVE = new Message("ALIVE", MessageStatus.Request.code(), 0);
    public static final Message TRANSFER = new Message("TRANSFER", MessageStatus.Request.code(), 0);
    public static final Message ELECTION = new Message("ELECTION", MessageStatus.Request.code(), 0);
    public static final Message BULLIED = new Message("BULLIED", MessageStatus.Request.code(), 0);
    public static final Message LEADER = new Message("LEADER", MessageStatus.Request.code(), 0);
    public static final Message ACK = new Message("ACK", MessageStatus.Reply.code(), 0);
    public static final Message NAK = new Message("NACK", MessageStatus.Reply.code(), 0);
    public static final Message CREATEDR = new Message("CREATE_DR", MessageStatus.Request.code(), 0);
}
