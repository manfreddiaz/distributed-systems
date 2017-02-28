package ca.concordia.encs.distributed.service.messaging;

import ca.concordia.encs.distributed.messaging.Message;
import ca.concordia.encs.distributed.model.Location;
import java.io.Serializable;

public class IdentifiableMessage extends Message implements Serializable, Comparable<IdentifiableMessage> {
    public Integer RepliesTo;
    public Integer Id;
    public Integer To;
    public Boolean Ack = false;

    public IdentifiableMessage() {

    }
    public IdentifiableMessage(Integer id) {
        this.Id = id;
    }
    public Location getServerLocation() {
       return MessageIdGenerator.getServerLocation(this.Id);
    }
    public Integer getServerId() { return MessageIdGenerator.getServerId(this.Id); }
    public Integer getMessageId() {
        return MessageIdGenerator.getMessageId(this.Id);
    }

    public static IdentifiableMessage fromMessage(Message message, Integer id) {
        return fromMessage(message, id, null, null);
    }
    public static IdentifiableMessage fromMessage(Message message, Integer id, Integer to) {
        return fromMessage(message, id, to, null);
    }
    public static IdentifiableMessage fromMessage(Message message, Integer id, Integer to, Integer repliesTo) {
        IdentifiableMessage identifiableMessage = new IdentifiableMessage(id);

        identifiableMessage.To = to;
        identifiableMessage.RepliesTo = repliesTo;
        identifiableMessage.Content = message.Content;
        identifiableMessage.Method = message.Method;
        identifiableMessage.Status = message.Status;

        return identifiableMessage;
    }
    @Override
    public String toString() {
        return String.format("Action:%s, Id:%d, Replies:%d", this.Method, this.Id, this.RepliesTo);
    }

    @Override
    public int compareTo(IdentifiableMessage o) {
        return getMessageId().compareTo(o.getMessageId());
    }
}
