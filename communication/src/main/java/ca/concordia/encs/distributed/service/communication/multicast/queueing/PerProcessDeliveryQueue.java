package ca.concordia.encs.distributed.service.communication.multicast.queueing;

import ca.concordia.encs.distributed.service.messaging.IdentifiableMessage;
import ca.concordia.encs.distributed.service.communication.MessageQueue;
import ca.concordia.encs.distributed.service.communication.multicast.DeliveryQueueState;
import ca.concordia.encs.distributed.service.communication.multicast.ReliableMessageDispatcher;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicLong;

public class PerProcessDeliveryQueue implements MessageQueue {
    private AtomicLong lastDeliveredMessage = new AtomicLong(0);
    private ConcurrentLinkedDeque<IdentifiableMessage> messages = new ConcurrentLinkedDeque<>();
    private ReliableMessageDispatcher dispatcher;

    public PerProcessDeliveryQueue(ReliableMessageDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public DeliveryQueueState enqueue(IdentifiableMessage message) {
        DeliveryQueueState deliveryState = DeliveryQueueState.ACCEPTED;

        if(lastDeliveredMessage.get() == 0) {
            lastDeliveredMessage.set(message.getMessageId() - 1);

        }

        messages.add(message);

        promote();

        return deliveryState;
    }
    @Override
    public IdentifiableMessage take() {
        return null;
    }

    public long lastDelivered() {
        return lastDeliveredMessage.get();
    }
    public void promote() {
        IdentifiableMessage firstMessageOnQueue = messages.peek();

        while(firstMessageOnQueue != null) {
            dispatcher.deliver(messages.poll());
            lastDeliveredMessage.incrementAndGet();
            firstMessageOnQueue = messages.peek();
        }
    }
}
