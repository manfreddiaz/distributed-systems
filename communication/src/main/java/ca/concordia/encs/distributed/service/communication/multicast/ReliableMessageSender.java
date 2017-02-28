package ca.concordia.encs.distributed.service.communication.multicast;

import ca.concordia.encs.distributed.service.communication.MessageListener;
import ca.concordia.encs.distributed.service.communication.MessageQueue;
import ca.concordia.encs.distributed.service.messaging.DefaultSystemMessages;
import ca.concordia.encs.distributed.service.messaging.IdentifiableMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ReliableMessageSender implements Runnable, MessageListener {
    private static Logger logger = (Logger) LoggerFactory.getLogger(ReliableMulticastComponent.class);
    private PriorityQueue<AcknowledgeContainer> nonACKQueue = new PriorityQueue<>();

    private AtomicBoolean stopped = new AtomicBoolean(false);
    private static Integer ACK_ROLLOVER = 17; //Empirical value.
    private AtomicInteger outgoingCounter = new AtomicInteger(0);
    private MessageQueue outgoingQueue;
    private ReliableMulticastComponent component;

    ReliableMessageSender(ReliableMulticastComponent component) {
        this.component = component;
        this.outgoingQueue = component.sender().outgoing();
        this.component.subscribe(DefaultSystemMessages.NAK.Method, this);
        this.component.subscribe(DefaultSystemMessages.ACK.Method, this);
    }

    public void enqueue(IdentifiableMessage message) {
        if(outgoingCounter.incrementAndGet() % ACK_ROLLOVER == 0) {
            logger.debug("[R-SENDER] Required ACK");
            message.Ack = true;
        }
        this.nonACKQueue.add(new AcknowledgeContainer(message));
        logger.debug("[R-SENDER] Message outgoing... {}", message.Method);
        this.outgoingQueue.enqueue(message);
        logger.debug("[R-SENDER] Message enqueue.");
    }
    @Override
    public void run() {
        while(!stopped.get()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
    }

    @Override
    public boolean receive(IdentifiableMessage message) {
        if(message.Method.equalsIgnoreCase(DefaultSystemMessages.ACK.Method)) {
            logger.debug("[R-SENDER] Received ACK");
            clean(message);
        }
        if(message.Method.equalsIgnoreCase(DefaultSystemMessages.NAK.Method)) {
            logger.debug("[R-SENDER] Received NACK");
            forward(message);
        }
        return true;
    }

    private void forward(IdentifiableMessage message) {
        synchronized (nonACKQueue) {
            nonACKQueue.forEach(container -> {
                if(container.message().Id < ((Double) message.Content).longValue()) {
                    container.message().Ack = false;
                    component.request(container.message());
                    container.ref();
                }
            });
        }
        logger.debug("[R-SENDER] Forwarding messages done.");
    }

    private void clean(IdentifiableMessage message) {
        synchronized (nonACKQueue) {
            nonACKQueue.removeIf(container -> {
                if(container.unref() == 0) {
                    return container.message().Id < ((Double) message.Content).longValue();
                }
                return false;
            });
        }
        logger.debug("[R-SENDER] NONACK storage emptied, State = {}", nonACKQueue.size());
    }
}
