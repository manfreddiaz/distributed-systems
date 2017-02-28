package ca.concordia.encs.distributed.service.communication;

import ca.concordia.encs.distributed.service.messaging.IdentifiableMessage;

public interface MessageListener {
    boolean receive(IdentifiableMessage message);
}
