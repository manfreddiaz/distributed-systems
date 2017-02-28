package ca.concordia.encs.distributed.service;

import ca.concordia.encs.distributed.messaging.Message;
import ca.concordia.encs.distributed.service.communication.MessageListener;
import ca.concordia.encs.distributed.service.messaging.IdentifiableMessage;

public interface CommunicationComponent extends Manageable {
    IdentifiableMessage request(String method, Object content);
    IdentifiableMessage request(Message message);
    IdentifiableMessage reply(Message message, Integer replyToId, Integer to);
    void subscribe(String topic, MessageListener listener);
    void unsubscribe(String topic, MessageListener listener);
}
