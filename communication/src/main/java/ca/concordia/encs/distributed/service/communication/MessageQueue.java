package ca.concordia.encs.distributed.service.communication;

import ca.concordia.encs.distributed.service.communication.multicast.DeliveryQueueState;
import ca.concordia.encs.distributed.service.messaging.IdentifiableMessage;

/**
 * Created by linux on 8/7/2016.
 */
public interface MessageQueue {
    DeliveryQueueState enqueue(IdentifiableMessage identifiableMessage);
    IdentifiableMessage take() throws InterruptedException;
}
