package ca.concordia.encs.distributed.service.communication.multicast.queueing;

import ca.concordia.encs.distributed.service.messaging.IdentifiableMessage;
import ca.concordia.encs.distributed.service.communication.multicast.DeliveryQueueState;
import ca.concordia.encs.distributed.service.communication.MessageQueue;

import java.util.concurrent.LinkedBlockingQueue;

public class SequentialMessageQueue implements MessageQueue {
    private LinkedBlockingQueue<IdentifiableMessage> messages = new LinkedBlockingQueue<>();

    @Override
    public DeliveryQueueState enqueue(IdentifiableMessage identifiableMessage) {
        try {
            messages.put(identifiableMessage);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return DeliveryQueueState.ACCEPTED;
    }

    @Override
    public IdentifiableMessage take() throws InterruptedException {
        return messages.take();
    }

}
