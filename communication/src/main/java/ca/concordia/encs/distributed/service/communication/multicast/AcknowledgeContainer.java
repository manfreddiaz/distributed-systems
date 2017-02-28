package ca.concordia.encs.distributed.service.communication.multicast;

import ca.concordia.encs.distributed.service.messaging.IdentifiableMessage;

import java.util.concurrent.atomic.AtomicInteger;

public class AcknowledgeContainer implements Comparable<AcknowledgeContainer> {
    private AtomicInteger ref = new AtomicInteger(3);
    private IdentifiableMessage message;

    public AcknowledgeContainer(IdentifiableMessage message) {
        this.message = message;
    }

    public IdentifiableMessage message() {
        return this.message;
    }
    public int ref() {
        return ref.incrementAndGet();
    }
    public int unref() {
        return ref.decrementAndGet();
    }

    @Override
    public int compareTo(AcknowledgeContainer o) {
        return o.message.Id.compareTo(message.Id);
    }
}
